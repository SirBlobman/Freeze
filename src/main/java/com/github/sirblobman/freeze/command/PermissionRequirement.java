package com.github.sirblobman.freeze.command;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public final class PermissionRequirement implements Predicate<CommandSourceStack> {
    private final String permissionName;

    public PermissionRequirement(@NotNull String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    public boolean test(CommandSourceStack context) {
        CommandSender sender = context.getSender();
        return sender.hasPermission(this.permissionName);
    }
}
