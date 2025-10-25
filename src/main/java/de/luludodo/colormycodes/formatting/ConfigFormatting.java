package de.luludodo.colormycodes.formatting;

import de.luludodo.colormycodes.colorIndex.ColorIndexHelper;

public class ConfigFormatting implements MixinFormatting {
    private final String name;
    private final char code;
    private final boolean isColor;
    private final boolean isModifier;
    private final int colorIndex;
    private final Integer colorValue;
    private final String stringValue;
    public ConfigFormatting(String name, char code, int colorValue) {
        this.name = name;
        this.code = code;
        this.isColor = true;
        this.isModifier = false;
        this.colorIndex = ColorIndexHelper.getColor(name);
        this.colorValue = colorValue;
        this.stringValue = "§" + code;
    }

    public ConfigFormatting(String name, char code, boolean modifier) {
        this.name = name;
        this.code = code;
        this.isColor = false;
        this.isModifier = modifier;
        this.colorIndex = -1;
        this.colorValue = null;
        this.stringValue = "§" + code;
    }

    public ConfigFormatting(String name) {
        this.name = name;
        this.code = '\00';
        this.isColor = false;
        this.isModifier = false;
        this.colorIndex = -1;
        this.colorValue = null;
        this.stringValue = "";
    }

    @Override
    public String colormycodes$getName() {
        return this.name;
    }

    @Override
    public char colormycodes$getCode() {
        return this.code;
    }

    @Override
    public boolean colormycodes$isColor() {
        return this.isColor;
    }

    @Override
    public boolean colormycodes$isModifier() {
        return this.isModifier;
    }

    @Override
    public int colormycodes$getColorIndex() {
        return this.colorIndex;
    }

    @Override
    public Integer colormycodes$getColorValue() {
        return this.colorValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }
}
