package org.skyforce.endersync.Vault.Managers;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultSetupManager {
    private static Economy econ = null;

    public static boolean setupEconomy(JavaPlugin plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().severe("Vault plugin not found! Disabling plugin.");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().severe("No economy provider found! Disabling plugin.");
            return false;
        }
        econ = rsp.getProvider();
        plugin.getLogger().info("Vault hooked successfully!");
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
}