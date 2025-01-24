package org.skyforce.endersync.Enderchest.EventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.skyforce.endersync.Enderchest.Managers.EnderChestManager;

import java.sql.SQLException;

public class EnderChestInventoryOpenEventHandler implements Listener {
    private final EnderChestManager enderChestManager;

    public EnderChestInventoryOpenEventHandler(EnderChestManager enderChestManager) {
        this.enderChestManager = enderChestManager;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            Player player = (Player) event.getPlayer();
            try {
                enderChestManager.loadEnderChest(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}