package com.github.sirblobman.freeze.configuration;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.adventure.adventure.text.minimessage.MiniMessage;
import com.github.sirblobman.api.configuration.IConfigurable;
import com.github.sirblobman.api.item.ItemBuilder;
import com.github.sirblobman.api.language.ComponentHelper;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.xseries.XMaterial;
import com.github.sirblobman.freeze.FreezePlugin;

public final class FakeInventoryItem implements IConfigurable {
    private final FreezePlugin plugin;
    private final String id;
    private ItemBuilder builder;
    private int slot;

    public FakeInventoryItem(FreezePlugin plugin, String id) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.id = Validate.notEmpty(id, "id must not be empty!");
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
        this.builder = new ItemBuilder(optionalMaterial.orElse(XMaterial.BARRIER));

        int quantity = section.getInt("quantity", 1);
        this.builder.withAmount(quantity);

        FreezePlugin plugin = getPlugin();
        LanguageManager languageManager = plugin.getLanguageManager();
        MultiVersionHandler multiVersionHandler = plugin.getMultiVersionHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();

        if (section.isString("display-name")) {
            String displayNameString = section.getString("display-name");
            if (displayNameString != null) {
                MiniMessage miniMessage = languageManager.getMiniMessage();
                Component displayName = miniMessage.deserialize(displayNameString);
                this.builder.withName(itemHandler, ComponentHelper.wrapNoItalics(displayName));
            }
        }

        if (section.isList("lore")) {
            List<String> loreStringList = section.getStringList("lore");
            if (!loreStringList.isEmpty()) {
                MiniMessage miniMessage = languageManager.getMiniMessage();
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

    private FreezePlugin getPlugin() {
        return this.plugin;
    }

    public String getId() {
        return this.id;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack getItem() {
        return this.builder.build();
    }
}
