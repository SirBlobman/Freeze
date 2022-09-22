package com.github.sirblobman.freeze.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.adventure.adventure.text.minimessage.MiniMessage;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.menu.AdvancedAbstractMenu;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FakeMenuManager;
import com.github.sirblobman.freeze.manager.FreezeManager;

import org.jetbrains.annotations.NotNull;

public final class FakeMenu extends AdvancedAbstractMenu<FreezePlugin> {
    public FakeMenu(FreezePlugin plugin, Player player) {
        super(plugin, player);
    }

    private FakeMenuManager getFakeMenuManager() {
        FreezePlugin plugin = getPlugin();
        return plugin.getFakeMenuManager();
    }

    @Override
    public void open() {
        FakeMenuManager fakeMenuManager = getFakeMenuManager();
        if (fakeMenuManager.isEnabled()) {
            super.open();
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        FakeMenuManager fakeMenuManager = getFakeMenuManager();
        String titleString = fakeMenuManager.getMenuTitle();
        int menuSize = fakeMenuManager.getMenuSize();

        FreezePlugin plugin = getPlugin();
        LanguageManager languageManager = plugin.getLanguageManager();
        MiniMessage miniMessage = languageManager.getMiniMessage();
        Component title = miniMessage.deserialize(titleString);
        Inventory inventory = getInventory(menuSize, title);

        for (int slot = 0; slot < menuSize; slot++) {
            FakeMenuItem fakeMenuItem = fakeMenuManager.getItem(slot);
            if (fakeMenuItem != null) {
                ItemStack item = fakeMenuItem.getAsItemStack(plugin);
                inventory.setItem(slot, item);
            }
        }

        return inventory;
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
