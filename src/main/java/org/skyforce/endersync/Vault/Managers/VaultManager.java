package org.skyforce.endersync.Vault.Managers;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.skyforce.endersync.Database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VaultManager {
    private final DatabaseManager databaseManager;
    private final Economy economy;
    private final String tableName;

    public VaultManager(DatabaseManager databaseManager, Economy economy, String tableName) {
        this.databaseManager = databaseManager;
        this.economy = economy;
        this.tableName = tableName;
    }

    public void savePlayerBalance(Player player) throws SQLException {
        String sql = "REPLACE INTO " + tableName + " (player_uuid, money) VALUES (?, ?)";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setDouble(2, economy.getBalance(player));
            statement.executeUpdate();
        }
    }

    public void loadPlayerBalance(Player player) throws SQLException {
        String sql = "SELECT money FROM " + tableName + " WHERE player_uuid = ?";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double balance = resultSet.getDouble("money");
                    economy.withdrawPlayer(player, economy.getBalance(player));
                    economy.depositPlayer(player, balance);
                }
            }
        }
    }
}