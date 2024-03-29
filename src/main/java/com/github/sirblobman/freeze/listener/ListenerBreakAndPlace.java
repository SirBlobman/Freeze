package com.github.sirblobman.freeze.listener;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.configuration.FreezeConfiguration;

public final class ListenerBreakAndPlace extends FreezeListener {
    public ListenerBreakAndPlace(@NotNull FreezePlugin plugin) {
        super(plugin);
    }

    @Override
    protected boolean isDisabled() {
        FreezeConfiguration configuration = getConfiguration();
        return !configuration.isPreventBlockActions();
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
}
