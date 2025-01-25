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
            System.out.println("\u001B[36m" + "Table " + tableName + " created or already exists." + "\u001B[0m");
        }
    }
    public void createEnderChestTableIfNotExists(String tableName) throws SQLException {
        String tableSchema = "player_uuid VARCHAR(36) NOT NULL, items TEXT NOT NULL, PRIMARY KEY (player_uuid)";
        createTableIfNotExists(tableName, tableSchema);
    }
    public void createInventoryTableIfNotExists(String tableName) throws SQLException {
        String tableSchema = "player_uuid VARCHAR(36) NOT NULL, inventory TEXT NOT NULL, armor TEXT, offhand TEXT, PRIMARY KEY (player_uuid)";
        createTableIfNotExists(tableName, tableSchema);
    }
    public void createExpTableIfNotExists(String tableName) throws SQLException {
        String tableSchema = "player_uuid VARCHAR(36) NOT NULL, total_exp INT NOT NULL, exp_lvl INT NOT NULL, exp FLOAT NOT NULL, PRIMARY KEY (player_uuid)";
        createTableIfNotExists(tableName, tableSchema);
    }
    public void createVaultTableIfNotExists(String tableName) throws SQLException {
        String tableSchema = "player_uuid VARCHAR(36) NOT NULL, money DOUBLE NOT NULL, PRIMARY KEY (player_uuid)";
        createTableIfNotExists(tableName, tableSchema);
    }
    public void createHealthTableIfNotExists(String tableName) throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + "player_uuid VARCHAR(36) PRIMARY KEY, " + "health DOUBLE, " + "foodLevel INT)";
        try (Statement statement = databaseManager.getConnection().createStatement()) {
            statement.executeUpdate(query);
            System.out.println("\u001B[36m" + "Table " + tableName + " created or already exists." + "\u001B[0m");
        }
    }
}