package de.luludodo.colormycodes.helper;

import de.luludodo.colormycodes.ColorMyCodesPreLaunch;
import net.minecraft.util.Formatting;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.WrongMethodTypeException;
import java.util.Locale;

public abstract class CursedReflectionHelper {
    private static int ORDINAL = 22;
    public static Formatting newFormatting(String name, char code, int colorIndex) {
        try {
            return (Formatting) MethodHandles.lookup()
                    .findConstructor(
                            Formatting.class,
                            MethodType.methodType(
                                    void.class,
                                    String.class, // Enum name
                                    int.class, // Enum id
                                    String.class, // name
                                    char.class, // code
                                    boolean.class, // modifier
                                    int.class, // colorIndex
                                    Integer.class // colorValue
                            )
                    ).invokeExact(
                            name.toUpperCase(Locale.ROOT),
                            ORDINAL++,
                            name,
                            code,
                            false,
                            colorIndex,
                            (Integer) null
                    );
        } catch (NoSuchMethodException e) {
            ColorMyCodesPreLaunch.LOG.error("Couldn't get Formatting constructor! -> crashing");
            throw new IllegalStateException("Couldn't get Formatting constructor!", e);
        } catch (IllegalAccessException e) {
            ColorMyCodesPreLaunch.LOG.error("Couldn't access Formatting constructor! -> crashing");
            throw new IllegalStateException("Couldn't access Formatting constructor!", e);
        } catch (WrongMethodTypeException e) {
            ColorMyCodesPreLaunch.LOG.error("Method type of Formatting constructor changed? -> crashing");
            throw new IllegalStateException("Method type of Formatting constructor changed?", e);
        } catch (Throwable e) {
            ColorMyCodesPreLaunch.LOG.error("Formatting constructor threw an exception! -> crashing");
            throw new IllegalStateException("Formatting constructor threw an exception!", e);
        }
    }
}
