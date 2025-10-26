package de.luludodo.colormycodes.helper;

import net.minecraft.util.Formatting;

import java.util.*;

public abstract class GetNamesHelper {
    private static Set<String> BAD_NAMES;
    public static void init() {
        BAD_NAMES = new HashSet<>();
        for (Formatting formatting : Formatting.values()) {
            if (formatting.getCode() == '\00')
                BAD_NAMES.add(formatting.getName());
        }
    }

    public static Collection<String> filter(Collection<String> original) {
        List<String> result = new ArrayList<>();
        for (String name : original) {
            if (!BAD_NAMES.contains(name)) {
                result.add(name);
            }
        }
        return result;
    }
}
