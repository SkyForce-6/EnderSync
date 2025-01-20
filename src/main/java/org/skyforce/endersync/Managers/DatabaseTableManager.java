package org.skyforce.endersync.Managers;

import java.sql.SQLException;

public class DatabaseTableManager {
    private final DatabaseManager databaseManager;

    public DatabaseTableManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS enderchests (" +
                "uuid VARCHAR(36) NOT NULL, " +
                "items TEXT NOT NULL, " +
                "PRIMARY KEY (uuid)" +
                ");";
        try {
            databaseManager.executeUpdate(createTableSQL);
            System.out.println("Table 'enderchests' successfully created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }
}