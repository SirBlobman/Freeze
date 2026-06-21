package com.github.sirblobman.freeze.listener;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Listener;

import com.github.sirblobman.freeze.FreezeManager;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.configuration.FreezeConfiguration;
import com.github.sirblobman.freeze.configuration.MessageConfiguration;

public abstract class FreezeListener implements Listener {
    private final FreezePlugin plugin;

    public FreezeListener(@NotNull FreezePlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        FreezePlugin plugin = getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    protected final @NotNull FreezePlugin getPlugin() {
        return this.plugin;
    }

    protected final @NotNull FreezeManager getFreezeManager() {
        return getPlugin().getFreezeManager();
    }

    protected final @NotNull FreezeConfiguration getConfiguration() {
        return getPlugin().getConfiguration();
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

    protected final void checkEvent(@NotNull Player player, @NotNull Cancellable event) {
        boolean disabled = isDisabled();
        checkEvent(player, event, disabled);
    }

    protected final void checkEvent(@NotNull Player player, @NotNull Cancellable event, boolean disabled) {
        FreezeManager freezeManager = getFreezeManager();
        if (!freezeManager.isFrozen(player)) {
            return;
        }

        event.setCancelled(true);
        sendFrozenMessage(player);
    }

    protected final void sendFrozenMessage(@NotNull Player player) {
        MessageConfiguration messages = getPlugin().getMessages();
        messages.sendActionBar(player, "action-bar.frozen");
    }

    protected abstract boolean isDisabled();
}
