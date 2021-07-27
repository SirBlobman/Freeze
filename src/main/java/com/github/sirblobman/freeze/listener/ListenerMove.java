package com.github.sirblobman.freeze.listener;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public final class ListenerMove extends FreezeListener {
    public ListenerMove(FreezePlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public void onMove(PlayerMoveEvent e) {
        if(isDisabled()) return;

        Player player = e.getPlayer();
        FreezeManager freezeManager = getFreezeManager();
        if(!freezeManager.isFrozen(player)) return;

        Location fromLocation = e.getFrom();
        Location toLocation = e.getTo();
        if(toLocation == null || isSimilar(fromLocation, toLocation)) return;

        e.setCancelled(true);
        sendFrozenMessage(player);
    }

    private boolean isDisabled() {
        FreezePlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        return !configuration.getBoolean("prevent-movement", true);
    }

    private boolean isSimilar(Location location1, Location location2) {
        double x1 = location1.getX(), y1 = location1.getY(), z1 = location1.getZ();
        double x2 = location2.getX(), y2 = location2.getY(), z2 = location2.getZ();
        return (x1 == x2 && z1 == z2 && y2 <= y1);
    }
}
