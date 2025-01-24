package org.skyforce.endersync;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.skyforce.endersync.Enderchest.EventHandler.EnderChestInventoryClickEventHandler;
import org.skyforce.endersync.Enderchest.EventHandler.EnderChestInventoryCloseEventHandler;
import org.skyforce.endersync.Enderchest.EventHandler.EnderChestInventoryOpenEventHandler;
import org.skyforce.endersync.Database.DatabaseManager;
import org.skyforce.endersync.Database.DatabaseTableManager;
import org.skyforce.endersync.Enderchest.Managers.EnderChestManager;
import org.skyforce.endersync.Inventory.EventHandler.InventoryPlayerJoinEventHandler;
import org.skyforce.endersync.Inventory.EventHandler.InventoryPlayerQuitEventHandler;
import org.skyforce.endersync.Inventory.Managers.InventoryManager;
import org.skyforce.endersync.Exp.EventHandler.ExpPlayerJoinListener;
import org.skyforce.endersync.Exp.EventHandler.ExpPlayerQuitListener;
import org.skyforce.endersync.Exp.Managers.ExpManager;

import java.sql.SQLException;

public final class Main extends JavaPlugin {
    private DatabaseManager databaseManager;
    private EnderChestManager enderChestManager;
    private InventoryManager inventoryManager;
    private DatabaseTableManager databaseTableManager;
    private ExpManager expManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        String host = getConfig().getString("database.host");
        String port = getConfig().getString("database.port");
        String dbName = getConfig().getString("database.databasename");
        String user = getConfig().getString("database.user");
        String password = getConfig().getString("database.password");
        String enderChestTable = getConfig().getString("database.enderchesttable");
        String inventoryTable = getConfig().getString("database.inventorytable");
        String expTable = getConfig().getString("database.exptable");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;

        databaseManager = new DatabaseManager(url, user, password);
        enderChestManager = new EnderChestManager(databaseManager, enderChestTable);
        inventoryManager = new InventoryManager(databaseManager, inventoryTable);
        databaseTableManager = new DatabaseTableManager(databaseManager);
        expManager = new ExpManager(databaseManager, expTable);

        try {
            databaseManager.connect();
            getLogger().info("\u001B[32m" + "Erfolgreich mit der Datenbank verbunden." + "\u001B[0m");
            databaseTableManager.createEnderChestTableIfNotExists(enderChestTable);
            databaseTableManager.createInventoryTableIfNotExists(inventoryTable);
            databaseTableManager.createExpTableIfNotExists(expTable);
        } catch (SQLException e) {
            getLogger().warning("\u001B[31m" + "Dieser Server ist nicht mit der Datenbank verbunden: " + e.getMessage() + "\u001B[0m");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Register inventory event handlers
        getServer().getPluginManager().registerEvents(new InventoryPlayerJoinEventHandler(inventoryManager), this);
        getServer().getPluginManager().registerEvents(new InventoryPlayerQuitEventHandler(inventoryManager), this);

        // Register EnderChest event handlers
        getServer().getPluginManager().registerEvents(new EnderChestInventoryClickEventHandler(enderChestManager), this);
        getServer().getPluginManager().registerEvents(new EnderChestInventoryCloseEventHandler(enderChestManager), this);
        getServer().getPluginManager().registerEvents(new EnderChestInventoryOpenEventHandler(enderChestManager), this);

        // Register EXP event handlers
        getServer().getPluginManager().registerEvents(new ExpPlayerJoinListener(expManager), this);
        getServer().getPluginManager().registerEvents(new ExpPlayerQuitListener(expManager), this);
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