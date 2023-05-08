package com.github.sirblobman.freeze.configuration;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.configuration.IConfigurable;
import com.github.sirblobman.api.item.ItemBuilder;
import com.github.sirblobman.api.language.ComponentHelper;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.minimessage.MiniMessage;
import com.github.sirblobman.api.shaded.xseries.XMaterial;

public final class FakeInventoryItem implements IConfigurable {
    private final FreezePlugin plugin;
    private final String id;

    private ItemBuilder builder;
    private int slot;

    public FakeInventoryItem(@NotNull FreezePlugin plugin, @NotNull String id) {
        this.plugin = plugin;
        this.id = id;
        this.builder = new ItemBuilder(XMaterial.BARRIER);
        this.slot = 0;
    }

    @Override
    public void load(ConfigurationSection section) {
        setSlot(section.getInt("slot", 0));

        String materialName = section.getString("material");
        if (materialName == null) {
            materialName = XMaterial.BARRIER.name();
        }

        Optional<XMaterial> optionalMaterial = XMaterial.matchXMaterial(materialName);
        XMaterial material = optionalMaterial.orElse(XMaterial.BARRIER);
        this.builder = new ItemBuilder(material);

        int quantity = section.getInt("quantity", 1);
        this.builder.withAmount(quantity);

        MiniMessage miniMessage = getMiniMessage();
        ItemHandler itemHandler = getItemHandler();

        if (section.isString("display-name")) {
            String displayNameString = section.getString("display-name");
            if (displayNameString != null) {
                Component displayName = miniMessage.deserialize(displayNameString);
                this.builder.withName(itemHandler, ComponentHelper.wrapNoItalics(displayName));
            }
        }

        if (section.isList("lore")) {
            List<String> loreStringList = section.getStringList("lore");
            if (!loreStringList.isEmpty()) {
                List<Component> loreList = loreStringList.stream().map(miniMessage::deserialize)
                        .collect(Collectors.toList());
                this.builder.withLore(itemHandler, ComponentHelper.wrapNoItalics(loreList));
            }
        }

        boolean glowing = section.getBoolean("glowing", false);
        if (glowing) {
            this.builder.withGlowing();
        }

        if (section.isInt("custom-model-data")) {
            int modelData = section.getInt("custom-model-data");
            this.builder.withModel(modelData);
        }
    }

    private @NotNull FreezePlugin getPlugin() {
        return this.plugin;
    }

    private @NotNull MultiVersionHandler getMultiVersionHandler() {
        FreezePlugin plugin = getPlugin();
        return plugin.getMultiVersionHandler();
    }

    private @NotNull ItemHandler getItemHandler() {
        MultiVersionHandler multiVersionHandler = getMultiVersionHandler();
        return multiVersionHandler.getItemHandler();
    }

    private @NotNull LanguageManager getLanguageManager() {
        FreezePlugin plugin = getPlugin();
        return plugin.getLanguageManager();
    }

    private @NotNull MiniMessage getMiniMessage() {
        LanguageManager languageManager = getLanguageManager();
        return languageManager.getMiniMessage();
    }

    public @NotNull String getId() {
        return this.id;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public @NotNull ItemStack getItem() {
        return this.builder.build();
    }
}
