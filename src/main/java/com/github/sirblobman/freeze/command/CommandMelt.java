package com.github.sirblobman.freeze.command;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;

import com.github.sirblobman.freeze.FreezeManager;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.configuration.MessageConfiguration;
import com.github.sirblobman.freeze.message.EntityReplacer;
import com.github.sirblobman.freeze.message.Replacer;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.kyori.adventure.text.Component;

public final class CommandMelt implements Command<CommandSourceStack> {
    private final FreezePlugin plugin;

    public CommandMelt(@NotNull FreezePlugin plugin) {
        this.plugin = plugin;
    }

    private @NotNull FreezePlugin getPlugin() {
        return this.plugin;
    }

    public void register(@NotNull Commands registrar) {
        registrar.register(setupMeltCommand());
    }

    /*
     * Commands:
     * - /melt all
     * - /freeze <player>
     */
    private @NotNull LiteralCommandNode<CommandSourceStack> setupMeltCommand() {
        LiteralArgumentBuilder<CommandSourceStack> meltCommandRoot = Commands.literal("melt");
        meltCommandRoot.requires(new PermissionRequirement("freeze.command.melt"));

        meltCommandRoot.then(Commands.literal("all")
                .requires(new PermissionRequirement("freeze.command.melt.all"))
                .executes(new CommandMeltAll(getPlugin())));

        meltCommandRoot.then(Commands.argument("player", ArgumentTypes.player()).executes(this));
        return meltCommandRoot.build();
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        CommandSender sender = source.getSender();

        PlayerSelectorArgumentResolver playerSelector = context.getArgument("player", PlayerSelectorArgumentResolver.class);
        Player target = playerSelector.resolve(source).getFirst();

        FreezeManager freezeManager = getPlugin().getFreezeManager();
        if (freezeManager.isImmune(target)) {
            Replacer replacer = new EntityReplacer("{target}", target);
            Component component = getPlugin().getMessages().getMessageComponent("error.player-immune", replacer);
            if (component == null) {
                component = Component.text("target immune!");
            }

            Message message = MessageComponentSerializer.message().serialize(component);
            throw new SimpleCommandExceptionType(message).create();
        }

        if (!freezeManager.isFrozen(target)) {
            Replacer replacer = new EntityReplacer("{target}", target);
            Component component = getPlugin().getMessages().getMessageComponent("error.not-frozen", replacer);
            if (component == null) {
                component = Component.text("target not frozen!");
            }

            Message message = MessageComponentSerializer.message().serialize(component);
            throw new SimpleCommandExceptionType(message).create();
        }

        FreezePlugin plugin = getPlugin();
        freezeManager.melt(target);

        MessageConfiguration messages = plugin.getMessages();
        messages.sendMessage(sender, "unfreeze", new EntityReplacer("{target}", target));
        return Command.SINGLE_SUCCESS;
    }
}
