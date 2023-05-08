package com.github.sirblobman.freeze.listener;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.plugin.listener.PluginListener;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.configuration.FreezeConfiguration;
import com.github.sirblobman.freeze.manager.FreezeManager;

public abstract class FreezeListener extends PluginListener<FreezePlugin> {
    public FreezeListener(@NotNull FreezePlugin plugin) {
        super(plugin);
    }

    protected final @NotNull FreezeManager getFreezeManager() {
        FreezePlugin plugin = getPlugin();
        return plugin.getFreezeManager();
    }

    protected final @NotNull LanguageManager getLanguageManager() {
        FreezePlugin plugin = getPlugin();
        return plugin.getLanguageManager();
    }

    protected final @NotNull FreezeConfiguration getConfiguration() {
        FreezePlugin plugin = getPlugin();
        return plugin.getConfiguration();
    }

    protected final void sendFrozenMessage(@NotNull Player player) {
        LanguageManager languageManager = getLanguageManager();
        languageManager.sendActionBar(player, "action-bar.frozen");
    }

    protected final boolean isSimilar(@NotNull Location location1, @NotNull Location location2) {
        double x1 = location1.getX();
        double y1 = location1.getY();
        double z1 = location1.getZ();
        double x2 = location2.getX();
        double y2 = location2.getY();
        double z2 = location2.getZ();
        return (x1 == x2 && z1 == z2 && y2 <= y1);
    }

    protected final void checkEvent(@NotNull Player player, @NotNull Cancellable e) {
        boolean disabled = isDisabled();
        checkEvent(player, e, disabled);
    }

    protected final void checkEvent(@NotNull Player player, @NotNull Cancellable e, boolean disabled) {
        if (disabled) {
            return;
        }

        FreezeManager freezeManager = getFreezeManager();
        if (!freezeManager.isFrozen(player)) {
            return;
        }

        e.setCancelled(true);
        sendFrozenMessage(player);
    }

    protected abstract boolean isDisabled();
}
