package de.luludodo.colormycodes.helper;

import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;

public abstract class ByNameHelper {
    private static final Map<String, Formatting> BY_NAME = new HashMap<>();
    public static void init() {
        for (Formatting formatting : FormattingHelper.getAllAsFormatting()) {
            BY_NAME.put(Formatting.sanitize(formatting.getName()), formatting);
        }
    }

    public static Formatting byName(String name) {
        return name == null ? null : BY_NAME.get(Formatting.sanitize(name));
    }
}
