package app;

import service.ExpiryScheduler;
import java.io.IOException;

public class App {

    public static void main(String[] args) {
        System.out.println("=== Medicine Expiry Tracker ===");

        int warningDays = 30;
        long intervalSeconds = 60L;

        ExpiryScheduler scheduler = new ExpiryScheduler(warningDays, intervalSeconds);
        scheduler.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            scheduler.stop();
        }));

        try {
            ConsoleUI ui = new ConsoleUI();
            ui.run();
        } catch (IOException e) {
            System.err.println("Failed to start UI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
