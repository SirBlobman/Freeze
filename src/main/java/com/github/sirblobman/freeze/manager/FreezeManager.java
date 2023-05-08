package com.github.sirblobman.freeze.manager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.event.PlayerFreezeEvent;
import com.github.sirblobman.freeze.event.PlayerMeltEvent;

public final class FreezeManager {
    private final FreezePlugin plugin;
    private final Set<UUID> frozenPlayerSet;

    public FreezeManager(@NotNull FreezePlugin plugin) {
        this.plugin = plugin;
        this.frozenPlayerSet = new HashSet<>();
    }

    public @NotNull FreezePlugin getPlugin() {
        return this.plugin;
    }

    public void setFrozen(@NotNull Player player, boolean freeze) {
        UUID playerId = player.getUniqueId();
        PluginManager pluginManager = Bukkit.getPluginManager();
        boolean wasFrozen = isFrozen(player);

        if (freeze) {
            this.frozenPlayerSet.add(playerId);
            if (!wasFrozen) {
                PlayerFreezeEvent freezeEvent = new PlayerFreezeEvent(player);
                pluginManager.callEvent(freezeEvent);
            }
        } else {
            this.frozenPlayerSet.remove(playerId);
            if (wasFrozen) {
                PlayerMeltEvent meltEvent = new PlayerMeltEvent(player);
                pluginManager.callEvent(meltEvent);
            }
        }
    }

    public boolean isFrozen(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        return this.frozenPlayerSet.contains(playerId);
    }

    public void removeAll() {
        this.frozenPlayerSet.clear();
    }
}
