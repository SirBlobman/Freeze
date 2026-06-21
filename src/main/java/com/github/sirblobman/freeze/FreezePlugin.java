package com.github.sirblobman.freeze;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.freeze.menu.FakeMenu;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;

public final class FreezePlugin extends JavaPlugin {
    private final FreezeManager freezeManager;
    private final FreezeConfiguration configuration;
    private final FoliaHelper foliaHelper;
    private final MiniMessage miniMessage;

    public FreezePlugin() {
        this.freezeManager = new FreezeManager(this);
        this.configuration = new FreezeConfiguration(this);
        this.foliaHelper = new FoliaHelper(this);
        this.miniMessage = MiniMessage.miniMessage();
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
        saveResource("messages.yml", false);
        reloadConfiguration();
    }

    @Override
    public void onEnable() {
        registerCommands();
        registerListeners();
        registerTasks();
        register_bStats();
    }

    @Override
    public void onDisable() {
        Collection<? extends Player> playerCollection = Bukkit.getOnlinePlayers();
        for (Player player : playerCollection) {
            closeFakeMenu(player);
        }
    }

    public @NotNull FreezeManager getFreezeManager() {
        return this.freezeManager;
    }

    public @NotNull FreezeConfiguration getConfiguration() {
        return this.configuration;
    }

    public @NotNull FoliaHelper getFoliaHelper() {
        return this.foliaHelper;
    }

    public @NotNull MiniMessage getMiniMessage() {
        return this.miniMessage;
    }

    private void reloadConfiguration() {
        // TODO
    }

    private void registerCommands() {
        // TODO
    }

    private void registerListeners() {
        // TODO
    }

    private void registerTasks() {
        // TODO
    }

    private void register_bStats() {
        int pluginId = 16174; // https://bstats.org/plugin/bukkit/Freeze/16174
        new Metrics(this, pluginId);
    }

    private void closeFakeMenu(@NotNull Player player) {
        InventoryView view = player.getOpenInventory();
        Inventory topInventory = view.getTopInventory();
        InventoryHolder holder = topInventory.getHolder();
        if (holder instanceof FakeMenu) {
            player.closeInventory();
        }
    }
}
