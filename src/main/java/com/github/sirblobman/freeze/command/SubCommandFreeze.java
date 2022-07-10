package com.github.sirblobman.freeze.command;

import com.github.sirblobman.freeze.FreezePlugin;

public final class SubCommandFreeze extends FreezeCommand {
    public SubCommandFreeze(FreezePlugin plugin) {
        super(plugin, "freeze");
        addSubCommand(new SubCommandFreezeAll(plugin));
        addSubCommand(new SubCommandFreezePlayer(plugin));
    }
}
