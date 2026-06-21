package com.github.sirblobman.freeze.menu;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import com.github.sirblobman.freeze.FreezePlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class FakeInventoryConfiguration {
    private boolean enabled;
    private String titleString;
    private int size;

    private final FreezePlugin plugin;
    private final Map<String, FakeItemConfiguration> items;

    private transient Component title;

    public FakeInventoryConfiguration(@NotNull FreezePlugin plugin) {
        this.plugin = plugin;
        this.items = new HashMap<>();
        this.enabled = false;
        this.size = 5;
        setTitleString("<aqua>You are frozen...</aqua>");
    }

    private @NotNull FreezePlugin getPlugin() {
        return this.plugin;
    }

    private @NotNull MiniMessage getMiniMessage() {
        return getPlugin().getMiniMessage();
    }

    public void loadConfiguration(@NotNull ConfigurationSection config) {
        boolean enabled = config.getBoolean("enabled", false);
        String titleString = config.getString("title", "<aqua>You are frozen...</aqua>");
        int size = config.getInt("size", 5);
        setEnabled(enabled);
        setTitleString(titleString);
        setSize(size);

        ConfigurationSection itemsSection = config.getConfigurationSection("items");
        if (itemsSection != null) {
            loadItems(itemsSection);
        }
    }

    private void loadItems(@NotNull ConfigurationSection config) {
        Set<String> itemIdSet = config.getKeys(false);
        Map<String, FakeItemConfiguration> items = new HashMap<>();
        for (String itemId : itemIdSet) {
            ConfigurationSection section = config.getConfigurationSection(itemId);
            if (section != null) {
                FakeItemConfiguration itemConfiguration = loadItem(itemId, section);
                if (itemConfiguration != null) {
                    items.put(itemId, itemConfiguration);
                }
            }
        }

        setItems(items);
    }

    private @Nullable FakeItemConfiguration loadItem(@NotNull String itemId, @NotNull ConfigurationSection section) {
        String materialName = section.getString("material", Material.BARRIER.name());
        int quantity = section.getInt("quantity", 1);
        int slot = section.getInt("slot", -1);
        String displayName = section.getString("display-name");
        boolean glowing = section.getBoolean("glowing", false);

        List<String> lore = null;
        if (section.isSet("lore")) {
            lore = section.getStringList("lore");
        }

        try {
            FreezePlugin plugin = getPlugin();
            FakeItemConfiguration item = new FakeItemConfiguration(plugin, itemId);
            item.setMaterialName(materialName);
            item.setQuantity(quantity);
            item.setSlot(slot);
            item.setDisplayNameString(displayName);
            item.setGlowing(glowing);
            item.setLoreStrings(lore);
            return item;
        } catch (RuntimeException ex) {
            return null;
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public @NotNull String getTitleString() {
        return titleString;
    }

    public void setTitleString(@NotNull String titleString) {
        this.titleString = titleString;
        this.title = getMiniMessage().deserialize(titleString);
    }

    public @NotNull Component getTitle() {
        return title;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public @UnmodifiableView @NotNull Map<String, FakeItemConfiguration> getItems() {
        return Collections.unmodifiableMap(this.items);
    }

    public void setItems(@NotNull Map<String, FakeItemConfiguration> items) {
        this.items.clear();
        this.items.putAll(items);
    }

    public @Nullable FakeItemConfiguration getItem(@NotNull String id) {
        return this.items.get(id);
    }
}
