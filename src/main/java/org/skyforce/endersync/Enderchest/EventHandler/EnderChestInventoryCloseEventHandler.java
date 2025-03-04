package org.skyforce.endersync.Enderchest.EventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.skyforce.endersync.Enderchest.Managers.EnderChestManager;

import java.sql.SQLException;

public class EnderChestInventoryCloseEventHandler implements Listener {
    private final EnderChestManager enderChestManager;

    public EnderChestInventoryCloseEventHandler(EnderChestManager enderChestManager) {
        this.enderChestManager = enderChestManager;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (event.getInventory().equals(player.getEnderChest())) {
                try {
                    enderChestManager.saveEnderChest(player);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}