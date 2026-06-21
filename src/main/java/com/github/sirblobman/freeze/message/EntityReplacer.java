package com.github.sirblobman.freeze.message;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;

public final class EntityReplacer extends Replacer{
    private final Entity entity;

    public EntityReplacer(@NotNull String source, @NotNull Entity entity) {
        super(source);
        this.entity = entity;
    }

    private @NotNull Entity getEntity() {
        return this.entity;
    }

    @Override
    public @NotNull Component getReplacement() {
        Entity entity = getEntity();
        HoverEvent<HoverEvent.ShowEntity> entityEvent = entity.asHoverEvent();
        return entity.name().hoverEvent(entityEvent);
    }
}
