package com.github.sirblobman.freeze.task;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.api.folia.details.TaskDetails;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.freeze.FreezeManager;
import com.github.sirblobman.freeze.FreezePlugin;

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

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(this::check);
    }

    public void register() {
        FreezePlugin plugin = getFreezePlugin();
        FoliaHelper foliaHelper = plugin.getFoliaHelper();
        TaskScheduler scheduler = foliaHelper.getScheduler();
        scheduler.scheduleAsyncTask(this);
    }

    private void check(@NotNull Player player) {
        FreezePlugin plugin = getFreezePlugin();
        FreezeManager freezeManager = plugin.getFreezeManager();

        long expireTime = freezeManager.getExpireTime(player);
        long systemTime = System.currentTimeMillis();
        if (systemTime >= expireTime) {
            MeltPlayerTask task = new MeltPlayerTask(plugin, player);
            task.register();
        }
    }
}
