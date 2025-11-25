package dao;

import model.Medicine;
import util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineDao {

    public int insertMedicine(Medicine m) throws SQLException {
        String sql = "INSERT INTO medicine (name, manufacturer, supplier) VALUES (?,?,?)";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getName());
            ps.setString(2, m.getManufacturer());
            ps.setString(3, m.getSupplier());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public Medicine getById(int id) throws SQLException {
        String sql = "SELECT * FROM medicine WHERE medicine_id=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Medicine m = new Medicine();
                    m.setMedicineId(rs.getInt("medicine_id"));
                    m.setName(rs.getString("name"));
                    m.setManufacturer(rs.getString("manufacturer"));
                    m.setSupplier(rs.getString("supplier"));
                    return m;
                }
            }
        }
        return null;
    }
    public List<Medicine> findByName(String nameLike) throws SQLException {
        String sql = "SELECT * FROM medicine WHERE LOWER(name) LIKE ? ORDER BY name";
        List<Medicine> out = new ArrayList<>();
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + nameLike.toLowerCase().trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Medicine m = new Medicine();
                    m.setMedicineId(rs.getInt("medicine_id"));
                    m.setName(rs.getString("name"));
                    m.setManufacturer(rs.getString("manufacturer"));
                    m.setSupplier(rs.getString("supplier"));
                    out.add(m);
                }
            }
        }
        return out;
    }
    public List<Medicine> getAll() throws SQLException {
        String sql = "SELECT * FROM medicine ORDER BY name";
        List<Medicine> out = new ArrayList<>();
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Medicine m = new Medicine();
                m.setMedicineId(rs.getInt("medicine_id"));
                m.setName(rs.getString("name"));
                m.setManufacturer(rs.getString("manufacturer"));
                m.setSupplier(rs.getString("supplier"));
                out.add(m);
            }
        }
        return out;
    }
    public boolean updateMedicine(Medicine m) throws SQLException {
        String sql = "UPDATE medicine SET name=?, manufacturer=?, supplier=? WHERE medicine_id=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getName());
            ps.setString(2, m.getManufacturer());
            ps.setString(3, m.getSupplier());
            ps.setInt(4, m.getMedicineId());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean deleteMedicine(int id) throws SQLException {
        String sql = "DELETE FROM medicine WHERE medicine_id=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
