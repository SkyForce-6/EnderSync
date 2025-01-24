package org.skyforce.endersync.Inventory.EventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.skyforce.endersync.Inventory.Managers.InventoryManager;

import java.sql.SQLException;

public class InventoryPlayerQuitEventHandler implements Listener {
    private final InventoryManager inventoryManager;

    public InventoryPlayerQuitEventHandler(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        try {
            inventoryManager.saveInventory(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}