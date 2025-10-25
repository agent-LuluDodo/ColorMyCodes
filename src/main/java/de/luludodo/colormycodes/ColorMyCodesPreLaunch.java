package de.luludodo.colormycodes;

import de.luludodo.colormycodes.colorIndex.ColorIndexHelper;
import de.luludodo.colormycodes.config.ColorMyCodesConfig;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class ColorMyCodesPreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        ColorIndexHelper.init();

        // Loads the config
        ColorMyCodesConfig.getInstance();
    }
}
