package com.github.sirblobman.freeze;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.plugin.ConfigurablePlugin;
import com.github.sirblobman.api.update.UpdateManager;
import com.github.sirblobman.freeze.command.CommandFreeze;
import com.github.sirblobman.freeze.listener.ListenerCommand;
import com.github.sirblobman.freeze.listener.ListenerMove;
import com.github.sirblobman.freeze.listener.ListenerTeleport;
import com.github.sirblobman.freeze.manager.FreezeManager;

public final class FreezePlugin extends ConfigurablePlugin {
    private final FreezeManager freezeManager;

    public FreezePlugin() {
        this.freezeManager = new FreezeManager(this);
    }

    @Override
    public void onLoad() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.saveDefault("config.yml");

        LanguageManager languageManager = getLanguageManager();
        languageManager.saveDefaultLanguages();
    }

    @Override
    public void onEnable() {
        LanguageManager languageManager = getLanguageManager();
        languageManager.reloadLanguages();

        new CommandFreeze(this).register();
        new ListenerMove(this).register();
        new ListenerTeleport(this).register();
        new ListenerCommand(this).register();

        CorePlugin corePlugin = JavaPlugin.getPlugin(CorePlugin.class);
        UpdateManager updateManager = corePlugin.getUpdateManager();
        updateManager.addResource(this, 31822L);
    }

    @Override
    public void onDisable() {
        FreezeManager freezeManager = getFreezeManager();
        freezeManager.removeAll();

        HandlerList.unregisterAll(this);
    }

    public FreezeManager getFreezeManager() {
        return this.freezeManager;
    }
}
