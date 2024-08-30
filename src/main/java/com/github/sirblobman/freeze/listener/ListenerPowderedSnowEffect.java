package com.github.sirblobman.freeze.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.event.PlayerFreezeEvent;
import com.github.sirblobman.freeze.event.PlayerMeltEvent;
import com.github.sirblobman.freeze.task.PowderedSnowSingleEffectTask;

public final class ListenerPowderedSnowEffect extends FreezeListener {
    private final Map<UUID, PowderedSnowSingleEffectTask> taskMap;

    public ListenerPowderedSnowEffect(@NotNull FreezePlugin plugin) {
        super(plugin);
        this.taskMap = new HashMap<>();
    }

    @Override
    protected boolean isDisabled() {
        return !getPlugin().getConfiguration().isUsePowderedSnowEffect();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFreeze(PlayerFreezeEvent e) {
        if (isDisabled()) {
            return;
        }

        Player player = e.getPlayer();
        TaskScheduler scheduler = getPlugin().getFoliaHelper().getScheduler();
        PowderedSnowSingleEffectTask task = new PowderedSnowSingleEffectTask(getPlugin(), player);

        UUID playerId = player.getUniqueId();
        this.taskMap.put(playerId, task);
        scheduler.scheduleEntityTask(task);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMelt(PlayerMeltEvent e) {
        if (isDisabled()) {
            return;
        }

        Player player = e.getPlayer();
        UUID playerId = player.getUniqueId();
        PowderedSnowSingleEffectTask task = this.taskMap.get(playerId);
        if (task != null) {
            task.cancel();
            this.taskMap.remove(playerId);
        }
    }
}
