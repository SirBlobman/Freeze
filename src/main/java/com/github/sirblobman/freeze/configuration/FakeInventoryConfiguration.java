package com.github.sirblobman.freeze.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.ConfigurationSection;

import com.github.sirblobman.api.configuration.IConfigurable;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.format.NamedTextColor;
import com.github.sirblobman.api.shaded.adventure.text.minimessage.MiniMessage;

public final class FakeInventoryConfiguration implements IConfigurable {
    private final FreezePlugin plugin;
    private final Map<Integer, FakeInventoryItem> slotMap;

    private boolean enabled;
    private Component title;
    private int size;


    public FakeInventoryConfiguration(@NotNull FreezePlugin plugin) {
        this.plugin = plugin;
        this.slotMap = new HashMap<>();

        this.enabled = true;
        this.title = Component.text("You are frozen...", NamedTextColor.AQUA);
        this.size = 5;

    }

    @Override
    public void load(@NotNull ConfigurationSection section) {
        setEnabled(section.getBoolean("enabled", false));
        setSize(section.getInt("size", 5));

        loadTitle(section.getString("title"));
        loadItems(getOrCreateSection(section, "items"));

        FreezePlugin plugin = getPlugin();
        LanguageManager languageManager = plugin.getLanguageManager();
        MiniMessage miniMessage = languageManager.getMiniMessage();

        String titleString = section.getString("title");
        if (titleString == null) {
            titleString = "";
        }

        Component title = miniMessage.deserialize(titleString);
        setTitle(title);

        ConfigurationSection itemsSection = getOrCreateSection(section, "items");
        loadItems(itemsSection);
    }

    private @NotNull FreezePlugin getPlugin() {
        return this.plugin;
    }

    private @NotNull Logger getLogger() {
        FreezePlugin plugin = getPlugin();
        return plugin.getLogger();
    }

    private @NotNull LanguageManager getLanguageManager() {
        FreezePlugin plugin = getPlugin();
        return plugin.getLanguageManager();
    }

    private @NotNull MiniMessage getMiniMessage() {
        LanguageManager languageManager = getLanguageManager();
        return languageManager.getMiniMessage();
    }

    private void loadTitle(@Nullable String title) {
        if (title == null) {
            setTitle(Component.empty());
            return;
        }

        MiniMessage miniMessage = getMiniMessage();
        setTitle(miniMessage.deserialize(title));
    }

    private void loadItems(@NotNull ConfigurationSection itemsSection) {
        this.slotMap.clear();
        Set<String> keySet = itemsSection.getKeys(false);

        for (String itemId : keySet) {
            ConfigurationSection section = itemsSection.getConfigurationSection(itemId);
            if (section == null) {
                continue;
            }

            loadItem(itemId, section);
        }
    }

    private void loadItem(@NotNull String itemId, @NotNull ConfigurationSection section) {
        FreezePlugin plugin = getPlugin();
        FakeInventoryItem item = new FakeInventoryItem(plugin, itemId);
        item.load(section);

        int slot = item.getSlot();
        if (this.slotMap.containsKey(slot)) {
            FakeInventoryItem oldItem = this.slotMap.get(slot);
            String oldItemId = oldItem.getId();

            Logger logger = getLogger();
            logger.warning("Slot '" + slot + "' is duplicated in fake inventory menu.");
            logger.warning("'" + itemId + "' will override previous item '" + oldItemId + "'.");
        }

        this.slotMap.put(slot, item);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public @NotNull Component getTitle() {
        return title;
    }

    public void setTitle(@NotNull Component title) {
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public @Nullable FakeInventoryItem getItem(int slot) {
        return this.slotMap.get(slot);
    }
}
