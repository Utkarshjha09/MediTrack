package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class DbUtil {

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASS;

    static {
        loadProperties();
        loadDriver();
    }
    private static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("[DbUtil] ERROR: MySQL JDBC Driver not found!");
            System.err.println("Make sure mysql-connector-java-x.x.x.jar is inside /lib/");
        }
    }
    private static void loadProperties() {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream("config/app.properties")) {
            props.load(fis);

            DB_URL  = props.getProperty("db.url");
            DB_USER = props.getProperty("db.user");
            DB_PASS = props.getProperty("db.pass");

            if (DB_URL == null || DB_USER == null || DB_PASS == null) {
                throw new RuntimeException("Missing DB configuration in app.properties");
            }

        } catch (IOException e) {
            System.err.println("[DbUtil] ERROR loading config/app.properties");
            System.err.println("Make sure the file exists and contains:");
            System.err.println("db.url=...");
            System.err.println("db.user=...");
            System.err.println("db.pass=...");
            throw new RuntimeException("Failed to load DB configuration", e);
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}
