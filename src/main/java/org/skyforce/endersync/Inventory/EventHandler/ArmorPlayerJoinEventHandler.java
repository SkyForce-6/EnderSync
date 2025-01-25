package org.skyforce.endersync.Inventory.EventHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.skyforce.endersync.Inventory.Managers.ArmorManager;

import java.sql.SQLException;

public class ArmorPlayerJoinEventHandler implements Listener {
    private final ArmorManager armorManager;

    public ArmorPlayerJoinEventHandler(ArmorManager armorManager) {
        this.armorManager = armorManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            armorManager.loadArmor(event.getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}