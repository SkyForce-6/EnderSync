package org.skyforce.endersync.Inventory.EventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.skyforce.endersync.Inventory.Managers.InventoryManager;

import java.sql.SQLException;

public class InventoryPlayerJoinEventHandler implements Listener {
    private final InventoryManager inventoryManager;

    public InventoryPlayerJoinEventHandler(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            inventoryManager.loadInventory(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}