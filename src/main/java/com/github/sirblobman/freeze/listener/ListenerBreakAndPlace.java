package com.github.sirblobman.freeze.listener;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.github.sirblobman.freeze.FreezePlugin;

public final class ListenerBreakAndPlace extends FreezeListener {
    public ListenerBreakAndPlace(FreezePlugin plugin) {
        super(plugin);
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        checkEvent(player, e);
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        checkEvent(player, e);
    }
    
    @Override
    protected boolean isDisabled() {
        YamlConfiguration configuration = getConfiguration();
        return !configuration.getBoolean("prevent-block-break-and-place", true);
    }
}
