package com.github.sirblobman.freeze;

import org.jetbrains.annotations.NotNull;

import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import io.papermc.paper.persistence.PersistentDataContainerView;

import com.github.sirblobman.freeze.event.PlayerFreezeEvent;
import com.github.sirblobman.freeze.event.PlayerMeltEvent;

public final class FreezeManager {
    private final FreezePlugin plugin;
    private final NamespacedKey keyFrozen;

    public FreezeManager(@NotNull FreezePlugin plugin) {
        this.plugin = plugin;
        this.keyFrozen = new NamespacedKey(plugin, "freeze");
    }

    private @NotNull FreezePlugin getPlugin() {
        return this.plugin;
    }

    public boolean isFrozen(@NotNull OfflinePlayer player) {
        PersistentDataContainerView dataView = player.getPersistentDataContainer();
        if (!dataView.has(this.keyFrozen)) {
            return false;
        }

        Long freezeExpireTime = dataView.get(this.keyFrozen, PersistentDataType.LONG);
        if (freezeExpireTime == null) {
            return false;
        }

        long systemTime = System.currentTimeMillis();
        return systemTime < freezeExpireTime;
    }

    public long getExpireTime(@NotNull OfflinePlayer player) {
        PersistentDataContainerView dataView = player.getPersistentDataContainer();
        if (!dataView.has(this.keyFrozen)) {
            return -1;
        }

        Long freezeExpireTime = dataView.get(this.keyFrozen, PersistentDataType.LONG);
        return (freezeExpireTime == null ? -1 : freezeExpireTime);
    }

    public void freeze(@NotNull Player player, long expireTimeMillis) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        dataContainer.set(this.keyFrozen, PersistentDataType.LONG, expireTimeMillis);

        PlayerFreezeEvent event = new PlayerFreezeEvent(player);
        getPlugin().getServer().getPluginManager().callEvent(event);
    }

    public void melt(@NotNull Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        dataContainer.remove(this.keyFrozen);

        PlayerMeltEvent event = new PlayerMeltEvent(player);
        getPlugin().getServer().getPluginManager().callEvent(event);
    }
}
