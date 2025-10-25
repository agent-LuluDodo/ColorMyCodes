package de.luludodo.colormycodes.formatting;

import net.minecraft.util.Formatting;

public interface MixinFormatting {
    char colormycodes$getCode();
    int colormycodes$getColorIndex();
    boolean colormycodes$isModifier();
    boolean colormycodes$isColor();
    Integer colormycodes$getColorValue();
    String colormycodes$getName();
    Formatting colormycodes$asFormatting();
}
