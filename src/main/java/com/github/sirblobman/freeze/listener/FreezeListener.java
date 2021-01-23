package com.github.sirblobman.freeze.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.nms.PlayerHandler;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public abstract class FreezeListener implements Listener {
    private final FreezePlugin plugin;
    public FreezeListener(FreezePlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    protected final FreezePlugin getPlugin() {
        return this.plugin;
    }

    protected final FreezeManager getFreezeManager() {
        FreezePlugin plugin = getPlugin();
        return plugin.getFreezeManager();
    }

    protected final void sendFrozenMessage(Player player) {
        FreezePlugin plugin = getPlugin();
        LanguageManager languageManager = plugin.getLanguageManager();
        String message = languageManager.getMessageColored(player, "action-bar.frozen");
        if(message.isEmpty()) return;

        MultiVersionHandler multiVersionHandler = plugin.getMultiVersionHandler();
        PlayerHandler playerHandler = multiVersionHandler.getPlayerHandler();
        playerHandler.sendActionBar(player, message);
    }
}