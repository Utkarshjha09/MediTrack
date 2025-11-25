package service;

import dao.BatchDao;
import dao.MedicineDao;
import dao.SlotDao;
import model.Batch;
import model.Slot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;




public class ExpiryScheduler {

    private final int warningDays;            // batches expiring within this many days will trigger alerts
    private final long intervalSeconds;       // how often to run check (seconds)
    private final ScheduledExecutorService scheduler;
    private final BatchDao batchDao;
    private final MedicineDao medicineDao;
    private final SlotDao slotDao;
    private final AlertService alertService;
    private ScheduledFuture<?> scheduledFuture;
    public ExpiryScheduler(int warningDays, long intervalSeconds) {
        this.warningDays = warningDays;
        this.intervalSeconds = intervalSeconds;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "ExpiryScheduler");
            t.setDaemon(true);
            return t;
        });
        this.batchDao = new BatchDao();
        this.medicineDao = new MedicineDao();
        this.slotDao = new SlotDao();
        this.alertService = new AlertService(); // uses its own DAOs internally
    }
    public void start() {
        if (scheduledFuture != null && !scheduledFuture.isDone()) return;
        scheduledFuture = scheduler.scheduleAtFixedRate(this::runCheckSafely, 0, intervalSeconds, TimeUnit.SECONDS);
        System.out.println("ExpiryScheduler started: checking every " + intervalSeconds + "s for next " + warningDays + " days.");
    }
    public void stop() {
        if (scheduledFuture != null) scheduledFuture.cancel(false);
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("ExpiryScheduler stopped.");
    }
    private void runCheckSafely() {
        try {
            runCheck();
        } catch (Throwable t) {
            System.err.println(LocalDateTime.now() + " - ExpiryScheduler encountered an error: " + t.getMessage());
            t.printStackTrace();
        }
    }
    private void runCheck() {
        try {
            List<Batch> expiring = batchDao.getExpiringWithinDays(warningDays);
            if (expiring == null || expiring.isEmpty()) {
                return;
            }

            for (Batch b : expiring) {
                String medName = "";
                try {
                    var med = medicineDao.getById(b.getMedicineId());
                    medName = (med != null) ? med.getName() : "(unknown medicine)";
                } catch (Exception ex) {
                    medName = "(error fetching medicine)";
                }

                String slotInfo = "N/A";
                try {
                    if (b.getSlotId() != null) {
                        Optional<Slot> slotOpt = slotDao.getSlotById(b.getSlotId());
                        if (slotOpt.isPresent()) {
                            Slot s = slotOpt.get();
                            slotInfo = String.format("Zone=%s Shelf=%s (slotId=%d, currentQty=%d)",
                                    s.getZone(), s.getShelfNumber(), s.getSlotId(), s.getCurrentQuantity());
                        } else {
                            slotInfo = "Slot not found (id=" + b.getSlotId() + ")";
                        }
                    } else {
                        slotInfo = "Unassigned slot";
                    }
                } catch (Exception ex) {
                    slotInfo = "Error fetching slot";
                }

                String msg = String.format("ALERT: BatchId=%d Medicine='%s' Composition='%s' Qty=%d Expiry=%s -- %s",
                        b.getBatchId(),
                        medName,
                        b.getComposition(),
                        b.getQuantity(),
                        (b.getExpDate() != null ? b.getExpDate().toString() : "N/A"),
                        slotInfo
                );
                alertService.createAlert(b.getBatchId(), msg);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while running expiry check: " + e.getMessage(), e);
        }
    }
}
