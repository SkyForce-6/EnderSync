package org.skyforce.endersync.Exp.Managers;

import org.bukkit.entity.Player;
import org.skyforce.endersync.Database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpManager {
    private final DatabaseManager databaseManager;
    private final String tableName;

    public ExpManager(DatabaseManager databaseManager, String tableName) {
        this.databaseManager = databaseManager;
        this.tableName = tableName;
    }

    public void savePlayerExp(Player player) throws SQLException {
        String sql = "REPLACE INTO " + tableName + " (uuid, total_exp, exp_lvl, exp) VALUES (?, ?, ?, ?)";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setInt(2, player.getTotalExperience());
            statement.setInt(3, player.getLevel());
            statement.setFloat(4, player.getExp());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void loadPlayerExp(Player player) throws SQLException {
        String sql = "SELECT total_exp, exp_lvl, exp FROM " + tableName + " WHERE uuid = ?";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int totalExp = resultSet.getInt("total_exp");
                    int level = resultSet.getInt("exp_lvl");
                 //   int exptolevel = resultSet.getInt("exp_to_level");
                    float expProgress = resultSet.getFloat("exp");

                    player.setTotalExperience(totalExp);
                    player.setLevel(level);
                  //  player.setexptolevel(exptolevel);
                    player.setExp(expProgress);
                }
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}