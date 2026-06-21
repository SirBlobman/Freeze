package com.github.sirblobman.freeze.command;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.configuration.MessageConfiguration;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

public class CommandFreezeReload implements Command<CommandSourceStack> {
    private final FreezePlugin plugin;

    public CommandFreezeReload(FreezePlugin plugin) {
        this.plugin = plugin;
    }

    private @NotNull FreezePlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        FreezePlugin plugin = getPlugin();
        plugin.reloadConfiguration();

        MessageConfiguration messages = plugin.getMessages();
        CommandSender sender = context.getSource().getSender();
        messages.sendMessage(sender, "reload-success");
        return Command.SINGLE_SUCCESS;
    }
}
