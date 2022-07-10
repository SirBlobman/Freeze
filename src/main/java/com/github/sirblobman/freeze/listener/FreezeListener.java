package com.github.sirblobman.freeze.listener;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.nms.PlayerHandler;
import com.github.sirblobman.api.plugin.listener.PluginListener;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public abstract class FreezeListener extends PluginListener<FreezePlugin> {
    public FreezeListener(FreezePlugin plugin) {
        super(plugin);
    }
    
    protected final FreezeManager getFreezeManager() {
        FreezePlugin plugin = getPlugin();
        return plugin.getFreezeManager();
    }
    
    protected final ConfigurationManager getConfigurationManager() {
        FreezePlugin plugin = getPlugin();
        return plugin.getConfigurationManager();
    }
    
    protected final LanguageManager getLanguageManager() {
        FreezePlugin plugin = getPlugin();
        return plugin.getLanguageManager();
    }
    
    protected final MultiVersionHandler getMultiVersionHandler() {
        FreezePlugin plugin = getPlugin();
        return plugin.getMultiVersionHandler();
    }
    
    protected final PlayerHandler getPlayerHandler() {
        MultiVersionHandler multiVersionHandler = getMultiVersionHandler();
        return multiVersionHandler.getPlayerHandler();
    }
    
    protected final YamlConfiguration getConfiguration() {
        ConfigurationManager configurationManager = getConfigurationManager();
        return configurationManager.get("config.yml");
    }
    
    protected final void sendFrozenMessage(Player player) {
        LanguageManager languageManager = getLanguageManager();
        String message = languageManager.getMessage(player, "action-bar.frozen", null, true);
        if(message.isEmpty()) {
            return;
        }
        
        PlayerHandler playerHandler = getPlayerHandler();
        playerHandler.sendActionBar(player, message);
    }
    
    protected final boolean isSimilar(Location location1, Location location2) {
        double x1 = location1.getX();
        double y1 = location1.getY();
        double z1 = location1.getZ();
        double x2 = location2.getX();
        double y2 = location2.getY();
        double z2 = location2.getZ();
        return (x1 == x2 && z1 == z2 && y2 <= y1);
    }
    
    protected final void checkEvent(Player player, Cancellable e) {
        boolean disabled = isDisabled();
        checkEvent(player, e, disabled);
    }
    
    protected final void checkEvent(Player player, Cancellable e, boolean disabled) {
        if(disabled) {
            return;
        }
        
        FreezeManager freezeManager = getFreezeManager();
        if(!freezeManager.isFrozen(player)) {
            return;
        }
        
        e.setCancelled(true);
        sendFrozenMessage(player);
    }
    
    protected abstract boolean isDisabled();
}
