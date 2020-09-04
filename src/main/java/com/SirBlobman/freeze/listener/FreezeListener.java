package com.SirBlobman.freeze.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.SirBlobman.api.language.LanguageManager;
import com.SirBlobman.api.nms.MultiVersionHandler;
import com.SirBlobman.api.nms.PlayerHandler;
import com.SirBlobman.api.utility.Validate;
import com.SirBlobman.freeze.FreezePlugin;
import com.SirBlobman.freeze.manager.FreezeManager;

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