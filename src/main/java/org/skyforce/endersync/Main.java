package org.skyforce.endersync;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.skyforce.endersync.Enderchest.EventHandler.EnderChestInventoryClickEventHandler;
import org.skyforce.endersync.Enderchest.EventHandler.EnderChestInventoryCloseEventHandler;
import org.skyforce.endersync.Enderchest.EventHandler.EnderChestInventoryOpenEventHandler;
import org.skyforce.endersync.Database.DatabaseManager;
import org.skyforce.endersync.Database.DatabaseTableManager;
import org.skyforce.endersync.Enderchest.Managers.EnderChestManager;
import org.skyforce.endersync.Health.EventHandler.HealthPlayerJoinEventHandler;
import org.skyforce.endersync.Health.EventHandler.HealthPlayerQuitEventHandler;
import org.skyforce.endersync.Health.Managers.HealthManager;
import org.skyforce.endersync.Inventory.EventHandler.ArmorPlayerJoinEventHandler;
import org.skyforce.endersync.Inventory.EventHandler.ArmorPlayerQuitEventHandler;
import org.skyforce.endersync.Inventory.EventHandler.InventoryPlayerJoinEventHandler;
import org.skyforce.endersync.Inventory.EventHandler.InventoryPlayerQuitEventHandler;
import org.skyforce.endersync.Inventory.Managers.InventoryManager;
import org.skyforce.endersync.Inventory.Managers.ArmorManager;
import org.skyforce.endersync.Exp.EventHandler.ExpPlayerJoinListener;
import org.skyforce.endersync.Exp.EventHandler.ExpPlayerQuitListener;
import org.skyforce.endersync.Exp.Managers.ExpManager;
import org.skyforce.endersync.Vault.EventHandler.VaultPlayerJoinListener;
import org.skyforce.endersync.Vault.EventHandler.VaultPlayerQuitListener;
import org.skyforce.endersync.Vault.Managers.VaultManager;
import org.skyforce.endersync.Vault.Managers.VaultSetupManager;

import java.sql.SQLException;

public final class Main extends JavaPlugin {
    private DatabaseManager databaseManager;
    private EnderChestManager enderChestManager;
    private InventoryManager inventoryManager;
    private ArmorManager armorManager;
    private DatabaseTableManager databaseTableManager;
    private ExpManager expManager;
    private static Economy econ = null;
    private VaultManager vaultManager;
    private HealthManager healthManager;

    @Override
    public void onEnable() {
        if (!VaultSetupManager.setupEconomy(this)) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        String host = getConfig().getString("database.host");
        String port = getConfig().getString("database.port");
        String dbName = getConfig().getString("database.databasename");
        String user = getConfig().getString("database.user");
        String password = getConfig().getString("database.password");
        String enderChestTable = getConfig().getString("database.enderchesttable");
        String inventoryTable = getConfig().getString("database.inventorytable");
        String expTable = getConfig().getString("database.exptable");
        String vaultTable = getConfig().getString("database.vaulttable");
        String healthTable = getConfig().getString("database.healthtable");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;

        databaseManager = new DatabaseManager(url, user, password);
        enderChestManager = new EnderChestManager(databaseManager, enderChestTable);
        inventoryManager = new InventoryManager(databaseManager, inventoryTable);
        armorManager = new ArmorManager(databaseManager, inventoryTable);
        expManager = new ExpManager(databaseManager, expTable);
        databaseTableManager = new DatabaseTableManager(databaseManager);
        vaultManager = new VaultManager(databaseManager, VaultSetupManager.getEconomy(), vaultTable);
        healthManager = new HealthManager(databaseManager, healthTable);

        try {
            databaseManager.connect();
            getLogger().info("\u001B[32m" + "Erfolgreich mit der Datenbank verbunden." + "\u001B[0m");
            databaseTableManager.createEnderChestTableIfNotExists(enderChestTable);
            databaseTableManager.createInventoryTableIfNotExists(inventoryTable);
            databaseTableManager.createExpTableIfNotExists(expTable);
            databaseTableManager.createVaultTableIfNotExists(vaultTable);
            databaseTableManager.createHealthTableIfNotExists(healthTable);
        } catch (SQLException e) {
            getLogger().warning("\u001B[31m" + "Dieser Server ist nicht mit der Datenbank verbunden: " + e.getMessage() + "\u001B[0m");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Register Vault event handlers
        getServer().getPluginManager().registerEvents(new VaultPlayerJoinListener(vaultManager), this);
        getServer().getPluginManager().registerEvents(new VaultPlayerQuitListener(vaultManager), this);

        // Register inventory event handlers
        getServer().getPluginManager().registerEvents(new InventoryPlayerJoinEventHandler(inventoryManager), this);
        getServer().getPluginManager().registerEvents(new InventoryPlayerQuitEventHandler(inventoryManager), this);
        getServer().getPluginManager().registerEvents(new ArmorPlayerJoinEventHandler(armorManager), this);
        getServer().getPluginManager().registerEvents(new ArmorPlayerQuitEventHandler(armorManager), this);

        // Register EnderChest event handlers
        getServer().getPluginManager().registerEvents(new EnderChestInventoryClickEventHandler(enderChestManager), this);
        getServer().getPluginManager().registerEvents(new EnderChestInventoryCloseEventHandler(enderChestManager), this);
        getServer().getPluginManager().registerEvents(new EnderChestInventoryOpenEventHandler(enderChestManager), this);

        // Register EXP event handlers
        getServer().getPluginManager().registerEvents(new ExpPlayerJoinListener(expManager), this);
        getServer().getPluginManager().registerEvents(new ExpPlayerQuitListener(expManager), this);

        //Register Health and Food event handlers
        getServer().getPluginManager().registerEvents(new HealthPlayerJoinEventHandler(healthManager), this);
        getServer().getPluginManager().registerEvents(new HealthPlayerQuitEventHandler(healthManager), this);
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