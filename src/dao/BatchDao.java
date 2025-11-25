package dao;

import model.Batch;
import util.DbUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BatchDao {
    public int insertBatch(Connection txConn, Batch batch) throws SQLException {
        String sql = "INSERT INTO batch (medicine_id, composition, quantity, mfg_date, exp_date, slot_id) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = txConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, batch.getMedicineId());
            ps.setString(2, batch.getComposition());
            ps.setInt(3, batch.getQuantity());
            if (batch.getMfgDate() != null) ps.setDate(4, Date.valueOf(batch.getMfgDate()));
            else ps.setNull(4, Types.DATE);
            if (batch.getExpDate() != null) ps.setDate(5, Date.valueOf(batch.getExpDate()));
            else ps.setNull(5, Types.DATE);
            if (batch.getSlotId() != null) ps.setInt(6, batch.getSlotId());
            else ps.setNull(6, Types.INTEGER);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }
    public Batch getBatchById(int batchId) throws SQLException {
        String sql = "SELECT * FROM batch WHERE batch_id=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, batchId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Batch b = new Batch();
                    b.setBatchId(rs.getInt("batch_id"));
                    b.setMedicineId(rs.getInt("medicine_id"));
                    b.setComposition(rs.getString("composition"));
                    b.setQuantity(rs.getInt("quantity"));
                    Date m = rs.getDate("mfg_date");
                    Date e = rs.getDate("exp_date");
                    if (m != null) b.setMfgDate(m.toLocalDate());
                    if (e != null) b.setExpDate(e.toLocalDate());
                    int slot = rs.getInt("slot_id");
                    if (!rs.wasNull()) b.setSlotId(slot);
                    return b;
                }
            }
        }
        return null;
    }
    public List<Batch> getExpiringWithinDays(int days) throws SQLException {
        String sql = "SELECT * FROM batch WHERE exp_date IS NOT NULL AND exp_date <= DATE_ADD(CURDATE(), INTERVAL ? DAY) ORDER BY exp_date";
        List<Batch> out = new ArrayList<>();
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, days);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Batch b = new Batch();
                    b.setBatchId(rs.getInt("batch_id"));
                    b.setMedicineId(rs.getInt("medicine_id"));
                    b.setComposition(rs.getString("composition"));
                    b.setQuantity(rs.getInt("quantity"));
                    Date m = rs.getDate("mfg_date");
                    Date e = rs.getDate("exp_date");
                    if (m != null) b.setMfgDate(m.toLocalDate());
                    if (e != null) b.setExpDate(e.toLocalDate());
                    int slot = rs.getInt("slot_id");
                    if (!rs.wasNull()) b.setSlotId(slot);
                    out.add(b);
                }
            }
        }
        return out;
    }
    public void deleteBatch(Connection txConn, int batchId) throws SQLException {
        try (PreparedStatement ps = txConn.prepareStatement("DELETE FROM batch WHERE batch_id=?")) {
            ps.setInt(1, batchId);
            ps.executeUpdate();
        }
    }
    public List<Batch> getAllBatches() throws SQLException {
        String sql = "SELECT * FROM batch ORDER BY exp_date NULLS LAST, batch_id";
        List<Batch> out = new ArrayList<>();
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Batch b = new Batch();
                b.setBatchId(rs.getInt("batch_id"));
                b.setMedicineId(rs.getInt("medicine_id"));
                b.setComposition(rs.getString("composition"));
                b.setQuantity(rs.getInt("quantity"));
                Date m = rs.getDate("mfg_date");
                Date e = rs.getDate("exp_date");
                if (m != null) b.setMfgDate(m.toLocalDate());
                if (e != null) b.setExpDate(e.toLocalDate());
                int slot = rs.getInt("slot_id");
                if (!rs.wasNull()) b.setSlotId(slot);
                out.add(b);
            }
        }
        return out;
    }
}
