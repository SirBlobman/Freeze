package com.github.sirblobman.freeze.command;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public class SubCommandFreezeAll extends FreezeCommand {
    public SubCommandFreezeAll(FreezePlugin plugin) {
        super(plugin, "all");
        setPermissionName("freeze.command.freeze.freeze.all");
    }
    
    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        int freezeCount = freezeAllCount();
        if(freezeCount <= 0) {
            sendMessage(sender, "freeze-all-failure", null);
            return true;
        }
        
        String freezeCountString = Integer.toString(freezeCount);
        Replacer freezeCountReplacer = getReplacer("{amount}", freezeCountString);
        sendMessage(sender, "freeze-all", freezeCountReplacer);
        return true;
    }
    
    private int freezeAllCount() {
        int count = 0;
        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        for(Player player : onlinePlayerCollection) {
            if(freeze(player)) {
                count++;
            }
        }
        
        return count;
    }
    
    private boolean freeze(Player player) {
        if(player.hasPermission("freeze.immune")) {
            return false;
        }
        
        FreezeManager freezeManager = getFreezeManager();
        if(freezeManager.isFrozen(player)) {
            return true;
        }
        
        freezeManager.setFrozen(player, true);
        return true;
    }
}
