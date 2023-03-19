package com.github.sirblobman.freeze.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.adventure.adventure.text.format.NamedTextColor;
import com.github.sirblobman.api.adventure.adventure.text.minimessage.MiniMessage;
import com.github.sirblobman.api.configuration.IConfigurable;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.freeze.FreezePlugin;

import org.jetbrains.annotations.Nullable;

public final class FakeInventoryConfiguration implements IConfigurable {
    private final FreezePlugin plugin;

    private boolean enabled;
    private Component title;
    private int size;

    private Map<Integer, FakeInventoryItem> slotMap;

    public FakeInventoryConfiguration(FreezePlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");

        this.enabled = true;
        this.title = Component.text("You are frozen...", NamedTextColor.AQUA);
        this.size = 5;

        this.slotMap = new HashMap<>();
    }

    @Override
    public void load(ConfigurationSection section) {
        setEnabled(section.getBoolean("enabled", false));
        setSize(section.getInt("size", 5));

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
        Set<String> keySet = itemsSection.getKeys(false);
        this.slotMap.clear();

        for (String itemId : keySet) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(itemId);
            if (itemSection == null) {
                continue;
            }

            FakeInventoryItem fakeInventoryItem = new FakeInventoryItem(plugin, itemId);
            fakeInventoryItem.load(itemSection);

            int slot = fakeInventoryItem.getSlot();
            if (this.slotMap.containsKey(slot)) {
                Logger logger = plugin.getLogger();
                logger.warning("Slot '" + slot + "' is duplicated in fake inventory menu.");
                logger.warning("'" + itemId + "' will override previous item '"
                        + this.slotMap.get(slot).getId() + "'.");
            }

            this.slotMap.put(slot, fakeInventoryItem);
        }
    }

    private FreezePlugin getPlugin() {
        return this.plugin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Component getTitle() {
        return title;
    }

    public void setTitle(Component title) {
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Nullable
    public FakeInventoryItem getItem(int slot) {
        return this.slotMap.get(slot);
    }
}
