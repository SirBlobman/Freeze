package com.github.sirblobman.freeze.command;

import com.github.sirblobman.freeze.FreezePlugin;

public final class CommandFreeze extends FreezeCommand {
    public CommandFreeze(FreezePlugin plugin) {
        super(plugin, "freeze");

        addSubCommand(new SubCommandFreeze(plugin));
        addSubCommand(new SubCommandHelp(plugin));
        addSubCommand(new SubCommandMelt(plugin));
        addSubCommand(new SubCommandReload(plugin));
    }
}
