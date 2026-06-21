package com.github.sirblobman.freeze.listener;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import com.github.sirblobman.freeze.configuration.FreezeConfiguration;
import com.github.sirblobman.freeze.FreezeManager;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.event.PlayerFreezeEvent;
import com.github.sirblobman.freeze.event.PlayerMeltEvent;
import com.github.sirblobman.freeze.configuration.FakeInventoryConfiguration;
import com.github.sirblobman.freeze.menu.FakeMenu;

public final class ListenerFakeMenu extends FreezeListener {
    public ListenerFakeMenu(@NotNull FreezePlugin plugin) {
        super(plugin);
    }

    @Override
    protected boolean isDisabled() {
        FreezeConfiguration configuration = getConfiguration();
        FakeInventoryConfiguration fakeInventoryConfiguration = configuration.getFakeInventoryConfiguration();
        return !fakeInventoryConfiguration.isEnabled();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFreeze(PlayerFreezeEvent event) {
        if (isDisabled()) {
            return;
        }

        Player player = event.getPlayer();
        openFreezeMenu(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMelt(@NotNull PlayerMeltEvent event) {
        if (isDisabled()) {
            return;
        }

        Player player = event.getPlayer();
        getPlugin().closeFakeMenu(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(@NotNull InventoryClickEvent event) {
        if (isDisabled()) {
            return;
        }

        HumanEntity clicker = event.getWhoClicked();
        if (!(clicker instanceof Player player)) {
            return;
        }

        FreezeManager freezeManager = getFreezeManager();
        if (!freezeManager.isFrozen(player)) {
            return;
        }

        event.setResult(Event.Result.DENY);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrag(@NotNull InventoryDragEvent event) {
        if (isDisabled()) {
            return;
        }

        HumanEntity clicker = event.getWhoClicked();
        if (!(clicker instanceof Player player)) {
            return;
        }

        FreezeManager freezeManager = getFreezeManager();
        if (!freezeManager.isFrozen(player)) {
            return;
        }

        event.setResult(Event.Result.DENY);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClose(@NotNull InventoryCloseEvent event) {
        if (isDisabled()) {
            return;
        }

        HumanEntity closer = event.getPlayer();
        if (!(closer instanceof Player player)) {
            return;
        }

        FreezeManager freezeManager = getFreezeManager();
        if (!freezeManager.isFrozen(player)) {
            return;
        }

        openFreezeMenu(player);
    }

    private void openFreezeMenu(@NotNull Player player) {
        FreezePlugin plugin = getPlugin();
        FakeMenu menu = new FakeMenu(plugin, player);
        Inventory inventory = menu.getInventory();
        player.openInventory(inventory);
    }
}
