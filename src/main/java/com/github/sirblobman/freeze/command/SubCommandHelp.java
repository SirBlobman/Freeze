package com.github.sirblobman.freeze.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.freeze.FreezePlugin;

public final class SubCommandHelp extends FreezeCommand {
    public SubCommandHelp(FreezePlugin plugin) {
        super(plugin, "help");
        setPermissionName("freeze.command.freeze.help");
    }
    
    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        List<Component> messageList = new ArrayList<>();
        addMessage(sender, messageList, "help");
        addMessage(sender, messageList, "reload");
        addMessage(sender, messageList, "freeze.player");
        addMessage(sender, messageList, "freeze.all");
        addMessage(sender, messageList, "melt.player");
        addMessage(sender, messageList, "melt.all");
        
        if(messageList.isEmpty()) {
            return true;
        }
        
        sendMessage(sender, "help.title", null);

        LanguageManager languageManager = getLanguageManager();
        for(Component message : messageList) {
            languageManager.sendMessage(sender, message);
        }
        
        sender.sendMessage("");
        return true;
    }
    
    private void addMessage(CommandSender sender, List<Component> messageList, String key) {
        String permissionName = ("freeze.command.freeze." + key);
        if(!checkPermission(sender, permissionName, false)) {
            return;
        }
        
        String messagePath = ("help." + key);
        LanguageManager languageManager = getLanguageManager();
        Component message = languageManager.getMessage(sender, messagePath, null);
        if(!Component.empty().equals(message)) {
            messageList.add(message);
        }
    }
}
