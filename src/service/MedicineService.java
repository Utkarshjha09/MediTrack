package service;

import dao.BatchDao;
import dao.MedicineDao;
import dao.SlotDao;
import model.Batch;
import model.Slot;
import util.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public class MedicineService {

    private final SlotDao slotDao = new SlotDao();
    private final BatchDao batchDao = new BatchDao();
    private final MedicineDao medicineDao = new MedicineDao();
    public synchronized boolean addBatchWithAllocation(Batch batch) throws SQLException {
        if (batch == null) throw new IllegalArgumentException("Batch cannot be null");
        String composition = batch.getComposition();
        if (composition == null || composition.trim().isEmpty())
            throw new IllegalArgumentException("Batch composition must be provided");

        int remaining = batch.getQuantity();
        if (remaining <= 0) throw new IllegalArgumentException("Batch quantity must be positive");
        composition = composition.trim();
        batch.setComposition(composition);

        try (Connection conn = DbUtil.getConnection()) {
            try {
                conn.setAutoCommit(false);
                while (remaining > 0) {
                    Optional<Slot> existingOpt = slotDao.findSlotWithCompositionAndSpace(composition, 1);
                    if (existingOpt.isPresent()) {
                        Slot s = existingOpt.get();
                        int free = s.freeSpace();
                        int take = Math.min(free, remaining);
                        slotDao.assignBatchToSlot(conn, s.getSlotId(), composition, take);
                        Batch portion = new Batch(batch.getMedicineId(), composition, take, batch.getMfgDate(), batch.getExpDate());
                        portion.setSlotId(s.getSlotId());
                        int insertedId = batchDao.insertBatch(conn, portion);
                        portion.setBatchId(insertedId);

                        remaining -= take;
                        continue;
                    }
                    Optional<Slot> emptyOpt = slotDao.findEmptySlot(1);
                    if (emptyOpt.isPresent()) {
                        Slot s2 = emptyOpt.get();
                        int free2 = s2.freeSpace();
                        int take2 = Math.min(free2, remaining);

                        slotDao.assignBatchToSlot(conn, s2.getSlotId(), composition, take2);

                        Batch portion2 = new Batch(batch.getMedicineId(), composition, take2, batch.getMfgDate(), batch.getExpDate());
                        portion2.setSlotId(s2.getSlotId());
                        int id2 = batchDao.insertBatch(conn, portion2);
                        portion2.setBatchId(id2);

                        remaining -= take2;
                        continue;
                    }
                    conn.rollback();
                    return false;
                }
                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    public synchronized boolean removeBatch(int batchId) throws SQLException {
        var batch = batchDao.getBatchById(batchId);
        if (batch == null) return false;

        Integer slotId = batch.getSlotId();
        int qty = batch.getQuantity();

        try (Connection conn = DbUtil.getConnection()) {
            try {
                conn.setAutoCommit(false);

                if (slotId != null) {
                    slotDao.removeQuantityFromSlot(conn, slotId, qty);
                }

                batchDao.deleteBatch(conn, batchId);

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    public List<Batch> getExpiringBatches(int days) throws SQLException {
        if (days < 0) throw new IllegalArgumentException("days must be >= 0");
        return batchDao.getExpiringWithinDays(days);
    }
    public int createSlot(Slot s) throws SQLException {
        if (s == null) throw new IllegalArgumentException("Slot cannot be null");
        return slotDao.createSlot(s);
    }
    public synchronized boolean moveBatchToSlot(int batchId, int targetSlotId) throws SQLException {
        var batch = batchDao.getBatchById(batchId);
        if (batch == null) return false;
        int qty = batch.getQuantity();
        String comp = batch.getComposition();
        Optional<Slot> targetOpt = slotDao.getSlotById(targetSlotId);
        if (targetOpt.isEmpty()) return false;
        Slot target = targetOpt.get();
        if (target.freeSpace() < qty) return false; // insufficient space

        try (Connection conn = DbUtil.getConnection()) {
            try {
                conn.setAutoCommit(false);
                if (batch.getSlotId() != null) {
                    slotDao.removeQuantityFromSlot(conn, batch.getSlotId(), qty);
                }
                slotDao.assignBatchToSlot(conn, targetSlotId, comp, qty);
                try (var ps = conn.prepareStatement("UPDATE batch SET slot_id=? WHERE batch_id=?")) {
                    ps.setInt(1, targetSlotId);
                    ps.setInt(2, batchId);
                    ps.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    public List<Slot> getAllSlots() throws SQLException {
        return slotDao.getAllSlots();
    }
    public int createMedicine(model.Medicine m) throws SQLException {
        return medicineDao.insertMedicine(m);
    }
    public List<Batch> getBatchesExpiringBetween(LocalDate from, LocalDate to) throws SQLException {
        int days = (int) (to.toEpochDay() - LocalDate.now().toEpochDay());
        if (days < 0) return List.of();
        return batchDao.getExpiringWithinDays(days);
    }
}
