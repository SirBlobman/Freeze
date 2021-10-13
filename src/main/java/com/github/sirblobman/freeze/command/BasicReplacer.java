package com.github.sirblobman.freeze.command;

import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.api.utility.Validate;

final class BasicReplacer implements Replacer {
    private final String literal;
    private final String replacement;
    
    BasicReplacer(String literal, String replacement) {
        this.literal = Validate.notNull(literal, "literal must not be null!");
        this.replacement = Validate.notNull(replacement, "replacement must not be null!");
    }
    
    public String getLiteral() {
        return this.literal;
    }
    
    public String getReplacement() {
        return this.replacement;
    }
    
    @Override
    public String replace(String string) {
        String literal = getLiteral();
        String replacement = getReplacement();
        return string.replace(literal, replacement);
    }
}
