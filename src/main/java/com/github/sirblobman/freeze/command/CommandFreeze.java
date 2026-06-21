package com.github.sirblobman.freeze.command;

import java.time.Duration;
import java.time.Instant;
import java.time.InstantSource;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.kyori.adventure.text.Component;

public final class CommandFreeze implements Command<CommandSourceStack> {
    private final FreezePlugin plugin;

    public CommandFreeze(@NotNull FreezePlugin plugin) {
        this.plugin = plugin;
    }

    private @NotNull FreezePlugin getPlugin() {
        return this.plugin;
    }

    public void register(@NotNull Commands registrar) {
        registrar.register(setupFreezeCommand());
    }

    /*
     * Commands:
     * - /freeze reload
     * - /freeze all
     * - /freeze <player> [time]
     */
    private @NotNull LiteralCommandNode<CommandSourceStack> setupFreezeCommand() {
        LiteralArgumentBuilder<CommandSourceStack> freezeCommandRoot = Commands.literal("freeze");
        freezeCommandRoot.requires(new PermissionRequirement("freeze.command.freeze"));

        freezeCommandRoot.then(Commands.literal("reload")
                .requires(new PermissionRequirement("freeze.command.freeze.reload"))
                .executes(new CommandFreezeReload(getPlugin())));

        freezeCommandRoot.then(Commands.literal("all")
                .requires(new PermissionRequirement("freeze.command.freeze.all"))
                .executes(new CommandFreezeAll(getPlugin())));

        freezeCommandRoot.then(
                Commands.argument("player", ArgumentTypes.player()).executes(this)
                        .then(Commands.argument("time", StringArgumentType.word()).executes(this::runWithTime)));
        return freezeCommandRoot.build();
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

        long systemTime = System.currentTimeMillis();
        long expireTime = (systemTime + TimeUnit.DAYS.toMillis(99));

        FreezePlugin plugin = getPlugin();
        freezeManager.freeze(target, expireTime);

        MessageConfiguration messages = plugin.getMessages();
        messages.sendMessage(sender, "freeze", new EntityReplacer("{target}", target));
        return Command.SINGLE_SUCCESS;
    }

    private int runWithTime(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
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

        String time = StringArgumentType.getString(context, "time");
        Instant expireTime = parseTimeOffset(time);
        if (expireTime == null) {
            Component component = Component.text("'" + time + "' is not in the correct time format.");
            Message message = MessageComponentSerializer.message().serialize(component);
            throw new SimpleCommandExceptionType(message).create();
        }

        FreezePlugin plugin = getPlugin();
        long expireTimeMillis = expireTime.toEpochMilli();
        freezeManager.freeze(target, expireTimeMillis);

        MessageConfiguration messages = plugin.getMessages();
        messages.sendMessage(sender, "freeze", new EntityReplacer("{target}", target));
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Parse a time offset from the current system time.
     * @param time A string with the format {@code "[<years>y][<months>mo][<weeks>w][<days>d][<hours>h][<minutes>m<seconds>s]"}
     * @return An {@link Instant} with the time, or {@code null} if one could not be parsed.
     */
    private @Nullable Instant parseTimeOffset(@NotNull String time) {
        String regex = "^(?:(\\d+)y)?(?:(\\d+)mo)?(?:(\\d+)w)?(?:(\\d+)d)?(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(time);
        if (!matcher.matches()) {
            return null;
        }

        int years = parseMatcherPart(matcher, 1, 10);
        int months = parseMatcherPart(matcher, 2, 12);
        int weeks = parseMatcherPart(matcher, 3, 4);
        int days = parseMatcherPart(matcher, 4, 7);
        int hours = parseMatcherPart(matcher, 5, 24);
        int minutes = parseMatcherPart(matcher, 6, 60);
        int seconds = parseMatcherPart(matcher, 7, 60);

        Duration duration = Duration.ZERO;

        if (years > 0) {
            duration = duration.plus(Duration.of(years, ChronoUnit.YEARS));
        }

        if (months > 0) {
            duration = duration.plus(Duration.of(months, ChronoUnit.MONTHS));
        }

        if (weeks > 0) {
            duration = duration.plus(Duration.of(weeks, ChronoUnit.WEEKS));
        }

        if (days > 0) {
            duration = duration.plus(Duration.of(days, ChronoUnit.DAYS));
        }

        if (hours > 0) {
            duration = duration.plus(Duration.of(hours, ChronoUnit.HOURS));
        }

        if (minutes > 0) {
            duration = duration.plus(Duration.of(minutes, ChronoUnit.MINUTES));
        }

        if (seconds > 0) {
            duration = duration.plus(Duration.of(seconds, ChronoUnit.SECONDS));
        }

        InstantSource now = InstantSource.system();
        InstantSource future = InstantSource.offset(now, duration);
        return future.instant();
    }

    private int parseMatcherPart(@NotNull Matcher matcher, int part, int max) {
        String group = matcher.group(part);
        if (group == null) {
            return 0;
        }

        try {
            int value = Integer.parseInt(group);
            return Math.clamp(value, 0, max);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
