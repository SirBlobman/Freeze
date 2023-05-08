package com.github.sirblobman.freeze.menu;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.menu.AdvancedAbstractMenu;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.configuration.FakeInventoryConfiguration;
import com.github.sirblobman.freeze.configuration.FakeInventoryItem;
import com.github.sirblobman.freeze.configuration.FreezeConfiguration;
import com.github.sirblobman.freeze.manager.FreezeManager;
import com.github.sirblobman.api.shaded.adventure.text.Component;

public final class FakeMenu extends AdvancedAbstractMenu<FreezePlugin> {
    private Inventory inventory;

    public FakeMenu(@NotNull FreezePlugin plugin, @NotNull Player player) {
        super(plugin, player);
        this.inventory = null;
    }

    private @NotNull FreezeConfiguration getConfiguration() {
        FreezePlugin plugin = getPlugin();
        return plugin.getConfiguration();
    }

    private @NotNull FakeInventoryConfiguration getFakeInventoryConfiguration() {
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

    @Override
    public @NotNull Inventory getInventory() {
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
    protected void onValidClose(@NotNull InventoryCloseEvent e) {
        // Reopen menu if player is still frozen.
        Player player = getPlayer();
        FreezePlugin plugin = getPlugin();
        FreezeManager freezeManager = plugin.getFreezeManager();
        if (freezeManager.isFrozen(player)) {
            open();
        }
    }

    @Override
    protected void onValidClick(@NotNull InventoryClickEvent e) {
        // Prevent clicking on any items.
        e.setCancelled(true);
    }

    @Override
    protected void onValidDrag(@NotNull InventoryDragEvent e) {
        // Prevent dragging on any items.
        e.setCancelled(true);
    }
}
