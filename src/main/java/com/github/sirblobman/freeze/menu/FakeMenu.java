package com.github.sirblobman.freeze.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.menu.AdvancedAbstractMenu;
import com.github.sirblobman.api.utility.MessageUtility;
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
        if(fakeMenuManager.isEnabled()) {
            super.open();
        }
    }
    
    @NotNull
    @Override
    public Inventory getInventory() {
        FakeMenuManager fakeMenuManager = getFakeMenuManager();
        String menuTitle = fakeMenuManager.getMenuTitle();
        int menuSize = fakeMenuManager.getMenuSize();
        
        Inventory inventory;
        if(menuSize == 5) {
            String titleColored = MessageUtility.color(menuTitle);
            inventory = Bukkit.createInventory(this, InventoryType.HOPPER, titleColored);
        } else {
            inventory = getInventory(menuSize, menuTitle);
        }
        
        for(int slot = 0; slot < menuSize; slot++) {
            FakeMenuItem fakeMenuItem = fakeMenuManager.getItem(slot);
            if(fakeMenuItem != null) {
                ItemStack item = fakeMenuItem.getAsItemStack();
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
        if(freezeManager.isFrozen(player)) {
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
