package com.github.sirblobman.freeze.command;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.freeze.FreezePlugin;

public final class CommandFreezeReload extends FreezeCommand {
    public CommandFreezeReload(@NotNull FreezePlugin plugin) {
        super(plugin, "freeze-reload");
        setPermissionName("freeze.command.freeze.reload");
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        FreezePlugin plugin = getFreezePlugin();
        plugin.reloadConfig();

        sendMessage(sender, "reload-success");
        return true;
    }
}
