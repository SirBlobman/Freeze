package com.github.sirblobman.freeze.command;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

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

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        if (args.length < 1) {
            return false;
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

        freezeManager.setFrozen(target ,true);
        sendMessage(sender, "freeze", replacer);
        return true;
    }
}
