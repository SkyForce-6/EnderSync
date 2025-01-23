package org.skyforce.endersync.EventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.skyforce.endersync.Managers.EnderChestManager;

import java.sql.SQLException;

public class PlayerJoinEventHandler implements Listener {
    private final EnderChestManager enderChestManager;

    public PlayerJoinEventHandler(EnderChestManager enderChestManager) {
        this.enderChestManager = enderChestManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            enderChestManager.loadEnderChest(player);
       } catch (SQLException e) {
           e.printStackTrace();
        }
    }
}