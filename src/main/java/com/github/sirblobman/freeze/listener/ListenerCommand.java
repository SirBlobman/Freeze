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
import com.github.sirblobman.freeze.manager.FreezeManager;

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
    public void beforeCommand(PlayerCommandPreprocessEvent e) {
        if (isDisabled()) {
            return;
        }

        Player player = e.getPlayer();
        FreezeManager freezeManager = getFreezeManager();
        if (!freezeManager.isFrozen(player)) {
            return;
        }

        String commandMessage = e.getMessage();
        String command = fixCommand(commandMessage);
        if (isAllowed(command) || !isBlocked(command)) {
            return;
        }

        e.setCancelled(true);
        sendFrozenMessage(player);
    }

    private @NotNull String fixCommand(@NotNull String message) {
        if (!message.startsWith("/")) {
            message = ("/" + message);
        }

        return message;
    }

    private boolean isBlocked(@NotNull String command) {
        FreezeConfiguration configuration = getConfiguration();
        List<String> blockedCommandList = configuration.getBlockedCommands();
        return matchesAny(command, blockedCommandList);
    }

    private boolean isAllowed(@NotNull String command) {
        FreezeConfiguration configuration = getConfiguration();
        List<String> allowedCommandList = configuration.getAllowedCommands();
        return matchesAny(command, allowedCommandList);
    }

    private boolean matchesAny(@NotNull String command, @NotNull Collection<String> values) {
        if (values.contains("*") || values.contains("/*")) {
            return true;
        }

        String commandLowerCase = command.toLowerCase(Locale.US);
        for (String value : values) {
            String valueLowerCase = value.toLowerCase(Locale.US);
            if (commandLowerCase.equals(valueLowerCase)) {
                return true;
            }

            String valueSpace = (valueLowerCase + " ");
            if (commandLowerCase.startsWith(valueSpace)) {
                return true;
            }
        }

        return false;
    }
}
