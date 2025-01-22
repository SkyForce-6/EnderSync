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
            System.out.println("Tabelle 'enderchests' erfolgreich erstellt oder existiert bereits.");
        } catch (SQLException e) {
            System.err.println("Fehler beim Erstellen der Tabelle: " + e.getMessage());
        }
    }
}