package com.github.sirblobman.freeze.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import org.jetbrains.annotations.NotNull;

/**
 * This event is fired when a player is frozen.
 * @author SirBlobman
 */
public final class PlayerFreezeEvent extends PlayerEvent {
    private static final HandlerList handlerList;
    
    static {
        handlerList = new HandlerList();
    }
    
    public static HandlerList getHandlerList() {
        return handlerList;
    }
    
    public PlayerFreezeEvent(Player player) {
        super(player);
    }
    
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
