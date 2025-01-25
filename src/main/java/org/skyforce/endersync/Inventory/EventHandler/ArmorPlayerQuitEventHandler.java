package org.skyforce.endersync.Inventory.EventHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.skyforce.endersync.Inventory.Managers.ArmorManager;

import java.sql.SQLException;

public class ArmorPlayerQuitEventHandler implements Listener {
    private final ArmorManager armorManager;

    public ArmorPlayerQuitEventHandler(ArmorManager armorManager) {
        this.armorManager = armorManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        try {
            armorManager.saveArmor(event.getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}