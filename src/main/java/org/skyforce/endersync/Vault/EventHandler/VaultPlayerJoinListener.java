package org.skyforce.endersync.Vault.EventHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.skyforce.endersync.Vault.Managers.VaultManager;

import java.sql.SQLException;

public class VaultPlayerJoinListener implements Listener {
    private final VaultManager moneyManager;

    public VaultPlayerJoinListener(VaultManager moneyManager) {
        this.moneyManager = moneyManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            moneyManager.loadPlayerBalance(event.getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}