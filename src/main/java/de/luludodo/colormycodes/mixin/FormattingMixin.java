package de.luludodo.colormycodes.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.luludodo.colormycodes.formatting.ConfigFormatting;
import de.luludodo.colormycodes.formatting.MixinFormatting;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static de.luludodo.colormycodes.config.ColorMyCodesConfig.getInstance;

@Mixin(Formatting.class)
public abstract class FormattingMixin implements MixinFormatting {

    @Shadow public abstract char getCode();
    @Shadow public abstract int getColorIndex();
    @Shadow public abstract boolean isModifier();
    @Shadow public abstract boolean isColor();
    @Shadow public abstract @Nullable Integer getColorValue();
    @Shadow public abstract String getName();

    @Unique private String colormycodes$name;

    @Inject(
            method = "<init>(Ljava/lang/String;ILjava/lang/String;CZILjava/lang/Integer;)V",
            at = @At("RETURN")
    )
    public void colormycodes$init(CallbackInfo ci) {
        colormycodes$name = "minecraft:" + getName();
    }

    @ModifyReturnValue(
            method = "getCode",
            at = @At("RETURN")
    )
    public char colormycodes$overrideCode(char original) {
        ConfigFormatting formatting = getInstance().get(colormycodes$name);
        if (formatting == null) {
            return original;
        }

        return formatting.colormycodes$getCode();
    }

    @Override
    public char colormycodes$getCode() {
        return getCode();
    }

    @Override
    public int colormycodes$getColorIndex() {
        return getColorIndex();
    }

    @ModifyReturnValue(
            method = "isModifier",
            at = @At("RETURN")
    )
    public boolean colormycodes$overrideIsModifier(boolean original) {
        ConfigFormatting formatting = getInstance().get(colormycodes$name);
        if (formatting == null) {
            return original;
        }

        return formatting.colormycodes$isModifier();
    }

    @Override
    public boolean colormycodes$isModifier() {
        return isModifier();
    }

    @ModifyReturnValue(
            method = "isModifier",
            at = @At("RETURN")
    )
    public boolean colormycodes$overrideIsColor(boolean original) {
        ConfigFormatting formatting = getInstance().get(colormycodes$name);
        if (formatting == null) {
            return original;
        }

        return formatting.colormycodes$isColor();
    }

    @Override
    public boolean colormycodes$isColor() {
        return isColor();
    }

    @ModifyReturnValue(
            method = "getColorValue",
            at = @At("RETURN")
    )
    public Integer colormycodes$overrideGetColorValue(Integer original) {
        ConfigFormatting formatting = getInstance().get(colormycodes$name);
        if (formatting == null) {
            return original;
        }

        return formatting.colormycodes$getColorValue();
    }

    @Override
    public Integer colormycodes$getColorValue() {
        return getColorValue();
    }

    @Override
    public String colormycodes$getName() {
        return getName();
    }

    @ModifyReturnValue(
            method = "strip",
            at = @At("RETURN")
    )
    private static String colormycodes$strip(@Nullable String original) {

    }
}
