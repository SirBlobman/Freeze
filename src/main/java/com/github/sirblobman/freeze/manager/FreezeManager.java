package com.github.sirblobman.freeze.manager;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.freeze.FreezePlugin;

public final class FreezeManager {
    private final FreezePlugin plugin;

    public FreezeManager(@NotNull FreezePlugin plugin) {
        this.plugin = plugin;
    }

    private @NotNull FreezePlugin getPlugin() {
        return this.plugin;
    }

    private @NotNull Logger getLogger() {
        FreezePlugin plugin = getPlugin();
        return plugin.getLogger();
    }

    private @NotNull PlayerDataManager getPlayerDataManager() {
        FreezePlugin plugin = getPlugin();
        return plugin.getPlayerDataManager();
    }

    public boolean isFrozen(@NotNull OfflinePlayer player) {
        PlayerDataManager playerDataManager = getPlayerDataManager();
        if (!playerDataManager.hasData(player)) {
            return false;
        }

        YamlConfiguration configuration = playerDataManager.get(player);
        return configuration.getBoolean("frozen", false);
    }

    public void setFrozen(@NotNull OfflinePlayer player, boolean frozen) {
        PlayerDataManager playerDataManager = getPlayerDataManager();
        if (!frozen && !playerDataManager.hasData(player)) {
            return;
        }

        YamlConfiguration configuration = playerDataManager.get(player);
        configuration.set("frozen", frozen);
        configuration.set("expire-time", null);
        playerDataManager.save(player);
    }

    public @Nullable Instant getExpireTime(@NotNull OfflinePlayer player) {
        PlayerDataManager playerDataManager = getPlayerDataManager();
        if (!playerDataManager.hasData(player)) {
            return null;
        }

        YamlConfiguration configuration = playerDataManager.get(player);
        String expireTimeString = configuration.getString("expire-time");
        if (expireTimeString == null) {
            return null;
        }

        try {
            return Instant.parse(expireTimeString);
        } catch (DateTimeParseException ex) {
            Logger logger = getLogger();
            logger.warning("Failed to parse freeze expire time '" + expireTimeString + "'.");
            return null;
        }
    }

    public void setExpireTime(@NotNull OfflinePlayer player, @NotNull Instant time) {
        PlayerDataManager playerDataManager = getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        String expireTimeString = time.toString();

        configuration.set("expire-time", expireTimeString);
        playerDataManager.save(player);
    }
}
