package com.SirBlobman.freeze;

import com.SirBlobman.freeze.command.CommandFreeze;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Freeze extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();

        registerCommand("freeze", new CommandFreeze(this));
    }

    private void registerCommand(String commandName, CommandExecutor executor) {
        if(commandName == null || commandName.isEmpty() || executor == null) return;

        PluginCommand command = getCommand(commandName);
        if(command == null) {
            Logger logger = getLogger();
            logger.warning("Could not find command '" + commandName + "' in plugin.yml. Please contact SirBlobman!");
            return;
        }
        command.setExecutor(executor);

        if(executor instanceof TabCompleter) {
            TabCompleter completer = (TabCompleter) executor;
            command.setTabCompleter(completer);
        }

        if(executor instanceof Listener) {
            Listener listener = (Listener) executor;
            registerListener(listener);
        }
    }

    private void registerListener(Listener listener) {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(listener, this);
    }
}