package com.github.sirblobman.freeze.command;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public abstract class FreezeCommand extends Command {
    private final FreezePlugin plugin;

    public FreezeCommand(@NotNull FreezePlugin plugin, @NotNull String commandName) {
        super(plugin, commandName);
        this.plugin = plugin;
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull CommandSender sender, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        return false;
    }

    protected final @NotNull FreezePlugin getFreezePlugin() {
        return this.plugin;
    }

    protected final @NotNull FreezeManager getFreezeManager() {
        FreezePlugin plugin = getFreezePlugin();
        return plugin.getFreezeManager();
    }

    protected final boolean isImmune(@NotNull Player player) {
        return player.hasPermission("freeze.immune");
    }
}
