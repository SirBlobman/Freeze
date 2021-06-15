package com.github.sirblobman.freeze.listener;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public final class ListenerCommand extends FreezeListener {
    public ListenerCommand(FreezePlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority= EventPriority.NORMAL, ignoreCancelled=true)
    public void beforeCommand(PlayerCommandPreprocessEvent e) {
        if(isDisabled()) return;
        FreezeManager freezeManager = getFreezeManager();

        Player player = e.getPlayer();
        if(!freezeManager.isFrozen(player)) return;

        String commandMessage = e.getMessage();
        String command = fixCommand(commandMessage);
        if(isAllowed(command) || !isBlocked(command)) return;

        e.setCancelled(true);
        sendFrozenMessage(player);
    }

    private boolean isDisabled() {
        FreezePlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        return !configuration.getBoolean("prevent-commands", true);
    }

    private boolean isBlocked(String command) {
        FreezePlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");

        List<String> blockedCommandList = configuration.getStringList("blocked-command-list");
        return startsWithAny(command, blockedCommandList);
    }

    private boolean isAllowed(String command) {
        FreezePlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");

        List<String> allowedCommandList = configuration.getStringList("allowed-command-list");
        return startsWithAny(command, allowedCommandList);
    }

    private String fixCommand(String message) {
        if(message.startsWith("/")) return message;
        return ("/" + message);
    }

    private boolean startsWithAny(String command, List<String> valueList) {
        return valueList.stream().anyMatch(command::startsWith);
    }
}
