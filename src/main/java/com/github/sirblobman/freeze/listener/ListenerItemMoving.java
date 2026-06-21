package com.github.sirblobman.freeze.listener;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import com.github.sirblobman.freeze.FreezePlugin;

public final class ListenerItemMoving extends FreezeListener {
    public ListenerItemMoving(@NotNull FreezePlugin plugin) {
        super(plugin);
    }

    @Override
    protected boolean isDisabled() {
        return !getConfiguration().isPreventItemMovement();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (human instanceof Player player) {
            checkEvent(player, event);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryDrag(@NotNull InventoryDragEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (human instanceof Player player) {
            checkEvent(player, event);
        }
    }
}
