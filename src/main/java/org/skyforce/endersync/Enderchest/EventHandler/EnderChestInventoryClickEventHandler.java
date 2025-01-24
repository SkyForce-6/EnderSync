package org.skyforce.endersync.Enderchest.EventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.skyforce.endersync.Enderchest.Managers.EnderChestManager;

import java.sql.SQLException;

public class EnderChestInventoryClickEventHandler implements Listener {
    private final EnderChestManager enderChestManager;

    public EnderChestInventoryClickEventHandler(EnderChestManager enderChestManager) {
        this.enderChestManager = enderChestManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
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