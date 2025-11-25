package dao;

import model.Slot;
import util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SlotDao {
    public int createSlot(Slot s) throws SQLException {
        String sql = "INSERT INTO slot (zone, shelf_number, composition_tag, capacity, current_quantity) VALUES (?,?,?,?,?)";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getZone());
            ps.setString(2, s.getShelfNumber());
            ps.setString(3, s.getCompositionTag());
            ps.setInt(4, s.getCapacity());
            ps.setInt(5, s.getCurrentQuantity());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }
    public Optional<Slot> findSlotWithCompositionAndSpace(String composition, int neededQty) throws SQLException {
        String sql = "SELECT * FROM slot WHERE composition_tag=? AND (capacity - current_quantity) >= ? ORDER BY zone, shelf_number LIMIT 1";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, composition);
            ps.setInt(2, neededQty);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(parseSlot(rs));
            }
        }
        return Optional.empty();
    }
    public Optional<Slot> findEmptySlot(int neededQty) throws SQLException {
        String sql = "SELECT * FROM slot WHERE composition_tag IS NULL AND capacity >= ? ORDER BY zone, shelf_number LIMIT 1";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, neededQty);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(parseSlot(rs));
            }
        }
        return Optional.empty();
    }
    public Optional<Slot> getSlotById(int slotId) throws SQLException {
        String sql = "SELECT * FROM slot WHERE slot_id=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(parseSlot(rs));
            }
        }
        return Optional.empty();
    }
    public List<Slot> getAllSlots() throws SQLException {
        String sql = "SELECT * FROM slot ORDER BY zone, shelf_number";
        List<Slot> out = new ArrayList<>();
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(parseSlot(rs));
        }
        return out;
    }
    public void assignBatchToSlot(Connection txConn, int slotId, String composition, int addQuantity) throws SQLException {
        String sql = "UPDATE slot SET composition_tag=?, current_quantity=current_quantity+? WHERE slot_id=?";
        try (PreparedStatement ps = txConn.prepareStatement(sql)) {
            ps.setString(1, composition);
            ps.setInt(2, addQuantity);
            ps.setInt(3, slotId);
            ps.executeUpdate();
        }
    }
    public void removeQuantityFromSlot(Connection txConn, int slotId, int qty) throws SQLException {
        String sql = "UPDATE slot SET current_quantity=current_quantity-? WHERE slot_id=?";
        try (PreparedStatement ps = txConn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, slotId);
            ps.executeUpdate();
        }
        try (PreparedStatement check = txConn.prepareStatement("SELECT current_quantity FROM slot WHERE slot_id=?")) {
            check.setInt(1, slotId);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next() && rs.getInt("current_quantity") <= 0) {
                    try (PreparedStatement reset = txConn.prepareStatement("UPDATE slot SET composition_tag=NULL, current_quantity=0 WHERE slot_id=?")) {
                        reset.setInt(1, slotId);
                        reset.executeUpdate();
                    }
                }
            }
        }
    }

    private Slot parseSlot(ResultSet rs) throws SQLException {
        Slot s = new Slot();
        s.setSlotId(rs.getInt("slot_id"));
        s.setZone(rs.getString("zone"));
        s.setShelfNumber(rs.getString("shelf_number"));
        s.setCompositionTag(rs.getString("composition_tag"));
        s.setCapacity(rs.getInt("capacity"));
        s.setCurrentQuantity(rs.getInt("current_quantity"));
        return s;
    }
}
