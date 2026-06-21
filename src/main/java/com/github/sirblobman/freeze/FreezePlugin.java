package com.github.sirblobman.freeze;

import java.io.File;
import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.freeze.command.CommandFreeze;
import com.github.sirblobman.freeze.command.CommandMelt;
import com.github.sirblobman.freeze.configuration.FreezeConfiguration;
import com.github.sirblobman.freeze.configuration.MessageConfiguration;
import com.github.sirblobman.freeze.listener.ListenerBreakAndPlace;
import com.github.sirblobman.freeze.listener.ListenerCommand;
import com.github.sirblobman.freeze.listener.ListenerDamage;
import com.github.sirblobman.freeze.listener.ListenerFakeMenu;
import com.github.sirblobman.freeze.listener.ListenerItemDropping;
import com.github.sirblobman.freeze.listener.ListenerItemMoving;
import com.github.sirblobman.freeze.listener.ListenerPlayerMove;
import com.github.sirblobman.freeze.listener.ListenerPowderedSnowEffect;
import com.github.sirblobman.freeze.listener.ListenerTeleport;
import com.github.sirblobman.freeze.menu.FakeMenu;
import com.github.sirblobman.freeze.task.MeltTask;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;

public final class FreezePlugin extends JavaPlugin {
    private final FreezeManager freezeManager;
    private final FreezeConfiguration configuration;
    private final MessageConfiguration messages;
    private final FoliaHelper foliaHelper;
    private MiniMessage miniMessage;

    public FreezePlugin() {
        this.freezeManager = new FreezeManager(this);
        this.configuration = new FreezeConfiguration(this);
        this.messages = new MessageConfiguration(this);
        this.foliaHelper = new FoliaHelper(this);
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

    public @NotNull MessageConfiguration getMessages() {
        return this.messages;
    }

    public @NotNull FoliaHelper getFoliaHelper() {
        return this.foliaHelper;
    }

    public @NotNull MiniMessage getMiniMessage() {
        if (this.miniMessage == null) {
            this.miniMessage = MiniMessage.miniMessage();
        }

        return this.miniMessage;
    }

    public void reloadConfiguration() {
        reloadConfig();
        this.configuration.loadConfiguration(getConfig());

        File dataFolder = getDataFolder();
        File messagesFile = new File(dataFolder, "messages.yml");
        YamlConfiguration messages = YamlConfiguration.loadConfiguration(messagesFile);
        this.messages.loadMessages(messages);
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, registry -> {
            Commands registrar = registry.registrar();
            new CommandFreeze(this).register(registrar);
            new CommandMelt(this).register(registrar);
        });
    }

    private void registerListeners() {
        new ListenerBreakAndPlace(this).register();
        new ListenerCommand(this).register();
        new ListenerDamage(this).register();
        new ListenerPlayerMove(this).register();
        new ListenerTeleport(this).register();
        new ListenerFakeMenu(this).register();
        new ListenerItemDropping(this).register();
        new ListenerItemMoving(this).register();
        new ListenerPowderedSnowEffect(this).register();
    }

    private void registerTasks() {
        new MeltTask(this).register();
    }

    private void register_bStats() {
        int pluginId = 16174; // https://bstats.org/plugin/bukkit/Freeze/16174
        new Metrics(this, pluginId);
    }

    public void closeFakeMenu(@NotNull Player player) {
        InventoryView view = player.getOpenInventory();
        Inventory topInventory = view.getTopInventory();
        InventoryHolder holder = topInventory.getHolder();
        if (holder instanceof FakeMenu) {
            player.closeInventory();
        }
    }
}
