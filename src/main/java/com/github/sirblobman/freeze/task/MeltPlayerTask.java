package com.github.sirblobman.freeze.task;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.freeze.FreezeManager;
import com.github.sirblobman.freeze.FreezePlugin;

public final class MeltPlayerTask extends EntityTaskDetails<Player> {
    private final FreezePlugin plugin;
    private final Player player;

    public MeltPlayerTask(@NotNull FreezePlugin plugin, @NotNull Player player) {
        super(plugin, player);
        this.plugin = plugin;
        this.player = player;
        setDelay(1L);
    }

    public void register() {
        FreezePlugin plugin = getFreezePlugin();
        FoliaHelper foliaHelper = plugin.getFoliaHelper();
        TaskScheduler scheduler = foliaHelper.getScheduler();
        scheduler.scheduleEntityTask(this);
    }

    private @NotNull FreezePlugin getFreezePlugin() {
        return this.plugin;
    }

    private @NotNull Player getPlayer() {
        return this.player;
    }

    @Override
    public void run() {
        FreezePlugin plugin = getFreezePlugin();
        FreezeManager freezeManager = plugin.getFreezeManager();
        Player player = getPlayer();
        freezeManager.melt(player);
    }
}
