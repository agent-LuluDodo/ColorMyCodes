package de.luludodo.colormycodes;

import de.luludodo.colormycodes.helper.ByNameHelper;
import de.luludodo.colormycodes.helper.ColorIndexHelper;
import de.luludodo.colormycodes.helper.FormattingHelper;
import de.luludodo.colormycodes.helper.StripHelper;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorMyCodesPreLaunch implements PreLaunchEntrypoint {
    public static final Logger LOG = LoggerFactory.getLogger("ColorMyCodes");

    @Override
    public void onPreLaunch() {
        LOG.info("Initializing ColorMyCodes");
        ColorIndexHelper.init();
        FormattingHelper.init();
        StripHelper.init();
        ByNameHelper.init();

        LOG.info("All Formatting values:");
        int count = 0;
        for (Formatting value : Formatting.values()) {
            count++;
            LOG.info(" - {}[{}](name: \"{}\", code: '{}', isColor: {}, isModifier: {}, colorIndex: {}, colorValue: {})", value.name(), value.ordinal(), value.getName(), value.getCode(), value.isColor(), value.isModifier(), value.getColorIndex(), value.getColorValue());
        }
        LOG.info("Found {} values", count);
    }
}
