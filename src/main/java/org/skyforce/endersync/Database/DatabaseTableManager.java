package org.skyforce.endersync.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTableManager {
    private final DatabaseManager databaseManager;

    public DatabaseTableManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void createTableIfNotExists(String tableName, String tableSchema) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + tableSchema + ");";

        try (Connection connection = databaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
            System.out.println("Table " + tableName + " created or already exists.");
        }
    }

    public void createEnderChestTableIfNotExists(String tableName) throws SQLException {
        String tableSchema = "uuid VARCHAR(36) NOT NULL, items TEXT NOT NULL, PRIMARY KEY (uuid)";
        createTableIfNotExists(tableName, tableSchema);
    }

    public void createInventoryTableIfNotExists(String tableName) throws SQLException {
        String tableSchema = "uuid VARCHAR(36) NOT NULL, items TEXT NOT NULL, PRIMARY KEY (uuid)";
        createTableIfNotExists(tableName, tableSchema);
    }

    public void createExpTableIfNotExists(String tableName) throws SQLException {
        String tableSchema = "uuid VARCHAR(36) NOT NULL, total_exp INT NOT NULL, level INT NOT NULL, exp_progress FLOAT NOT NULL, PRIMARY KEY (uuid)";
        createTableIfNotExists(tableName, tableSchema);
    }
}