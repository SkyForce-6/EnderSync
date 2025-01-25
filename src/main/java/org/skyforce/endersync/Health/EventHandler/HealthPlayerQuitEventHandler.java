package org.skyforce.endersync.Health.EventHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.skyforce.endersync.Health.Managers.HealthManager;

import java.sql.SQLException;

public class HealthPlayerQuitEventHandler implements Listener {
    private final HealthManager healthManager;

    public HealthPlayerQuitEventHandler(HealthManager healthManager) {
        this.healthManager = healthManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        try {
            healthManager.saveHealth(event.getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}