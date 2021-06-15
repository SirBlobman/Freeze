package com.github.sirblobman.freeze.listener;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.core.listener.PluginListener;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.nms.PlayerHandler;
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

    protected final void sendFrozenMessage(Player player) {
        FreezePlugin plugin = getPlugin();
        LanguageManager languageManager = plugin.getLanguageManager();
        String message = languageManager.getMessage(player, "action-bar.frozen", null, true);
        if(message.isEmpty()) return;

        MultiVersionHandler multiVersionHandler = plugin.getMultiVersionHandler();
        PlayerHandler playerHandler = multiVersionHandler.getPlayerHandler();
        playerHandler.sendActionBar(player, message);
    }
}
