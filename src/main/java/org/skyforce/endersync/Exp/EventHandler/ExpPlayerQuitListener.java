package org.skyforce.endersync.Exp.EventHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.skyforce.endersync.Exp.Managers.ExpManager;

import java.sql.SQLException;

public class ExpPlayerQuitListener implements Listener {
    private final ExpManager expManager;

    public ExpPlayerQuitListener(ExpManager expManager) {
        this.expManager = expManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        try {
            expManager.savePlayerExp(event.getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}