package com.github.sirblobman.freeze.menu;

import java.util.Collection;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.freeze.configuration.FakeInventoryConfiguration;
import com.github.sirblobman.freeze.configuration.FakeItemConfiguration;
import com.github.sirblobman.freeze.FreezePlugin;

import net.kyori.adventure.text.Component;

public final class FakeMenu implements InventoryHolder {
    private final Inventory inventory;
    private final FreezePlugin plugin;
    private final Player player;

    public FakeMenu(@NotNull FreezePlugin plugin, @NotNull Player player) {
        this.plugin = plugin;
        this.player = player;

        FakeInventoryConfiguration configuration = plugin.getConfiguration().getFakeInventoryConfiguration();
        this.inventory = setupFakeInventory(configuration);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    private @NotNull Inventory setupFakeInventory(@NotNull FakeInventoryConfiguration configuration) {
        int size = configuration.getSize();
        Component title = configuration.getTitle();

        Inventory inventory;
        if (size == 5) {
            inventory = Bukkit.createInventory(this, InventoryType.HOPPER, title);
        } else {
            inventory = Bukkit.createInventory(this, size, title);
        }

        Map<String, FakeItemConfiguration> itemMap = configuration.getItems();
        Collection<FakeItemConfiguration> items = itemMap.values();
        for (FakeItemConfiguration item : items) {
            int slot = item.getSlot();
            if (slot < 0 || slot >= size) {
                continue;
            }

            ItemStack stack = item.buildItemStack();
            inventory.setItem(slot, stack);
        }

        return inventory;
    }
}
