package com.github.sirblobman.freeze.command;

import java.time.Duration;
import java.time.Instant;
import java.time.InstantSource;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public final class CommandFreeze extends FreezeCommand {
    public CommandFreeze(@NotNull FreezePlugin plugin) {
        super(plugin, "freeze");
        setPermissionName("freeze.command.freeze");
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull CommandSender sender, String @NotNull [] args) {
        if (args.length == 1) {
            Set<String> valueSet = getOnlinePlayerNames();
            return getMatching(args[0], valueSet);
        }

        if (args.length == 2) {
            return Collections.singletonList("1m30s");
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        if (args.length < 1) {
            return false;
        }

        String time = (args.length < 2 ? null : args[1]);
        Instant expireTime = null;
        if (time != null) {
            expireTime = parseTimeOffset(time);
            if (expireTime == null) {
                return false;
            }
        }

        Player target = findTarget(sender, args[0]);
        if (target == null) {
            return true;
        }

        String targetName = target.getName();
        Replacer replacer = new StringReplacer("{target}", targetName);

        if (isImmune(target)) {
            sendMessage(sender, "error.player-immune", replacer);
            return true;
        }

        FreezeManager freezeManager = getFreezeManager();
        if (freezeManager.isFrozen(target)) {
            sendMessage(sender, "error.currently-frozen", replacer);
            return true;
        }

        freezeManager.setFrozen(target, true);

        if (expireTime != null) {
            freezeManager.setExpireTime(target, expireTime);
        }

        sendMessage(sender, "freeze", replacer);
        return true;
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

        Duration duration = Duration.of(years, ChronoUnit.YEARS);
        duration = duration.plus(Duration.of(months, ChronoUnit.MONTHS));
        duration = duration.plus(Duration.of(weeks, ChronoUnit.WEEKS));
        duration = duration.plus(Duration.of(days, ChronoUnit.DAYS));
        duration = duration.plus(Duration.of(hours, ChronoUnit.HOURS));
        duration = duration.plus(Duration.of(minutes, ChronoUnit.MINUTES));
        duration = duration.plus(Duration.of(seconds, ChronoUnit.SECONDS));

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
            return Math.max(0, Math.min(value, max));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
