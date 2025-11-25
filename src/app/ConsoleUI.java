package app;

import dao.BatchDao;
import dao.MedicineDao;
import model.Batch;
import model.Medicine;
import model.Slot;
import service.AlertService;
import service.MedicineService;
import util.CsvExporter;
import util.DateUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);
    private final MedicineService medicineService = new MedicineService();
    private final MedicineDao medicineDao = new MedicineDao();
    private final BatchDao batchDao = new BatchDao();
    private final AlertService alertService = new AlertService();
    private final CsvExporter csvExporter = new CsvExporter();

    public ConsoleUI() throws IOException {
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1": createSlot(); break;
                    case "2": createMedicine(); break;
                    case "3": addBatch(); break;
                    case "4": listSlots(); break;
                    case "5": listBatches(); break;
                    case "6": exportExpiringCsv(); break;
                    case "7": manualExpiryCheck(); break;
                    case "8": running = false; break;
                    default: System.out.println("Invalid choice"); break;
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Exiting UI. Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1) Create Slot");
        System.out.println("2) Add Medicine");
        System.out.println("3) Add Batch (auto-allocate)");
        System.out.println("4) List Slots");
        System.out.println("5) List Batches (all)");
        System.out.println("6) Export Expiring Batches CSV (30 days)");
        System.out.println("7) Run Manual Expiry Check (one-time)");
        System.out.println("8) Exit");
        System.out.print("Choose: ");
    }

    private void createSlot() throws SQLException {
        System.out.print("Zone (e.g. A): ");
        String zone = scanner.nextLine().trim();
        System.out.print("Shelf number (e.g. A-1): ");
        String shelf = scanner.nextLine().trim();
        System.out.print("Capacity (integer, e.g. 100): ");
        int capacity = Integer.parseInt(scanner.nextLine().trim());

        Slot s = new Slot(zone, shelf, capacity);
        int id = medicineService.createSlot(s);
        if (id > 0) System.out.println("Created slot id=" + id);
        else System.out.println("Failed to create slot.");
    }

    private void createMedicine() throws SQLException {
        System.out.print("Medicine name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Manufacturer: ");
        String mf = scanner.nextLine().trim();
        System.out.print("Supplier: ");
        String sup = scanner.nextLine().trim();

        Medicine m = new Medicine(name, mf, sup);
        int id = medicineService.createMedicine(m);
        if (id > 0) System.out.println("Created medicine id=" + id);
        else System.out.println("Failed to create medicine.");
    }

    private void addBatch() throws SQLException {
        System.out.print("Medicine id: ");
        int medId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Composition (e.g., Paracetamol 500mg): ");
        String comp = scanner.nextLine().trim();
        System.out.print("Quantity (int): ");
        int qty = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Mfg date (YYYY-MM-DD) or blank: ");
        String mfg = scanner.nextLine().trim();
        System.out.print("Exp date (YYYY-MM-DD) or blank: ");
        String exp = scanner.nextLine().trim();

        LocalDate mfgDate = mfg.isEmpty() ? null : LocalDate.parse(mfg);
        LocalDate expDate = exp.isEmpty() ? null : LocalDate.parse(exp);

        Batch b = new Batch(medId, comp, qty, mfgDate, expDate);

        boolean ok = medicineService.addBatchWithAllocation(b);
        if (ok) System.out.println("Batch added and allocated successfully.");
        else System.out.println("Failed to allocate batch. Consider creating more slots or splitting manually.");
    }

    private void listSlots() throws SQLException {
        List<Slot> slots = medicineService.getAllSlots();
        System.out.println("Slots:");
        for (Slot s : slots) {
            System.out.println(s);
        }
    }

    private void listBatches() throws SQLException {
        List<Batch> batches = batchDao.getAllBatches();
        System.out.println("Batches:");
        for (Batch b : batches) {
            String days = (b.getExpDate() != null) ? String.valueOf(DateUtil.daysFromToday(b.getExpDate())) + " days" : "N/A";
            System.out.printf("ID:%d MedId:%d Comp:%s Qty:%d Exp:%s (%s) Slot:%s%n",
                    b.getBatchId(), b.getMedicineId(), b.getComposition(), b.getQuantity(),
                    b.getExpDate() != null ? b.getExpDate().toString() : "N/A",
                    days,
                    b.getSlotId() != null ? b.getSlotId().toString() : "Unassigned");
        }
    }

    private void exportExpiringCsv() throws SQLException {
        int days = 30;
        List<Batch> expiring = medicineService.getExpiringBatches(days);
        String out = "resources/reports/expiring_report.csv";
        csvExporter.exportBatches(expiring, out);
        System.out.println("Exported " + expiring.size() + " rows to " + out);
    }

    private void manualExpiryCheck() throws SQLException {
        int days = 30;
        List<Batch> expiring = medicineService.getExpiringBatches(days);
        if (expiring.isEmpty()) {
            System.out.println("No batches expiring within " + days + " days.");
            return;
        }
        for (Batch b : expiring) {
            String medName = "";
            try {
                var med = medicineDao.getById(b.getMedicineId());
                medName = med != null ? med.getName() : "(unknown)";
            } catch (Exception ex) { medName = "(err)"; }

            String slotInfo = b.getSlotId() != null ? "slotId=" + b.getSlotId() : "Unassigned";
            String msg = String.format("MANUAL ALERT: BatchId=%d Medicine='%s' Comp='%s' Qty=%d Expiry=%s -- %s",
                    b.getBatchId(), medName, b.getComposition(), b.getQuantity(),
                    b.getExpDate() != null ? b.getExpDate().toString() : "N/A", slotInfo);

            alertService.createAlert(b.getBatchId(), msg);
            System.out.println("Alert created: " + msg);
        }
        csvExporter.exportBatches(expiring, "resources/reports/manual_expiring_report.csv");
        System.out.println("Manual CSV exported.");
    }
}
