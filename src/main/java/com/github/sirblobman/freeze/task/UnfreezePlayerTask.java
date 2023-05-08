package com.github.sirblobman.freeze.task;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public final class UnfreezePlayerTask extends EntityTaskDetails<Player> {
    private final FreezePlugin plugin;

    public UnfreezePlayerTask(@NotNull FreezePlugin plugin, @NotNull Player player) {
        super(plugin, player);
        this.plugin = plugin;
        setDelay(1L);
    }

    private @NotNull FreezePlugin getFreezePlugin() {
        return this.plugin;
    }

    private @NotNull FreezeManager getFreezeManager() {
        FreezePlugin plugin = getFreezePlugin();
        return plugin.getFreezeManager();
    }

    @Override
    public void run() {
        Player player = getEntity();
        if (player == null) {
            return;
        }

        FreezeManager freezeManager = getFreezeManager();
        freezeManager.setFrozen(player, false);
    }
}
