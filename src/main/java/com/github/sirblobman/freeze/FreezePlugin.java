package com.github.sirblobman.freeze;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.Language;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.plugin.ConfigurablePlugin;
import com.github.sirblobman.api.update.SpigotUpdateManager;
import com.github.sirblobman.freeze.command.CommandFreeze;
import com.github.sirblobman.freeze.command.CommandFreezeAll;
import com.github.sirblobman.freeze.command.CommandFreezeReload;
import com.github.sirblobman.freeze.command.CommandMelt;
import com.github.sirblobman.freeze.command.CommandMeltAll;
import com.github.sirblobman.freeze.configuration.FreezeConfiguration;
import com.github.sirblobman.freeze.listener.ListenerBreakAndPlace;
import com.github.sirblobman.freeze.listener.ListenerCommand;
import com.github.sirblobman.freeze.listener.ListenerDamage;
import com.github.sirblobman.freeze.listener.ListenerFakeMenu;
import com.github.sirblobman.freeze.listener.ListenerItemDropping;
import com.github.sirblobman.freeze.listener.ListenerItemMoving;
import com.github.sirblobman.freeze.listener.ListenerMove;
import com.github.sirblobman.freeze.listener.ListenerTeleport;
import com.github.sirblobman.freeze.manager.FreezeManager;
import com.github.sirblobman.freeze.menu.FakeMenu;
import com.github.sirblobman.api.shaded.bstats.bukkit.Metrics;
import com.github.sirblobman.api.shaded.bstats.charts.SimplePie;

public final class FreezePlugin extends ConfigurablePlugin {
    private final FreezeManager freezeManager;
    private final FreezeConfiguration configuration;

    public FreezePlugin() {
        this.freezeManager = new FreezeManager(this);
        this.configuration = new FreezeConfiguration(this);
    }

    @Override
    public void onLoad() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.saveDefault("config.yml");

        LanguageManager languageManager = getLanguageManager();
        languageManager.saveDefaultLanguageFiles();
    }

    @Override
    public void onEnable() {
        reloadConfiguration();

        LanguageManager languageManager = getLanguageManager();
        languageManager.onPluginEnable();

        registerCommands();
        registerListeners();

        registerUpdateChecker();
        register_bStats();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        FreezeManager freezeManager = getFreezeManager();
        freezeManager.removeAll();

        Collection<? extends Player> playerCollection = Bukkit.getOnlinePlayers();
        for (Player player : playerCollection) {
            closeFakeMenu(player);
        }
    }

    @Override
    protected void reloadConfiguration() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("config.yml");

        YamlConfiguration configurationFile = configurationManager.get("config.yml");
        FreezeConfiguration configuration = getConfiguration();
        configuration.load(configurationFile);

        LanguageManager languageManager = getLanguageManager();
        languageManager.reloadLanguages();
    }

    public void closeFakeMenu(Player player) {
        InventoryView openInventory = player.getOpenInventory();
        Inventory topInventory = openInventory.getTopInventory();
        InventoryHolder holder = topInventory.getHolder();
        if (holder instanceof FakeMenu) {
            player.closeInventory();
        }
    }

    public @NotNull FreezeManager getFreezeManager() {
        return this.freezeManager;
    }

    public @NotNull FreezeConfiguration getConfiguration() {
        return this.configuration;
    }

    private void registerCommands() {
        new CommandFreeze(this).register();
        new CommandFreezeAll(this).register();
        new CommandFreezeReload(this).register();
        new CommandMelt(this).register();
        new CommandMeltAll(this).register();
    }

    private void registerListeners() {
        new ListenerBreakAndPlace(this).register();
        new ListenerCommand(this).register();
        new ListenerDamage(this).register();
        new ListenerMove(this).register();
        new ListenerTeleport(this).register();
        new ListenerFakeMenu(this).register();
        new ListenerItemDropping(this).register();
        new ListenerItemMoving(this).register();
    }

    private void registerUpdateChecker() {
        CorePlugin corePlugin = JavaPlugin.getPlugin(CorePlugin.class);
        SpigotUpdateManager updateManager = corePlugin.getSpigotUpdateManager();
        updateManager.addResource(this, 31822L);
    }

    private void register_bStats() {
        Metrics metrics = new Metrics(this, 16174);
        metrics.addCustomChart(new SimplePie("selected_language", this::getDefaultLanguageCode));
    }

    private @NotNull String getDefaultLanguageCode() {
        LanguageManager languageManager = getLanguageManager();
        Language defaultLanguage = languageManager.getDefaultLanguage();
        return (defaultLanguage == null ? "none" : defaultLanguage.getLanguageName());
    }
}
