package com.github.sirblobman.freeze.task;

import java.time.Instant;
import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.api.folia.details.TaskDetails;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public final class MeltTask extends TaskDetails {
    private final FreezePlugin plugin;

    public MeltTask(@NotNull FreezePlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        setDelay(20L);
        setPeriod(20L);
    }

    private @NotNull FreezePlugin getFreezePlugin() {
        return this.plugin;
    }

    public void register() {
        FreezePlugin plugin = getFreezePlugin();
        FoliaHelper foliaHelper = plugin.getFoliaHelper();
        TaskScheduler scheduler = foliaHelper.getScheduler();
        scheduler.scheduleAsyncTask(this);
    }

    @Override
    public void run() {
        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayerCollection) {
            checkMelt(player);
        }
    }

    private void checkMelt(@NotNull Player player) {
        FreezePlugin plugin = getFreezePlugin();
        FreezeManager freezeManager = plugin.getFreezeManager();
        if (!freezeManager.isFrozen(player)) {
            return;
        }

        Instant expireTime = freezeManager.getExpireTime(player);
        if (expireTime == null) {
            return;
        }

        if (expireTime.isBefore(Instant.now())) {
            UnfreezePlayerTask task = new UnfreezePlayerTask(plugin, player);
            getFreezePlugin().getFoliaHelper().getScheduler().scheduleEntityTask(task);
        }
    }
}
