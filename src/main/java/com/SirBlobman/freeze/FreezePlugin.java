package com.SirBlobman.freeze;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.SirBlobman.api.configuration.ConfigurationManager;
import com.SirBlobman.api.language.LanguageManager;
import com.SirBlobman.api.nms.MultiVersionHandler;
import com.SirBlobman.api.update.UpdateChecker;
import com.SirBlobman.core.CorePlugin;
import com.SirBlobman.freeze.command.CommandFreeze;
import com.SirBlobman.freeze.listener.ListenerCommand;
import com.SirBlobman.freeze.listener.ListenerMove;
import com.SirBlobman.freeze.listener.ListenerTeleport;
import com.SirBlobman.freeze.manager.FreezeManager;

public final class FreezePlugin extends JavaPlugin {
    private final ConfigurationManager configurationManager;
    private final LanguageManager languageManager;
    private final FreezeManager freezeManager;
    public FreezePlugin() {
        this.configurationManager = new ConfigurationManager(this);
        this.languageManager = new LanguageManager(this, this.configurationManager);
        this.freezeManager = new FreezeManager(this);
    }

    @Override
    public void onLoad() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.saveDefault("config.yml");
        configurationManager.saveDefault("language.yml");
        configurationManager.saveDefault("language/en_us.lang.yml");
    }

    @Override
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ListenerMove(this), this);
        pluginManager.registerEvents(new ListenerTeleport(this), this);
        pluginManager.registerEvents(new ListenerCommand(this), this);

        new CommandFreeze(this).register();

        UpdateChecker updateChecker = new UpdateChecker(this, 31822L);
        updateChecker.runCheck();
    }

    @Override
    public void onDisable() {
        FreezeManager freezeManager = getFreezeManager();
        freezeManager.removeAll();

        HandlerList.unregisterAll(this);
    }

    @Override
    public YamlConfiguration getConfig() {
        ConfigurationManager configurationManager = getConfigurationManager();
        return configurationManager.get("config.yml");
    }

    @Override
    public void saveDefaultConfig() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.saveDefault("config.yml");
    }

    @Override
    public void reloadConfig() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("config.yml");
        configurationManager.reload("language.yml");
        configurationManager.reload("language/en_us.lang.yml");
    }

    @Override
    public void saveConfig() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.save("config.yml");
    }

    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public MultiVersionHandler getMultiVersionHandler() {
        CorePlugin plugin = JavaPlugin.getPlugin(CorePlugin.class);
        return plugin.getMultiVersionHandler();
    }

    public FreezeManager getFreezeManager() {
        return this.freezeManager;
    }
}
