package com.github.sirblobman.freeze.listener;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.github.sirblobman.freeze.FreezeManager;
import com.github.sirblobman.freeze.FreezePlugin;

public final class ListenerTeleport extends FreezeListener {
    public ListenerTeleport(@NotNull FreezePlugin plugin) {
        super(plugin);
    }

    @Override
    protected boolean isDisabled() {
        return !getConfiguration().isPreventTeleport();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onTeleport(@NotNull PlayerTeleportEvent event) {
        if (isDisabled()) {
            return;
        }

        PlayerTeleportEvent.TeleportCause cause = event.getCause();
        if (cause == PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            return;
        }

        if (cause == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            return;
        }

        Player player = event.getPlayer();
        FreezeManager freezeManager = getFreezeManager();
        if(!freezeManager.isFrozen(player)) {
            return;
        }

        Location locationFrom = event.getFrom();
        Location locationTo = event.getTo();
        if (isSimilar(locationFrom, locationTo)) {
            return;
        }

        event.setTo(locationFrom);
        sendFrozenMessage(player);
    }
}
