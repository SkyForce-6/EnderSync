package org.skyforce.endersync.Exp.EventHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.skyforce.endersync.Exp.Managers.ExpManager;

import java.sql.SQLException;

public class ExpPlayerJoinListener implements Listener {
    private final ExpManager expManager;

    public ExpPlayerJoinListener(ExpManager expManager) {
        this.expManager = expManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            expManager.loadPlayerExp(event.getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}