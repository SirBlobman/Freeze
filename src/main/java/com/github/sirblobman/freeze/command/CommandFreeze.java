package com.github.sirblobman.freeze.command;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.nms.PlayerHandler;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

import org.jetbrains.annotations.NotNull;

public final class CommandFreeze extends Command {
    private final FreezePlugin plugin;
    public CommandFreeze(FreezePlugin plugin) {
        super(plugin, "freeze");
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public LanguageManager getLanguageManager() {
        return this.plugin.getLanguageManager();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if(args.length == 1) {
            Set<String> valueSet = getOnlinePlayerNames();
            if(sender.hasPermission("freeze.reload")) valueSet.add("reload");
            return getMatching(valueSet, args[0]);
        }

        return Collections.emptyList();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length < 1) return false;

        String sub = args[0].toLowerCase();
        if(sub.equals("reload") && sender.hasPermission("freeze.reload")) {
            ConfigurationManager configurationManager = this.plugin.getConfigurationManager();
            configurationManager.reload("config.yml");
            configurationManager.reload("language.yml");

            LanguageManager languageManager = getLanguageManager();
            languageManager.reloadLanguages();

            sendMessageOrDefault(sender, "reload-success", "", null, true);
            return true;
        }

        Player target = findTarget(sender, args[0]);
        if(target == null) return true;

        String targetName = target.getName();
        Replacer targetReplacer = message -> message.replace("{target}", targetName);

        FreezeManager freezeManager = this.plugin.getFreezeManager();
        if(freezeManager.isFrozen(target)) {
            freezeManager.setFrozen(target, false);
            sendMessageOrDefault(sender, "unfreeze", "", targetReplacer, true);
            sendUnfrozenMessage(target);
        } else {
            if(target.hasPermission("freeze.immune")) {
                sendMessageOrDefault(sender, "error.player-immune", "", targetReplacer, true);
                return true;
            }

            freezeManager.setFrozen(target, true);
            sendMessageOrDefault(sender, "freeze", "", targetReplacer, true);
        }

        return true;
    }

    private void sendUnfrozenMessage(Player player) {
        LanguageManager languageManager = getLanguageManager();
        String message = languageManager.getMessage(player, "action-bar.melted", null, true);
        if(message.isEmpty()) return;

        MultiVersionHandler multiVersionHandler = this.plugin.getMultiVersionHandler();
        PlayerHandler playerHandler = multiVersionHandler.getPlayerHandler();
        playerHandler.sendActionBar(player, message);
    }
}
