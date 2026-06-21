package com.github.sirblobman.freeze.message;

import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

public final class IntegerReplacer extends Replacer {
    private final int value;

    public IntegerReplacer(@NotNull String source, int value) {
        super(source);
        this.value = value;
    }

    private int getValue() {
        return this.value;
    }

    @Override
    public @NotNull Component getReplacement() {
        return Component.text(getValue());
    }
}
