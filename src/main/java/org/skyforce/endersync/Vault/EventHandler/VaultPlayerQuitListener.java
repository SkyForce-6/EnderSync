package org.skyforce.endersync.Vault.EventHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.skyforce.endersync.Vault.Managers.VaultManager;

import java.sql.SQLException;

public class VaultPlayerQuitListener implements Listener {
    private final VaultManager moneyManager;

    public VaultPlayerQuitListener(VaultManager moneyManager) {
        this.moneyManager = moneyManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        try {
            moneyManager.savePlayerBalance(event.getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}