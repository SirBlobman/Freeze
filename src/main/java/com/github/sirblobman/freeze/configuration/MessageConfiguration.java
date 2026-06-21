package com.github.sirblobman.freeze.configuration;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.ConfigurationSection;

import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.message.Replacer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class MessageConfiguration {
    private final FreezePlugin plugin;
    private final Map<String, String> messageStringMap;
    private final transient Map<String, Component> messageComponentMap;

    public MessageConfiguration(@NotNull FreezePlugin plugin) {
        this.plugin = plugin;
        this.messageStringMap = new HashMap<>();
        this.messageComponentMap = new HashMap<>();
    }

    private @NotNull FreezePlugin getPlugin() {
        return this.plugin;
    }

    private @NotNull MiniMessage getMiniMessage() {
        return getPlugin().getMiniMessage();
    }

    public void loadMessages(@NotNull ConfigurationSection config) {
        this.messageStringMap.clear();
        this.messageComponentMap.clear();

        MiniMessage miniMessage = getMiniMessage();
        Set<String> messagePathSet = config.getKeys(true);
        for (String messagePath : messagePathSet) {
            if (config.isString(messagePath)) {
                String messageString = config.getString(messagePath);
                if (messageString == null || messageString.isEmpty()) {
                    this.messageStringMap.put(messagePath, "");
                    this.messageComponentMap.put(messagePath, Component.empty());
                } else {
                    this.messageStringMap.put(messagePath, messageString);
                    Component message = miniMessage.deserialize(messageString);
                    this.messageComponentMap.put(messagePath, message);
                }
            }
        }
    }

    public @Nullable String getMessageString(@NotNull String path) {
        String messageString = this.messageStringMap.get(path);
        if (messageString == null) {
            return String.format(Locale.US, "{%s}", path);
        }

        if (messageString.isEmpty()) {
            return null;
        }

        return messageString;
    }

    public @Nullable Component getMessageComponent(@NotNull String path, Replacer @NotNull... replacers) {
        Component message = this.messageComponentMap.get(path);
        if (message == null) {
            String format = String.format(Locale.US, "{%s}", path);
            return Component.text(format);
        }

        if (Component.empty().equals(message)) {
            return null;
        }

        for(Replacer replacer : replacers) {
            TextReplacementConfig.Builder replacementBuilder = TextReplacementConfig.builder();
            replacementBuilder.matchLiteral(replacer.getSource());
            replacementBuilder.replacement(replacer.getReplacement());
            message = message.replaceText(replacementBuilder.build());
        }

        return message;
    }

    public void sendMessage(@NotNull Audience audience, @NotNull String path, Replacer @NotNull... replacers) {
        Component message = getMessageComponent(path, replacers);
        if (message == null) {
            return;
        }

        audience.sendMessage(message);
    }

    public void sendActionBar(@NotNull Audience audience, @NotNull String path, Replacer @NotNull... replacers) {
        Component message = getMessageComponent(path, replacers);
        if (message == null) {
            return;
        }

        audience.sendActionBar(message);
    }
}
