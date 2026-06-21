package com.github.sirblobman.freeze.listener;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.github.sirblobman.freeze.FreezePlugin;

public final class ListenerDamage extends FreezeListener {
    public ListenerDamage(@NotNull FreezePlugin plugin) {
        super(plugin);
    }

    @Override
    protected boolean isDisabled() {
        return !getConfiguration().isPreventDamage();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDamage(@NotNull EntityDamageByEntityEvent event) {
        Entity damaged = event.getEntity();
        if (damaged instanceof Player player) {
            checkEvent(player, event);
        }

        boolean preventAttacking = getConfiguration().isPreventDamage();
        Entity damager = event.getDamager();
        if(damager instanceof Player player) {
            checkEvent(player, event, !preventAttacking);
        }
    }
}
