package service;

import dao.BatchDao;
import dao.MedicineDao;
import dao.SlotDao;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import model.Batch;
import model.Slot;
public class AlertService {

    private static final Path ALERT_LOG_DIR = Paths.get("resources", "logs");
    private static final Path ALERT_LOG_FILE = ALERT_LOG_DIR.resolve("expiry_alerts.txt");
    private static final Path REPORTS_DIR = Paths.get("resources", "reports");

    private final BatchDao batchDao = new BatchDao();
    private final MedicineDao medicineDao = new MedicineDao();
    private final SlotDao slotDao = new SlotDao();

    public AlertService() {
        try {
            if (!Files.exists(ALERT_LOG_DIR)) Files.createDirectories(ALERT_LOG_DIR);
            if (!Files.exists(REPORTS_DIR)) Files.createDirectories(REPORTS_DIR);
            if (!Files.exists(ALERT_LOG_FILE)) Files.createFile(ALERT_LOG_FILE);
        } catch (IOException e) {
            System.err.println("Could not create alert log directories/files: " + e.getMessage());
        }
    }
    public void createAlert(Integer batchId, String message) {
        try (Connection c = util.DbUtil.getConnection()) {
            String sql = "INSERT INTO alerts (batch_id, message) VALUES (?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                if (batchId != null) ps.setInt(1, batchId);
                else ps.setNull(1, java.sql.Types.INTEGER);
                ps.setString(2, message);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.err.println(LocalDateTime.now() + " - Failed to insert alert into DB: " + ex.getMessage());
        }
        appendToLog(message);
    }

    private void appendToLog(String message) {
        String line = String.format("[%s] %s%n", LocalDateTime.now(), message);
        try (BufferedWriter bw = Files.newBufferedWriter(ALERT_LOG_FILE, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(line);
        } catch (IOException e) {
            System.err.println("Failed to write alert to log file: " + e.getMessage());
        }
    }
    public void exportExpiringBatchesCsv(List<Batch> batches, String filename) {
        Path out = Paths.get(filename);
        try {
            if (!Files.exists(out.getParent())) Files.createDirectories(out.getParent());
        } catch (IOException e) {
            System.err.println("Could not create reports directory: " + e.getMessage());
            return;
        }

        String header = "batch_id,medicine_name,composition,quantity,exp_date,slot_zone,slot_shelf,slot_id\n";
        try (BufferedWriter bw = Files.newBufferedWriter(out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            bw.write(header);
            for (Batch b : batches) {
                String medName = "";
                try {
                    var med = medicineDao.getById(b.getMedicineId());
                    medName = (med != null) ? med.getName() : "";
                } catch (Exception ex) {
                    medName = "";
                }

                String zone = "";
                String shelf = "";
                String slotId = "";
                try {
                    if (b.getSlotId() != null) {
                        var slotOpt = slotDao.getSlotById(b.getSlotId());
                        if (slotOpt.isPresent()) {
                            Slot s = slotOpt.get();
                            zone = s.getZone() != null ? s.getZone() : "";
                            shelf = s.getShelfNumber() != null ? s.getShelfNumber() : "";
                            slotId = Integer.toString(s.getSlotId());
                        }
                    }
                } catch (Exception ex) {
                }

                String line = String.format("%d,%s,%s,%d,%s,%s,%s,%s\n",
                        b.getBatchId(),
                        escapeCsv(medName),
                        escapeCsv(b.getComposition()),
                        b.getQuantity(),
                        b.getExpDate() != null ? b.getExpDate().toString() : "",
                        escapeCsv(zone),
                        escapeCsv(shelf),
                        slotId
                );
                bw.write(line);
            }
        } catch (IOException e) {
            System.err.println("Failed to write CSV report: " + e.getMessage());
        }
    }

    private String escapeCsv(String s) {
        if (s == null) return "";
        String out = s.replace("\"", "\"\""); // escape quotes
        if (out.contains(",") || out.contains("\"") || out.contains("\n")) {
            out = "\"" + out + "\"";
        }
        return out;
    }
}
