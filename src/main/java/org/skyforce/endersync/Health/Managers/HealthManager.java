package org.skyforce.endersync.Health.Managers;

import org.bukkit.entity.Player;
import org.skyforce.endersync.Database.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HealthManager {
    private final DatabaseManager databaseManager;
    private final String tableName;

    public HealthManager(DatabaseManager databaseManager, String tableName) {
        this.databaseManager = databaseManager;
        this.tableName = tableName;
    }

    public void saveHealth(Player player) throws SQLException {
        String query = "REPLACE INTO " + tableName + " (player_uuid, health, foodLevel) VALUES (?, ?, ?)";
        try (PreparedStatement statement = databaseManager.getConnection().prepareStatement(query)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setDouble(2, player.getHealth());
            statement.setInt(3, player.getFoodLevel());
            statement.executeUpdate();
        }
    }

    public void loadHealth(Player player) throws SQLException {
        String query = "SELECT health, foodLevel FROM " + tableName + " WHERE player_uuid = ?";
        try (PreparedStatement statement = databaseManager.getConnection().prepareStatement(query)) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    player.setHealth(resultSet.getDouble("health"));
                    player.setFoodLevel(resultSet.getInt("foodLevel"));
                }
            }
        }
    }
}