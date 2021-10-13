package com.github.sirblobman.freeze.command;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public class CommandFreezeMeltAll extends FreezeCommand {
    public CommandFreezeMeltAll(FreezePlugin plugin) {
        super(plugin, "all");
    }
    
    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        if(!checkPermission(sender, "freeze.command.freeze.melt.all", true)) {
            return true;
        }
        
        int meltCount = meltAllCount();
        if(meltCount <= 0) {
            sendMessage(sender, "unfreeze-all-failure", null, true);
            return true;
        }
        
        String meltCountString = Integer.toString(meltCount);
        Replacer meltCountReplacer = getReplacer("{amount}", meltCountString);
        sendMessage(sender, "unfreeze-all", meltCountReplacer, true);
        return true;
    }
    
    private int meltAllCount() {
        int count = 0;
        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        for(Player player : onlinePlayerCollection) {
            if(melt(player)) {
                count++;
            }
        }
        
        return count;
    }
    
    private boolean melt(Player player) {
        FreezeManager freezeManager = getFreezeManager();
        if(freezeManager.isFrozen(player)) {
            freezeManager.setFrozen(player, false);
            return true;
        }
        
        return false;
    }
}
