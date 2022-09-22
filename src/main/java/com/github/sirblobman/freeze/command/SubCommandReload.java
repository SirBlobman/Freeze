package com.github.sirblobman.freeze.command;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.freeze.FreezePlugin;

public final class SubCommandReload extends FreezeCommand {
    public SubCommandReload(FreezePlugin plugin) {
        super(plugin, "reload");
        setPermissionName("freeze.command.freeze.reload");
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        FreezePlugin plugin = getFreezePlugin();
        plugin.reloadConfig();

        sendMessage(sender, "reload-success", null);
        return true;
    }
}
