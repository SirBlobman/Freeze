package com.github.sirblobman.freeze.listener;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.github.sirblobman.freeze.FreezePlugin;

public final class ListenerDamage extends FreezeListener {
    public ListenerDamage(FreezePlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent e) {
        Entity damaged = e.getEntity();
        if (damaged instanceof Player) {
            Player player = (Player) damaged;
            checkEvent(player, e);
        }

        Entity damager = e.getDamager();
        if (damager instanceof Player) {
            Player player = (Player) damager;
            checkEvent(player, e, isDisabled2());
        }
    }

    @Override
    protected boolean isDisabled() {
        YamlConfiguration configuration = getConfiguration();
        return !configuration.getBoolean("prevent-damage", true);
    }

    private boolean isDisabled2() {
        YamlConfiguration configuration = getConfiguration();
        return !configuration.getBoolean("prevent-attacking", true);
    }
}
