package de.luludodo.colormycodes.helper;

import de.luludodo.colormycodes.formatting.MixinFormatting;
import net.minecraft.util.Formatting;

import java.util.HashSet;
import java.util.Set;

public abstract class StripHelper {
    private static Set<Character> CODES;
    public static void init() {
        CODES = new HashSet<>();
        for (MixinFormatting formatting : FormattingHelper.getAll()) {
            char code = formatting.colormycodes$getCode();
            if (code != '\00') {
                CODES.add(code);
            }
        }
    }

    public static String strip(String input) {
        StringBuilder output = new StringBuilder();

        boolean prefixed = false;
        for (int codePoint : input.codePoints().toArray()) {
            if (prefixed) {
                if (!CODES.contains((char) codePoint)) {
                    output.append(Formatting.FORMATTING_CODE_PREFIX);
                    output.appendCodePoint(codePoint);
                }
            } else {
                if (codePoint == Formatting.FORMATTING_CODE_PREFIX) {
                    prefixed = true;
                } else {
                    output.appendCodePoint(codePoint);
                }
            }
        }
        if (prefixed)
            output.append(Formatting.FORMATTING_CODE_PREFIX);

        return output.toString();
    }
}
