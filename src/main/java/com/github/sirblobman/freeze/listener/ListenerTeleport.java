package com.github.sirblobman.freeze.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.configuration.FreezeConfiguration;
import com.github.sirblobman.freeze.manager.FreezeManager;

public final class ListenerTeleport extends FreezeListener {
    public ListenerTeleport(FreezePlugin plugin) {
        super(plugin);
    }

    @Override
    protected boolean isDisabled() {
        FreezeConfiguration configuration = getConfiguration();
        return !configuration.isPreventTeleport();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent e) {
        if (isDisabled()) {
            return;
        }

        TeleportCause teleportCause = e.getCause();
        if (teleportCause == TeleportCause.UNKNOWN || teleportCause == TeleportCause.PLUGIN) {
            return;
        }

        Player player = e.getPlayer();
        FreezeManager freezeManager = getFreezeManager();
        if (!freezeManager.isFrozen(player)) {
            return;
        }

        Location fromLocation = e.getFrom();
        Location toLocation = e.getTo();
        if (toLocation == null || isSimilar(fromLocation, toLocation)) {
            return;
        }

        e.setTo(fromLocation);
        sendFrozenMessage(player);
    }
}
