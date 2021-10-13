package com.github.sirblobman.freeze.command;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.freeze.FreezePlugin;

public final class CommandFreezeReload extends FreezeCommand {
    public CommandFreezeReload(FreezePlugin plugin) {
        super(plugin, "reload");
    }
    
    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        if(!checkPermission(sender, "freeze.command.freeze.reload", true)) {
            return true;
        }
        
        FreezePlugin plugin = getFreezePlugin();
        plugin.onReload();
        
        sendMessage(sender, "reload-success", null, true);
        return true;
    }
}
