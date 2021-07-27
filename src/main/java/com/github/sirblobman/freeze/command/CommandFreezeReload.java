package com.github.sirblobman.freeze.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.freeze.FreezePlugin;

import org.jetbrains.annotations.NotNull;

public final class CommandFreezeReload extends Command {
    private final FreezePlugin plugin;

    public CommandFreezeReload(FreezePlugin plugin) {
        super(plugin, "freeze-reload");
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public LanguageManager getLanguageManager() {
        return this.plugin.getLanguageManager();
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        ConfigurationManager configurationManager = this.plugin.getConfigurationManager();
        configurationManager.reload("config.yml");
        configurationManager.reload("language.yml");

        LanguageManager languageManager = getLanguageManager();
        languageManager.reloadLanguages();

        sendMessageOrDefault(sender, "reload-success", "", null, true);
        return true;
    }
}
