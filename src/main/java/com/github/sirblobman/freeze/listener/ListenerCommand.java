package com.github.sirblobman.freeze.listener;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.configuration.FreezeConfiguration;

public final class ListenerCommand extends FreezeListener {
    public ListenerCommand(@NotNull FreezePlugin plugin) {
        super(plugin);
    }

    @Override
    protected boolean isDisabled() {
        FreezeConfiguration configuration = getConfiguration();
        return !configuration.isPreventCommands();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void beforeCommand(@NotNull PlayerCommandPreprocessEvent event) {
        if (isDisabled()) {
            return;
        }

        Player player = event.getPlayer();
        if(!getFreezeManager().isFrozen(player)) {
            return;
        }

        String commandMessage = event.getMessage();
        String command = fixCommand(commandMessage);
        if (isAllowed(command) || !isBlocked(command)) {
            return;
        }

        event.setCancelled(true);
        sendFrozenMessage(player);
    }

    private @NotNull String fixCommand(@NotNull String command) {
        if(!command.startsWith("/")) {
            command = ("/" + command);
        }

        return command;
    }

    private boolean isAllowed(@NotNull String command) {
        List<String> allowedCommandList = getConfiguration().getAllowedCommandList();
        return matchesAny(allowedCommandList, command);
    }

    private boolean isBlocked(@NotNull String command) {
        List<String> blockedCommandList = getConfiguration().getBlockedCommandList();
        return matchesAny(blockedCommandList, command);
    }

    private boolean matchesAny(@NotNull Collection<String> values, @NotNull String command) {
        if (values.contains("*") || values.contains("/*")) {
            return true;
        }

        String commandLower = command.toLowerCase(Locale.US);
        for (String value : values) {
            String valueLower = value.toLowerCase(Locale.US);
            if (commandLower.equals(valueLower)) {
                return true;
            }

            String valueSpace = (valueLower + " ");
            if (commandLower.startsWith(valueSpace)) {
                return true;
            }
        }

        return false;
    }
}
