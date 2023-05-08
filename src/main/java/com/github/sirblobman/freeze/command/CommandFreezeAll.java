package com.github.sirblobman.freeze.command;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.language.replacer.IntegerReplacer;
import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public final class CommandFreezeAll extends FreezeCommand {
    public CommandFreezeAll(@NotNull FreezePlugin plugin) {
        super(plugin, "freeze-all");
        setPermissionName("freeze.command.freeze.all");
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        int count = freezeAllPlayers();
        if (count <= 0) {
            sendMessage(sender, "freeze-all-failure");
            return true;
        }

        Replacer replacer = new IntegerReplacer("{amount}", count);
        sendMessage(sender, "freeze-all", replacer);
        return true;
    }

    private int freezeAllPlayers() {
        int count = 0;

        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayerCollection) {
            if (freeze(player)) {
                count++;
            }
        }

        return count;
    }

    private boolean freeze(@NotNull Player player) {
        if (isImmune(player)) {
            return false;
        }

        FreezeManager freezeManager = getFreezeManager();
        if (freezeManager.isFrozen(player)) {
            return true;
        }

        freezeManager.setFrozen(player, true);
        return true;
    }
}
