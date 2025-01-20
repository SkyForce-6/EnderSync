package org.skyforce.endersync;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.skyforce.endersync.EventHandler.InventoryClickEventHandler;
import org.skyforce.endersync.EventHandler.InventoryCloseEventHandler;
import org.skyforce.endersync.EventHandler.PlayerJoinEventHandler;
import org.skyforce.endersync.Managers.DatabaseManager;
import org.skyforce.endersync.Managers.DatabaseTableManager;
import org.skyforce.endersync.Managers.EnderChestManager;

import java.sql.SQLException;

public final class Main extends JavaPlugin {
    private DatabaseManager databaseManager;
    private EnderChestManager enderChestManager;
    private DatabaseTableManager databaseTableManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        String url = getConfig().getString("database.url");
        String user = getConfig().getString("database.user");
        String password = getConfig().getString("database.password");

        databaseManager = new DatabaseManager(url, user, password);
        enderChestManager = new EnderChestManager(databaseManager);
        databaseTableManager = new DatabaseTableManager(databaseManager);

        try {
            databaseManager.connect();
            getLogger().info("Successfully connected to the database.");
            databaseTableManager.createTableIfNotExists();
        } catch (SQLException e) {
            getLogger().severe("This server is not connected to the database: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerJoinEventHandler(enderChestManager), this);
        getServer().getPluginManager().registerEvents(new InventoryClickEventHandler(enderChestManager), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseEventHandler(enderChestManager), this);
    }

    @Override
    public void onDisable() {
        try {
            databaseManager.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}