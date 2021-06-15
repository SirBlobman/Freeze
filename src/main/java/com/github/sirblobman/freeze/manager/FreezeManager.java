package com.github.sirblobman.freeze.manager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.freeze.FreezePlugin;

public final class FreezeManager {
    private final FreezePlugin plugin;
    private final Set<UUID> frozenPlayerSet;

    public FreezeManager(FreezePlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.frozenPlayerSet = new HashSet<>();
    }

    public FreezePlugin getPlugin() {
        return this.plugin;
    }

    public void setFrozen(Player player, boolean freeze) {
        UUID uuid = player.getUniqueId();
        if(freeze) this.frozenPlayerSet.add(uuid);
        else this.frozenPlayerSet.remove(uuid);
    }

    public boolean isFrozen(Player player) {
        UUID uuid = player.getUniqueId();
        return this.frozenPlayerSet.contains(uuid);
    }

    public void removeAll() {
        this.frozenPlayerSet.clear();
    }
}
