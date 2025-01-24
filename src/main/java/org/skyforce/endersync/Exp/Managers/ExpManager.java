package org.skyforce.endersync.Exp.Managers;

import org.bukkit.entity.Player;
import org.skyforce.endersync.Database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ExpManager {
    private final DatabaseManager databaseManager;
    private final String tableName;
    private final Logger logger;

    public ExpManager(DatabaseManager databaseManager, String tableName, Logger logger) {
        this.databaseManager = databaseManager;
        this.tableName = tableName;
        this.logger = logger;
    }

    public void savePlayerExp(Player player) throws SQLException {
        String sql = "REPLACE INTO " + tableName + " (uuid, total_exp, level, exp_progress) VALUES (?, ?, ?, ?)";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setInt(2, player.getTotalExperience());
            statement.setInt(3, player.getLevel());
            statement.setFloat(4, player.getExp());
            statement.executeUpdate();
            logger.info("Saved EXP for player " + player.getName() + ": " + player.getTotalExperience() + ", Level: " + player.getLevel() + ", Exp Progress: " + player.getExp());
        } catch (SQLException e) {
            logger.severe("Failed to save EXP for player " + player.getName() + ": " + e.getMessage());
            throw e;
        }
    }

    public void loadPlayerExp(Player player) throws SQLException {
        String sql = "SELECT total_exp, level, exp_progress FROM " + tableName + " WHERE uuid = ?";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int totalExp = resultSet.getInt("total_exp");
                    int level = resultSet.getInt("level");
                    float expProgress = resultSet.getFloat("exp_progress");

                    player.setTotalExperience(totalExp);
                    player.setLevel(level);
                    player.setExp(expProgress);

                    logger.info("Loaded EXP for player " + player.getName() + ": " + totalExp + ", Level: " + level + ", Exp Progress: " + expProgress);
                } else {
                    logger.warning("No EXP found for player " + player.getName());
                }
            }
        } catch (SQLException e) {
            logger.severe("Failed to load EXP for player " + player.getName() + ": " + e.getMessage());
            throw e;
        }
    }
}