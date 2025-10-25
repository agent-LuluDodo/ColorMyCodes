package de.luludodo.colormycodes.config;

import de.luludodo.colormycodes.config.serializer.ColorMyCodesSerializer;
import de.luludodo.colormycodes.formatting.ConfigFormatting;

public class ColorMyCodesConfig extends JsonMapConfig<String, ConfigFormatting> {
    private static ColorMyCodesConfig INSTANCE = null;
    public static ColorMyCodesConfig getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ColorMyCodesConfig();

        return INSTANCE;
    }

    public ColorMyCodesConfig() {
        super("colormycodes/config", 1, new ColorMyCodesSerializer(), true);
    }

    @Override
    protected String getDefaultResource() {
        return "/config/default.json";
    }
}
