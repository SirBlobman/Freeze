package com.github.sirblobman.freeze.command;

import com.github.sirblobman.freeze.FreezePlugin;

public final class CommandFreezeFreeze extends FreezeCommand {
    public CommandFreezeFreeze(FreezePlugin plugin) {
        super(plugin, "freeze");
        addSubCommand(new CommandFreezeFreezeAll(plugin));
        addSubCommand(new CommandFreezeFreezePlayer(plugin));
    }
}
