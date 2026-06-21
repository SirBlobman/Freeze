package com.github.sirblobman.freeze.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.sirblobman.freeze.FreezePlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class FakeItemConfiguration {
    private final String id;
    private final FreezePlugin plugin;

    private int slot;
    private String materialName;
    private int quantity;
    private String displayNameString;
    private List<String> loreStrings;
    private boolean glowing;

    private transient Material material;
    private transient Component displayName;
    private transient List<Component> lore;

    public FakeItemConfiguration(@NotNull FreezePlugin plugin, @NotNull String id) {
        this.plugin = plugin;
        this.id = id;

        this.slot = -1;
        this.materialName = Material.BARRIER.name();
        this.glowing = false;
    }

    public @NotNull FreezePlugin getPlugin() {
        return plugin;
    }

    private @NotNull MiniMessage getMiniMessage() {
        return getPlugin().getMiniMessage();
    }

    public @NotNull String getId() {
        return this.id;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public @NotNull String getMaterialName() {
        return this.materialName;
    }

    public void setMaterialName(@NotNull String materialName) {
        Material material = Material.matchMaterial(materialName, false);
        if (material == null) {
            throw new ItemBuildException("Material not found: " + materialName);
        }

        this.materialName = material.name();
        this.material = material;
    }

    public @NotNull Material getMaterial() {
        if (this.material == null) {
            throw new ItemBuildException("Material not initialized for item id '" + this.id + "'.");
        }

        return this.material;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public @Nullable String getDisplayNameString() {
        return displayNameString;
    }

    public void setDisplayNameString(@Nullable String displayNameString) {
        this.displayNameString = displayNameString;
        if (displayNameString == null) {
            this.displayName = null;
            return;
        }

        MiniMessage miniMessage = getMiniMessage();
        this.displayName = miniMessage.deserialize(displayNameString);
    }

    public @Nullable Component getDisplayName() {
        return this.displayName;
    }

    public @UnmodifiableView @Nullable List<String> getLoreStrings() {
        if (this.loreStrings == null) {
            return null;
        }

        return Collections.unmodifiableList(this.loreStrings);
    }

    public void setLoreStrings(@Nullable List<String> loreStrings) {
        if (loreStrings == null) {
            this.loreStrings = null;
            this.lore = null;
            return;
        }

        MiniMessage miniMessage = getMiniMessage();
        List<Component> lore = new ArrayList<>();
        for (String loreString : loreStrings) {
            Component line = miniMessage.deserialize(loreString);
            lore.add(line);
        }

        this.loreStrings = new ArrayList<>(loreStrings);
        this.lore = lore;
    }

    public @UnmodifiableView @Nullable List<Component> getLore() {
        if (this.lore == null) {
            return null;
        }

        return Collections.unmodifiableList(this.lore);
    }

    public boolean isGlowing() {
        return this.glowing;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    public @NotNull ItemStack buildItemStack() {
        Material material = getMaterial();
        int quantity = getQuantity();
        ItemStack stack = new ItemStack(material, quantity);
        ItemMeta meta = stack.getItemMeta();

        Component displayName = getDisplayName();
        if (displayName != null) {
            meta.displayName(displayName);
        }

        List<Component> lore = getLore();
        if (lore != null) {
            meta.lore(lore);
        }

        if (isGlowing()) {
            meta.setEnchantmentGlintOverride(true);
        }

        stack.setItemMeta(meta);
        return stack;
    }
}
