package com.github.sirblobman.freeze.command;

import com.github.sirblobman.freeze.FreezePlugin;

public final class CommandFreezeMelt extends FreezeCommand {
    public CommandFreezeMelt(FreezePlugin plugin) {
        super(plugin, "melt");
        addSubCommand(new CommandFreezeMeltAll(plugin));
        addSubCommand(new CommandFreezeMeltPlayer(plugin));
    }
}
