package com.github.sirblobman.freeze.manager;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.menu.FakeMenuItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FakeMenuManager {
    private final FreezePlugin plugin;
    private final Map<Integer, FakeMenuItem> itemMap;

    private boolean enabled;
    private int menuSize;
    private String menuTitle;

    public FakeMenuManager(FreezePlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.itemMap = new ConcurrentHashMap<>();

        this.enabled = false;
        this.menuSize = 5;
        this.menuTitle = "&bYou are frozen...";
    }

    public FreezePlugin getPlugin() {
        return this.plugin;
    }

    public Logger getLogger() {
        FreezePlugin plugin = getPlugin();
        return plugin.getLogger();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMenuSize() {
        return this.menuSize;
    }

    public void setMenuSize(int menuSize) {
        if (menuSize != 5) {
            if (menuSize == 0 || menuSize % 9 != 0) {
                String errorMessage = "menuSize must be one of the following values: 5,9,18,27,36,45,54";
                throw new IllegalArgumentException(errorMessage);
            }
        }

        this.menuSize = menuSize;
    }

    @NotNull
    public String getMenuTitle() {
        return this.menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        Validate.notEmpty(menuTitle, "menuTitle must not be empty.");
        this.menuTitle = menuTitle;
    }

    public Map<Integer, FakeMenuItem> getItems() {
        return Collections.unmodifiableMap(this.itemMap);
    }

    @Nullable
    public FakeMenuItem getItem(int slot) {
        Map<Integer, FakeMenuItem> items = getItems();
        return items.get(slot);
    }

    public void addItem(int slot, FakeMenuItem item) {
        Validate.notNull(item, "item must not be null!");

        int menuSize = getMenuSize();
        if (slot < 0 || slot >= menuSize) {
            throw new IllegalArgumentException("slot must be between 0 and the menu size (" + menuSize + ")");
        }

        this.itemMap.put(slot, item);
    }

    public void removeItem(int slot) {
        this.itemMap.remove(slot);
    }

    public void reload() {
        FreezePlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");

        ConfigurationSection fakeInventorySection = configuration.getConfigurationSection("fake-inventory");
        if (fakeInventorySection == null) {
            setEnabled(false);
            return;
        }

        try {
            boolean enabled = fakeInventorySection.getBoolean("enabled", false);
            setEnabled(enabled);

            String menuTitle = fakeInventorySection.getString("title", "&bYou are frozen...");
            setMenuTitle(menuTitle);

            int menuSize = fakeInventorySection.getInt("size", 5);
            setMenuSize(menuSize);

            ConfigurationSection itemsSection = fakeInventorySection.getConfigurationSection("items");
            if (itemsSection != null) {
                reloadItems(itemsSection);
            }
        } catch (Exception ex) {
            setEnabled(false);
            getLogger().log(Level.WARNING, "Failed to load the fake inventory settings:", ex);
        }
    }

    private void reloadItems(ConfigurationSection itemsSection) {
        Validate.notNull(itemsSection, "itemsSection must not be null!");
        Logger logger = getLogger();
        this.itemMap.clear();

        Set<String> itemIdSet = itemsSection.getKeys(false);
        for (String itemId : itemIdSet) {
            logger.info("Loading fake menu item '" + itemId + "'...");
            ConfigurationSection section = itemsSection.getConfigurationSection(itemId);
            if (section == null) {
                logger.info("Invalid section '" + itemId + "'.");
                continue;
            }

            try {
                FakeMenuItem fakeMenuItem = new FakeMenuItem();
                fakeMenuItem.load(section);

                int slot = fakeMenuItem.getSlot();
                if (this.itemMap.containsKey(slot)) {
                    logger.warning("Item id '" + itemId + "' has the same slot as a previous item.");
                }

                addItem(slot, fakeMenuItem);
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Failed to load item '" + itemId + "':", ex);
            }
        }

        int itemMapSize = this.itemMap.size();
        logger.info("Successfully loaded " + itemMapSize + " fake items for the fake menu.");
    }
}
