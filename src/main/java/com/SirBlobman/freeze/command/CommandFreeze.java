package com.SirBlobman.freeze.command;

import com.SirBlobman.api.nms.NMS_Handler;
import com.SirBlobman.freeze.Freeze;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandFreeze implements CommandExecutor, Listener {
    private static final List<UUID> frozenList = new ArrayList<>();

    private final Freeze plugin;
    public CommandFreeze(Freeze plugin) {
        this.plugin = plugin;
    }

    private String color(String message) {
        if(message == null) return null;
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private String getConfigMessage(String path) {
        FileConfiguration config = this.plugin.getConfig();
        if(config.isList(path)) {
            List<String> messageList = config.getStringList(path);
            String[] messageArray = messageList.toArray(new String[0]);

            String message = String.join("\n", messageArray);
            return color(message);
        }

        if(config.isString(path)) {
            String message = config.getString(path);
            return color(message);
        }

        return path;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) return false;

        String targetName = args[0].toLowerCase();
        Player target = Bukkit.getPlayer(targetName);
        if(target == null) {
            String message = getConfigMessage("messages.invalid target").replace("{target}", targetName);
            sender.sendMessage(message);
            return true;
        }
        targetName = target.getName();

        UUID uuid = target.getUniqueId();
        if(frozenList.contains(uuid)) {
            frozenList.remove(uuid);
            String message = getConfigMessage("messages.un-freeze").replace("{target}", targetName);
            sender.sendMessage(message);

            sendUnfrozenMessage(target);
            return true;
        }

        frozenList.add(uuid);
        String message = getConfigMessage("messages.freeze").replace("{target}", targetName);
        sender.sendMessage(message);
        return true;
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if(!frozenList.contains(uuid)) return;

        e.setCancelled(true);
        sendFrozenMessage(player);
    }

    private void sendFrozenMessage(Player player) {
        String message = getConfigMessage("messages.action bar.frozen");
        NMS_Handler.getHandler().sendActionBar(player, message);
    }

    private void sendUnfrozenMessage(Player player) {
        String message = getConfigMessage("messages.action bar.melted");
        NMS_Handler.getHandler().sendActionBar(player, message);
    }
}