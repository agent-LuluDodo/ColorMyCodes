package de.luludodo.colormycodes.colorIndex;

import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class ColorIndexHelper {
    private static int NEXT = 40;

    private static final Map<String, Integer> NAME_TO_COLOR_INDEX = new HashMap<>();
    public static void init() {
        for (Formatting formatting : Formatting.values()) {
            NAME_TO_COLOR_INDEX.put(formatting.getName(), formatting.getColorIndex());
        }
    }

    /**
     * Returns the next free color-index.
     * <p>
     * Recommended usage:  {@code private final int colorIndex = ColorIndexHelper.getNext();}
     *
     * @return The next free color-index.
     */
    public static int getColor(String name) {
        return NAME_TO_COLOR_INDEX.computeIfAbsent(name.toLowerCase(Locale.ROOT), key -> NEXT++);
    }
}
