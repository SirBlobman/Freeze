package com.github.sirblobman.freeze.command;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;

import com.github.sirblobman.freeze.FreezeManager;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.configuration.MessageConfiguration;
import com.github.sirblobman.freeze.message.IntegerReplacer;
import com.github.sirblobman.freeze.message.Replacer;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.kyori.adventure.text.Component;

public class CommandMeltAll implements Command<CommandSourceStack> {
    private final FreezePlugin plugin;

    public CommandMeltAll(FreezePlugin plugin) {
        this.plugin = plugin;
    }

    private @NotNull FreezePlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        MessageConfiguration messages = getPlugin().getMessages();
        CommandSender sender = context.getSource().getSender();

        int count = meltAllPlayers();
        if (count <= 0) {
            Component component = messages.getMessageComponent("unfreeze-all-failure");
            if (component == null) {
                component = Component.text("No players were melted.");
            }

            Message message = MessageComponentSerializer.message().serialize(component);
            throw new SimpleCommandExceptionType(message).create();
        }

        Replacer replacer = new IntegerReplacer("{amount}", count);
        messages.sendMessage(sender, "unfreeze-all", replacer);
        return Command.SINGLE_SUCCESS;
    }

    private int meltAllPlayers() {
        int count = 0;

        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayerCollection) {
            if (melt(player)) {
                count++;
            }
        }

        return count;
    }

    private boolean melt(@NotNull Player player) {
        FreezeManager freezeManager = getPlugin().getFreezeManager();
        if (freezeManager.isImmune(player)) {
            return false;
        }

        if (!freezeManager.isFrozen(player)) {
            return false;
        }

        freezeManager.melt(player);
        return true;
    }
}
