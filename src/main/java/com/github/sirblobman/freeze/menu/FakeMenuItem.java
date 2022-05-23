package com.github.sirblobman.freeze.menu;

import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.api.xseries.XMaterial;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FakeMenuItem {
    private int slot;
    private XMaterial material;
    private int quantity;
    private String displayName;
    private List<String> lore;
    private boolean glowing;
    private Integer customModelData;
    
    public FakeMenuItem() {
        this.slot = 0;
        this.material = XMaterial.AIR;
        this.quantity = 1;
        this.displayName = null;
        this.lore = null;
        this.glowing = false;
        this.customModelData = null;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public void setSlot(int slot) {
        if(slot < 0) {
            throw new IllegalArgumentException("slot must be at least 0.");
        }
        
        this.slot = slot;
    }
    
    @NotNull
    public XMaterial getMaterial() {
        return this.material;
    }
    
    public void setMaterial(XMaterial material) {
        Validate.notNull(material, "material must not be null!");
        this.material = material;
    }
    
    public int getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(int quantity) {
        if(quantity < 0) {
            throw new IllegalArgumentException("quantity must be at least 0.");
        }
        
        if(quantity > 64) {
            throw new IllegalArgumentException("quantity must not be higher than 64.");
        }
        
        this.quantity = quantity;
    }
    
    @Nullable
    public String getDisplayName() {
        return this.displayName;
    }
    
    @Nullable
    public String getDisplayNameFormatted() {
        String displayName = getDisplayName();
        if(displayName == null) {
            return null;
        }
        
        return MessageUtility.color(displayName);
    }
    
    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
    }
    
    @Nullable
    public List<String> getLore() {
        return this.lore;
    }
    
    @Nullable
    public List<String> getLoreFormatted() {
        List<String> lore = getLore();
        if(lore == null) {
            return null;
        }
        
        return MessageUtility.colorList(lore);
    }
    
    public void setLore(@Nullable List<String> lore) {
        this.lore = lore;
    }
    
    public boolean isGlowing() {
        return this.glowing;
    }
    
    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }
    
    @Nullable
    public Integer getCustomModelData() {
        return this.customModelData;
    }
    
    public void setCustomModelData(@Nullable Integer customModelData) {
        this.customModelData = customModelData;
    }
    
    public void load(ConfigurationSection section) {
        Validate.notNull(section, "section must not be null!");
        
        int slot = section.getInt("slot");
        setSlot(slot);
        
        String materialName = section.getString("material");
        if(materialName == null) {
            throw new IllegalArgumentException("material is not valid.");
        }
        
        Optional<XMaterial> optionalMaterial = XMaterial.matchXMaterial(materialName);
        if(!optionalMaterial.isPresent()) {
            throw new IllegalArgumentException(materialName + " is not a valid XMaterial name.");
        }
        
        XMaterial material = optionalMaterial.get();
        setMaterial(material);
        
        int quantity = section.getInt("quantity");
        setQuantity(quantity);
        
        String displayName = section.getString("display-name");
        setDisplayName(displayName);
    
        List<String> lore = section.getStringList("lore");
        setLore(lore.isEmpty() ? null : lore);
        
        boolean glowing = section.getBoolean("glowing");
        setGlowing(glowing);
        
        if(section.isSet("custom-model-data")) {
            int customModelData = section.getInt("custom-model-data");
            setCustomModelData(customModelData);
        } else {
            setCustomModelData(null);
        }
    }
    
    @NotNull
    public ItemStack getAsItemStack() {
        XMaterial material = getMaterial();
        ItemStack item = material.parseItem();
        if(item == null) {
            return new ItemStack(Material.AIR);
        }
        
        int quantity = getQuantity();
        item.setAmount(quantity);
    
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) {
            return item;
        }
        
        String displayName = getDisplayNameFormatted();
        if(displayName != null) {
            itemMeta.setDisplayName(displayName);
        }
        
        List<String> lore = getLoreFormatted();
        if(lore != null) {
            itemMeta.setLore(lore);
        }
        
        Integer customModelData = getCustomModelData();
        if(customModelData != null) {
            int minorVersion = VersionUtility.getMinorVersion();
            if(minorVersion >= 14) {
                itemMeta.setCustomModelData(customModelData);
            }
        }
        
        boolean glowing = isGlowing();
        if(glowing) {
            itemMeta.addEnchant(Enchantment.LUCK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        
        item.setItemMeta(itemMeta);
        return item;
    }
}
