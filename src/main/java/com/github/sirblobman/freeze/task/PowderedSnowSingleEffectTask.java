package com.github.sirblobman.freeze.task;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.freeze.FreezePlugin;

public final class PowderedSnowSingleEffectTask extends EntityTaskDetails<Player> {
    private final FreezePlugin plugin;

    public PowderedSnowSingleEffectTask(@NotNull FreezePlugin plugin, @NotNull Player player) {
        super(plugin, player);
        this.plugin = plugin;

        setPeriod(1L);
        setDelay(1L);
    }

    @Override
    public void run() {
        if (isDisabled()) {
            cancel();
            return;
        }

        Player player = getEntity();
        if (player != null) {
            int maxFreezeTicks = player.getMaxFreezeTicks();
            player.setFreezeTicks(maxFreezeTicks - 1);
        }
    }

    private @NotNull FreezePlugin getFreezePlugin() {
        return this.plugin;
    }

    private boolean isDisabled() {
        return !getFreezePlugin().getConfiguration().isUsePowderedSnowEffect();
    }
}
