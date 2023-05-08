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

public final class CommandMeltAll extends FreezeCommand {
    public CommandMeltAll(@NotNull FreezePlugin plugin) {
        super(plugin, "melt-all");
        setPermissionName("freeze.command.melt.all");
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        int count = meltAllPlayers();
        if (count <= 0) {
            sendMessage(sender, "unfreeze-all-failure");
            return true;
        }

        Replacer replacer = new IntegerReplacer("{amount}", count);
        sendMessage(sender, "unfreeze-all", replacer);
        return true;
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
        FreezeManager freezeManager = getFreezeManager();
        if (freezeManager.isFrozen(player)) {
            freezeManager.setFrozen(player, false);
            return true;
        }

        return false;
    }
}
