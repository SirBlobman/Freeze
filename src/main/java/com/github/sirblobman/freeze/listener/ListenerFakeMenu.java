package com.github.sirblobman.freeze.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.configuration.FakeInventoryConfiguration;
import com.github.sirblobman.freeze.configuration.FreezeConfiguration;
import com.github.sirblobman.freeze.event.PlayerFreezeEvent;
import com.github.sirblobman.freeze.event.PlayerMeltEvent;
import com.github.sirblobman.freeze.menu.FakeMenu;

public final class ListenerFakeMenu extends FreezeListener {
    public ListenerFakeMenu(FreezePlugin plugin) {
        super(plugin);
    }

    private FakeInventoryConfiguration getFakeInventoryConfiguration() {
        FreezeConfiguration configuration = getConfiguration();
        return configuration.getFakeInventoryConfiguration();
    }

    @Override
    protected boolean isDisabled() {
        FakeInventoryConfiguration configuration = getFakeInventoryConfiguration();
        return !configuration.isEnabled();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFreeze(PlayerFreezeEvent e) {
        if (isDisabled()) {
            return;
        }

        Player player = e.getPlayer();
        FreezePlugin plugin = getPlugin();
        new FakeMenu(plugin, player).open();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMelt(PlayerMeltEvent e) {
        if (isDisabled()) {
            return;
        }

        Player player = e.getPlayer();
        FreezePlugin freezePlugin = getPlugin();
        freezePlugin.closeFakeMenu(player);
    }
}
