package com.github.sirblobman.freeze.command;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public class SubCommandFreezePlayer extends FreezeCommand {
    public SubCommandFreezePlayer(FreezePlugin plugin) {
        super(plugin, "player");
        setPermissionName("freeze.command.freeze.freeze.player");
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Set<String> valueSet = getOnlinePlayerNames();
            return getMatching(args[0], valueSet);
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }

        Player target = findTarget(sender, args[0]);
        if (target == null) {
            return true;
        }

        String targetName = target.getName();
        Replacer targetNameReplacer = new StringReplacer("{target}", targetName);

        if (isImmune(target)) {
            sendMessage(sender, "error.player-immune", targetNameReplacer);
            return true;
        }

        FreezeManager freezeManager = getFreezeManager();
        if (freezeManager.isFrozen(target)) {
            sendMessage(sender, "error.currently-frozen", targetNameReplacer);
            return true;
        }

        freezeManager.setFrozen(target, true);
        sendMessage(sender, "freeze", targetNameReplacer);
        return true;
    }
}
