package de.luludodo.colormycodes.helper;

import de.luludodo.colormycodes.ColorMyCodesPreLaunch;
import de.luludodo.colormycodes.config.ColorMyCodesConfig;
import de.luludodo.colormycodes.formatting.ConfigFormatting;
import de.luludodo.colormycodes.formatting.MixinFormatting;
import net.minecraft.util.Formatting;

import java.util.*;

public abstract class FormattingHelper {
    private static Map<String, MixinFormatting> NAME_TO_FORMATTING;
    private static List<MixinFormatting> ALL;
    private static Formatting[] ALL_FORMATTING;
    private static boolean LOADED = false;
    public static void init() {
        NAME_TO_FORMATTING = new HashMap<>();
        Map<String, Formatting> NAME_TO_REAL_FORMATTING = new HashMap<>();
        Map<String, MixinFormatting> REMOVED = new HashMap<>();

        for (Formatting formatting : Formatting.values()) {
            MixinFormatting mixinFormatting = (MixinFormatting) (Object) formatting;
            assert mixinFormatting != null;

            String name = Formatting.sanitize(mixinFormatting.colormycodes$getName());
            NAME_TO_FORMATTING.put(name, mixinFormatting);
            NAME_TO_REAL_FORMATTING.put(name, formatting);
        }

        Set<String> names = new HashSet<>();
        for (String rawName : ColorMyCodesConfig.getInstance().options()) {
            ConfigFormatting configFormatting = ColorMyCodesConfig.getInstance().get(rawName);

            String name = Formatting.sanitize(configFormatting.colormycodes$getName());
            if (names.add(name)) {
                if (configFormatting.colormycodes$getCode() != '\00') {
                    NAME_TO_FORMATTING.put(name, configFormatting);

                    Formatting formatting = NAME_TO_REAL_FORMATTING.get(name);
                    if (formatting == null) {
                        NAME_TO_REAL_FORMATTING.put(name, configFormatting.createFormatting());
                    } else {
                        configFormatting.setFormatting(formatting);
                    }
                } else {
                    NAME_TO_FORMATTING.remove(name);
                    REMOVED.put(name, configFormatting);
                    NAME_TO_REAL_FORMATTING.remove(name);
                }
            } else {
                ColorMyCodesPreLaunch.LOG.warn("Duplicate name '{}' (ignoring second declaration)!", name);
            }
        }

        ALL = List.copyOf(NAME_TO_FORMATTING.values());
        ALL_FORMATTING = NAME_TO_REAL_FORMATTING.values().stream()
                .sorted(Comparator.comparingInt(Enum::ordinal)).toArray(Formatting[]::new);
        NAME_TO_FORMATTING.putAll(REMOVED);
        LOADED = true;
    }

    public static List<MixinFormatting> getAll() {
        return ALL;
    }

    public static Formatting[] getAllAsFormatting() {
        return ALL_FORMATTING;
    }

    public static MixinFormatting get(String name) {
        return NAME_TO_FORMATTING.get(name);
    }

    public static boolean isLoaded() {
        return LOADED;
    }
}
