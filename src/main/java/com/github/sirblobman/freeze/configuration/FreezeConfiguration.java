package com.github.sirblobman.freeze.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import org.bukkit.configuration.ConfigurationSection;

import com.github.sirblobman.freeze.FreezePlugin;

public final class FreezeConfiguration {
    private final FreezePlugin plugin;
    private final FakeInventoryConfiguration fakeInventoryConfiguration;

    private boolean preventMovement;
    private boolean preventTeleport;
    private boolean preventCommands;
    private boolean preventBlockBreakAndPlace;
    private boolean preventDamage;
    private boolean preventAttacking;
    private boolean preventItemDropping;
    private boolean preventItemMovement;
    private boolean usePowderedSnowEffect;

    private final List<String> blockedCommandList;
    private final List<String> allowedCommandList;

    public FreezeConfiguration(@NotNull FreezePlugin plugin) {
        this.plugin = plugin;
        this.fakeInventoryConfiguration = new FakeInventoryConfiguration(plugin);
        this.blockedCommandList = new ArrayList<>();
        this.allowedCommandList = new ArrayList<>();
    }

    private @NotNull FreezePlugin getPlugin() {
        return this.plugin;
    }

    public void loadConfiguration(@NotNull ConfigurationSection config) {
        boolean preventMovement = config.getBoolean("prevent-movement", true);
        boolean preventTeleport = config.getBoolean("prevent-teleport", true);
        boolean preventCommands = config.getBoolean("prevent-commands", true);
        boolean preventBlockBreakAndPlace = config.getBoolean("prevent-block-break-and-place", true);
        boolean preventDamage = config.getBoolean("prevent-damage", true);
        boolean preventAttacking = config.getBoolean("prevent-attacking", true);
        boolean preventItemDropping = config.getBoolean("prevent-item-dropping", true);
        boolean preventItemMovement = config.getBoolean("prevent-item-movement", true);
        boolean usePowderedSnowEffect = config.getBoolean("use-powdered-snow-effect", true);
        setPreventMovement(preventMovement);
        setPreventTeleport(preventTeleport);
        setPreventCommands(preventCommands);
        setPreventBlockBreakAndPlace(preventBlockBreakAndPlace);
        setPreventDamage(preventDamage);
        setPreventAttacking(preventAttacking);
        setPreventItemDropping(preventItemDropping);
        setPreventItemMovement(preventItemMovement);
        setUsePowderedSnowEffect(usePowderedSnowEffect);

        List<String> blockedCommandList = config.getStringList("blocked-command-list");
        List<String> allowedCommandList = config.getStringList("allowed-command-list");
        setBlockedCommandList(blockedCommandList);
        setAllowedCommandList(allowedCommandList);

        ConfigurationSection fakeInventorySection = config.getConfigurationSection("fake-inventory");
        if (fakeInventorySection != null) {
            this.fakeInventoryConfiguration.loadConfiguration(fakeInventorySection);
        }
    }

    public @NotNull FakeInventoryConfiguration getFakeInventoryConfiguration() {
        return this.fakeInventoryConfiguration;
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

    public boolean isPreventBlockBreakAndPlace() {
        return this.preventBlockBreakAndPlace;
    }

    public void setPreventBlockBreakAndPlace(boolean preventBlockBreakAndPlace) {
        this.preventBlockBreakAndPlace = preventBlockBreakAndPlace;
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

    public boolean isPreventItemDropping() {
        return this.preventItemDropping;
    }

    public void setPreventItemDropping(boolean preventItemDropping) {
        this.preventItemDropping = preventItemDropping;
    }

    public boolean isPreventItemMovement() {
        return this.preventItemMovement;
    }

    public void setPreventItemMovement(boolean preventItemMovement) {
        this.preventItemMovement = preventItemMovement;
    }

    public boolean isUsePowderedSnowEffect() {
        return this.usePowderedSnowEffect;
    }

    public void setUsePowderedSnowEffect(boolean usePowderedSnowEffect) {
        this.usePowderedSnowEffect = usePowderedSnowEffect;
    }

    public @NotNull @UnmodifiableView List<String> getBlockedCommandList() {
        return Collections.unmodifiableList(this.blockedCommandList);
    }

    public void setBlockedCommandList(List<String> blockedCommandList) {
        this.blockedCommandList.clear();
        this.blockedCommandList.addAll(blockedCommandList);
    }

    public @NotNull @UnmodifiableView List<String> getAllowedCommandList() {
        return Collections.unmodifiableList(this.allowedCommandList);
    }

    public void setAllowedCommandList(List<String> allowedCommandList) {
        this.allowedCommandList.clear();
        this.allowedCommandList.addAll(allowedCommandList);
    }
}
