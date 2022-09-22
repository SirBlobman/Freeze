package com.github.sirblobman.freeze.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import org.jetbrains.annotations.NotNull;

/**
 * This event is fired when a player is no longer frozen.
 *
 * @author SirBlobman
 */
public final class PlayerMeltEvent extends PlayerEvent {
    private static final HandlerList handlerList;

    static {
        handlerList = new HandlerList();
    }

    public PlayerMeltEvent(Player player) {
        super(player);
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
