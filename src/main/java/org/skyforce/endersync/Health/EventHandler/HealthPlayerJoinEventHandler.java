package org.skyforce.endersync.Health.EventHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.skyforce.endersync.Health.Managers.HealthManager;

import java.sql.SQLException;

public class HealthPlayerJoinEventHandler implements Listener {
    private final HealthManager healthManager;

    public HealthPlayerJoinEventHandler(HealthManager healthManager) {
        this.healthManager = healthManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            healthManager.loadHealth(event.getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}