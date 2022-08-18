package com.github.sirblobman.freeze.command;

import com.github.sirblobman.freeze.FreezePlugin;

public final class SubCommandMelt extends FreezeCommand {
    public SubCommandMelt(FreezePlugin plugin) {
        super(plugin, "melt");
        setPermissionName("freeze.command.freeze.melt");
        addSubCommand(new SubCommandMeltAll(plugin));
        addSubCommand(new SubCommandMeltPlayer(plugin));
    }
}
