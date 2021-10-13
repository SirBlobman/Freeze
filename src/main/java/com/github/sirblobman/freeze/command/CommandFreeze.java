package com.github.sirblobman.freeze.command;

import com.github.sirblobman.freeze.FreezePlugin;

public final class CommandFreeze extends FreezeCommand {
    public CommandFreeze(FreezePlugin plugin) {
        super(plugin, "freeze");
        addSubCommand(new CommandFreezeFreeze(plugin));
        addSubCommand(new CommandFreezeHelp(plugin));
        addSubCommand(new CommandFreezeMelt(plugin));
        addSubCommand(new CommandFreezeReload(plugin));
    }
}
