package com.github.sirblobman.freeze;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.bstats.bukkit.Metrics;
import com.github.sirblobman.api.bstats.charts.SimplePie;
import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.Language;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.plugin.ConfigurablePlugin;
import com.github.sirblobman.api.update.UpdateManager;
import com.github.sirblobman.freeze.command.CommandFreeze;
import com.github.sirblobman.freeze.listener.ListenerBreakAndPlace;
import com.github.sirblobman.freeze.listener.ListenerCommand;
import com.github.sirblobman.freeze.listener.ListenerDamage;
import com.github.sirblobman.freeze.listener.ListenerFakeMenu;
import com.github.sirblobman.freeze.listener.ListenerMove;
import com.github.sirblobman.freeze.listener.ListenerTeleport;
import com.github.sirblobman.freeze.manager.FakeMenuManager;
import com.github.sirblobman.freeze.manager.FreezeManager;
import com.github.sirblobman.freeze.menu.FakeMenu;

public final class FreezePlugin extends ConfigurablePlugin {
    private final FreezeManager freezeManager;
    private final FakeMenuManager fakeMenuManager;

    public FreezePlugin() {
        this.freezeManager = new FreezeManager(this);
        this.fakeMenuManager = new FakeMenuManager(this);
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

        registerCommands();
        registerListeners();

        registerUpdateChecker();
        registerbStats();
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

        LanguageManager languageManager = getLanguageManager();
        languageManager.reloadLanguageFiles();

        FakeMenuManager fakeMenuManager = getFakeMenuManager();
        fakeMenuManager.reload();
    }

    public void closeFakeMenu(Player player) {
        InventoryView openInventory = player.getOpenInventory();
        Inventory topInventory = openInventory.getTopInventory();
        InventoryHolder holder = topInventory.getHolder();
        if (holder instanceof FakeMenu) {
            player.closeInventory();
        }
    }

    public FreezeManager getFreezeManager() {
        return this.freezeManager;
    }

    public FakeMenuManager getFakeMenuManager() {
        return this.fakeMenuManager;
    }

    private void registerCommands() {
        new CommandFreeze(this).register();
    }

    private void registerListeners() {
        new ListenerBreakAndPlace(this).register();
        new ListenerCommand(this).register();
        new ListenerDamage(this).register();
        new ListenerMove(this).register();
        new ListenerTeleport(this).register();
        new ListenerFakeMenu(this).register();
    }

    private void registerUpdateChecker() {
        CorePlugin corePlugin = JavaPlugin.getPlugin(CorePlugin.class);
        UpdateManager updateManager = corePlugin.getUpdateManager();
        updateManager.addResource(this, 31822L);
    }

    private void registerbStats() {
        Metrics metrics = new Metrics(this, 16174);
        metrics.addCustomChart(new SimplePie("selected_language", this::getDefaultLanguageCode));
    }

    private String getDefaultLanguageCode() {
        LanguageManager languageManager = getLanguageManager();
        Language defaultLanguage = languageManager.getDefaultLanguage();
        return (defaultLanguage == null ? "none" : defaultLanguage.getLanguageCode());
    }
}
