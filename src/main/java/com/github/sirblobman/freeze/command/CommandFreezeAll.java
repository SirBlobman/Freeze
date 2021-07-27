package com.github.sirblobman.freeze.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

import org.jetbrains.annotations.NotNull;

public final class CommandFreezeAll extends Command {
    private final FreezePlugin plugin;

    public CommandFreezeAll(FreezePlugin plugin) {
        super(plugin, "freeze-all");
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public LanguageManager getLanguageManager() {
        return this.plugin.getLanguageManager();
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        int freezeCount = 0;

        for(Player target : onlinePlayerCollection) {
            boolean freeze = freeze(target);
            if(freeze) freezeCount++;
        }

        String amountString = Integer.toString(freezeCount);
        Replacer replacer = message -> message.replace("{amount}", amountString);

        LanguageManager languageManager = getLanguageManager();
        languageManager.sendMessage(sender, "freeze-all", replacer, true);
        return true;
    }

    private boolean freeze(Player target) {
        FreezeManager freezeManager = this.plugin.getFreezeManager();
        if(freezeManager.isFrozen(target)) return false;
        if(target.hasPermission("freeze.immune")) return false;

        freezeManager.setFrozen(target, true);
        return true;
    }
}
