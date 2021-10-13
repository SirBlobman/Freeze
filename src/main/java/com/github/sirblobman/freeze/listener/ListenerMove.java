package com.github.sirblobman.freeze.listener;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public final class ListenerMove extends FreezeListener {
    public ListenerMove(FreezePlugin plugin) {
        super(plugin);
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent e) {
        if(isDisabled()) {
            return;
        }
        
        Player player = e.getPlayer();
        FreezeManager freezeManager = getFreezeManager();
        if(!freezeManager.isFrozen(player)) {
            return;
        }
        
        Location fromLocation = e.getFrom();
        Location toLocation = e.getTo();
        if(toLocation == null || isSimilar(fromLocation, toLocation)) {
            return;
        }
        
        e.setTo(fromLocation);
        sendFrozenMessage(player);
    }
    
    @Override
    protected boolean isDisabled() {
        YamlConfiguration configuration = getConfiguration();
        return !configuration.getBoolean("prevent-movement", true);
    }
}
