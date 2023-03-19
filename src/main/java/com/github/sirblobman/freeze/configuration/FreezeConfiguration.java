package com.github.sirblobman.freeze.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import com.github.sirblobman.api.configuration.IConfigurable;
import com.github.sirblobman.freeze.FreezePlugin;

public final class FreezeConfiguration implements IConfigurable {
    private boolean preventMovement;
    private boolean preventTeleport;
    private boolean preventCommands;
    private boolean preventBlockActions;
    private boolean preventDamage;
    private boolean preventAttacking;

    private final List<String> blockedCommandList;
    private final List<String> allowedCommandList;
    private final FakeInventoryConfiguration fakeInventoryConfiguration;

    public FreezeConfiguration(FreezePlugin plugin) {
        this.preventMovement = true;
        this.preventTeleport = true;
        this.preventCommands = true;
        this.preventBlockActions = true;
        this.preventDamage = true;
        this.preventAttacking = true;

        this.blockedCommandList = new ArrayList<>();
        this.allowedCommandList = new ArrayList<>();

        this.fakeInventoryConfiguration = new FakeInventoryConfiguration(plugin);
    }

    @Override
    public void load(ConfigurationSection config) {
        setPreventMovement(config.getBoolean("prevent-moving", true));
        setPreventTeleport(config.getBoolean("prevent-teleport", true));
        setPreventCommands(config.getBoolean("prevent-commands", true));
        setPreventBlockActions(config.getBoolean("prevent-block-break-and-place", true));
        setPreventDamage(config.getBoolean("prevent-damage", true));
        setPreventAttacking(config.getBoolean("prevent-attacking", true));

        setBlockedCommandList(config.getStringList("blocked-command-list"));
        setAllowedCommandList(config.getStringList("allowed-command-list"));

        ConfigurationSection fakeInventorySection = getOrCreateSection(config, "fake-inventory");
        FakeInventoryConfiguration fakeInventoryConfiguration = getFakeInventoryConfiguration();
        fakeInventoryConfiguration.load(fakeInventorySection);
    }

    public boolean isPreventMovement() {
        return this.preventMovement;
    }

    public void setPreventMovement(boolean preventMovement) {
        this.preventMovement = preventMovement;
    }

    public boolean isPreventTeleport() {
        return this.preventTeleport;
    }

    public void setPreventTeleport(boolean preventTeleport) {
        this.preventTeleport = preventTeleport;
    }

    public boolean isPreventCommands() {
        return this.preventCommands;
    }

    public void setPreventCommands(boolean preventCommands) {
        this.preventCommands = preventCommands;
    }

    public boolean isPreventBlockActions() {
        return this.preventBlockActions;
    }

    public void setPreventBlockActions(boolean preventBlockActions) {
        this.preventBlockActions = preventBlockActions;
    }

    public boolean isPreventDamage() {
        return this.preventDamage;
    }

    public void setPreventDamage(boolean preventDamage) {
        this.preventDamage = preventDamage;
    }

    public boolean isPreventAttacking() {
        return this.preventAttacking;
    }

    public void setPreventAttacking(boolean preventAttacking) {
        this.preventAttacking = preventAttacking;
    }

    public List<String> getBlockedCommandList() {
        return Collections.unmodifiableList(this.blockedCommandList);
    }

    public void setBlockedCommandList(List<String> blockedCommandList) {
        this.blockedCommandList.clear();
        this.blockedCommandList.addAll(blockedCommandList);
    }

    public List<String> getAllowedCommandList() {
        return Collections.unmodifiableList(this.allowedCommandList);
    }

    public void setAllowedCommandList(List<String> allowedCommandList) {
        this.allowedCommandList.clear();
        this.allowedCommandList.addAll(allowedCommandList);
    }

    public FakeInventoryConfiguration getFakeInventoryConfiguration() {
        return this.fakeInventoryConfiguration;
    }
}
