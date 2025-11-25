package util;

import model.Batch;
import model.Slot;
import dao.MedicineDao;
import dao.SlotDao;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.List;

public class CsvExporter {

    private final SlotDao slotDao = new SlotDao();
    private final MedicineDao medicineDao = new MedicineDao();

    public CsvExporter() { }
    public void exportBatches(List<Batch> batches, String filePath) {
        Path output = Paths.get(filePath);

        try {
            if (!Files.exists(output.getParent())) {
                Files.createDirectories(output.getParent());
            }
        } catch (IOException e) {
            System.err.println("Failed to create directories for CSV: " + e.getMessage());
        }

        String header = "batch_id,medicine_name,composition,qty,mfg_date,exp_date,slot_zone,slot_shelf,slot_id\n";

        try (BufferedWriter bw = Files.newBufferedWriter(output,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            bw.write(header);

            for (Batch b : batches) {
                String medName = "";
                try {
                    var med = medicineDao.getById(b.getMedicineId());
                    medName = med != null ? med.getName() : "";
                } catch (SQLException ignored) {}

                String zone = "";
                String shelf = "";
                String slotId = "";

                if (b.getSlotId() != null) {
                    try {
                        var slotOpt = slotDao.getSlotById(b.getSlotId());
                        if (slotOpt.isPresent()) {
                            Slot s = slotOpt.get();
                            zone = s.getZone();
                            shelf = s.getShelfNumber();
                            slotId = String.valueOf(s.getSlotId());
                        }
                    } catch (SQLException ignored) {}
                }

                String line = String.format("%d,%s,%s,%d,%s,%s,%s,%s,%s\n",
                        b.getBatchId(),
                        escape(medName),
                        escape(b.getComposition()),
                        b.getQuantity(),
                        b.getMfgDate() != null ? b.getMfgDate().toString() : "",
                        b.getExpDate() != null ? b.getExpDate().toString() : "",
                        escape(zone),
                        escape(shelf),
                        slotId
                );

                bw.write(line);
            }

        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }


    private String escape(String s) {
        if (s == null) return "";
        s = s.replace("\"", "\"\"");
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            s = "\"" + s + "\"";
        }
        return s;
    }
}
