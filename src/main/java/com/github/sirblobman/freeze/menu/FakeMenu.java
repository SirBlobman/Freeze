package com.github.sirblobman.freeze.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.menu.AdvancedAbstractMenu;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.configuration.FakeInventoryConfiguration;
import com.github.sirblobman.freeze.configuration.FakeInventoryItem;
import com.github.sirblobman.freeze.configuration.FreezeConfiguration;
import com.github.sirblobman.freeze.manager.FreezeManager;

import org.jetbrains.annotations.NotNull;

public final class FakeMenu extends AdvancedAbstractMenu<FreezePlugin> {
    private Inventory inventory;

    public FakeMenu(FreezePlugin plugin, Player player) {
        super(plugin, player);
        this.inventory = null;
    }

    private FreezeConfiguration getConfiguration() {
        FreezePlugin plugin = getPlugin();
        return plugin.getConfiguration();
    }

    private FakeInventoryConfiguration getFakeInventoryConfiguration() {
        FreezeConfiguration configuration = getConfiguration();
        return configuration.getFakeInventoryConfiguration();
    }

    @Override
    public void open() {
        FakeInventoryConfiguration configuration = getFakeInventoryConfiguration();
        if (configuration.isEnabled()) {
            super.open();
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        if (this.inventory != null) {
            return this.inventory;
        }

        FakeInventoryConfiguration configuration = getFakeInventoryConfiguration();
        Component title = configuration.getTitle();
        int size = configuration.getSize();

        Inventory inventory = getInventory(size, title);
        for (int slot = 0; slot < size; slot++) {
            FakeInventoryItem fakeItem = configuration.getItem(slot);
            if (fakeItem == null) {
                continue;
            }

            ItemStack item = fakeItem.getItem();
            inventory.setItem(slot, item);
        }

        return (this.inventory = inventory);
    }

    @Override
    protected void onValidClose(InventoryCloseEvent e) {
        // Reopen menu if player is still frozen.
        Player player = getPlayer();
        FreezePlugin plugin = getPlugin();
        FreezeManager freezeManager = plugin.getFreezeManager();
        if (freezeManager.isFrozen(player)) {
            open();
        }
    }

    @Override
    protected void onValidClick(InventoryClickEvent e) {
        // Prevent clicking on any items.
        e.setCancelled(true);
    }

    @Override
    protected void onValidDrag(InventoryDragEvent e) {
        // Prevent dragging on any items.
        e.setCancelled(true);
    }
}
