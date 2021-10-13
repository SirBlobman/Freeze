package com.github.sirblobman.freeze.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

import org.jetbrains.annotations.NotNull;

public abstract class FreezeCommand extends Command {
    private final FreezePlugin plugin;
    
    public FreezeCommand(FreezePlugin plugin, String commandName) {
        super(plugin, commandName);
        this.plugin = plugin;
    }
    
    @NotNull
    @Override
    protected final LanguageManager getLanguageManager() {
        FreezePlugin plugin = getFreezePlugin();
        return plugin.getLanguageManager();
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
    
    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        return false;
    }
    
    protected final FreezePlugin getFreezePlugin() {
        return this.plugin;
    }
    
    protected final FreezeManager getFreezeManager() {
        FreezePlugin plugin = getFreezePlugin();
        return plugin.getFreezeManager();
    }
    
    protected final boolean isImmune(Player player) {
        return player.hasPermission("freeze.immune");
    }
    
    protected final Replacer getReplacer(String literal, String replacement) {
        return new BasicReplacer(literal, replacement);
    }
}
