package de.luludodo.colormycodes.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.luludodo.colormycodes.ColorMyCodesPreLaunch;
import de.luludodo.colormycodes.formatting.MixinFormatting;
import de.luludodo.colormycodes.helper.ByNameHelper;
import de.luludodo.colormycodes.helper.FormattingHelper;
import de.luludodo.colormycodes.helper.StripHelper;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Formatting.class)
public abstract class FormattingMixin implements MixinFormatting {

    @Shadow public abstract char getCode();
    @Shadow public abstract int getColorIndex();
    @Shadow public abstract boolean isModifier();
    @Shadow public abstract boolean isColor();
    @Shadow public abstract @Nullable Integer getColorValue();
    @Shadow public abstract String getName();

    @Unique private boolean skipOverrides = false;
    @Unique private String colormycodes$name;

    @Inject(
            method = "<init>(Ljava/lang/String;ILjava/lang/String;CZILjava/lang/Integer;)V",
            at = @At("RETURN")
    )
    public void colormycodes$init(CallbackInfo ci) {
        colormycodes$name =  Formatting.sanitize(getName());
    }

    @ModifyReturnValue(
            method = "getCode",
            at = @At("RETURN")
    )
    public char colormycodes$overrideCode(char original) {
        if (skipOverrides)
            return original;

        return FormattingHelper.get(colormycodes$name).colormycodes$getCode();
    }

    @Override
    public char colormycodes$getCode() {
        skipOverrides = true;
        char result = getCode();
        skipOverrides = false;
        return result;
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
        if (skipOverrides)
            return original;

        return FormattingHelper.get(colormycodes$name).colormycodes$isModifier();
    }

    @Override
    public boolean colormycodes$isModifier() {
        skipOverrides = true;
        boolean result = isModifier();
        skipOverrides = false;
        return result;
    }

    @ModifyReturnValue(
            method = "isColor",
            at = @At("RETURN")
    )
    public boolean colormycodes$overrideIsColor(boolean original) {
        if (skipOverrides)
            return original;

        return FormattingHelper.get(colormycodes$name).colormycodes$isColor();
    }

    @Override
    public boolean colormycodes$isColor() {
        skipOverrides = true;
        boolean result = isColor();
        skipOverrides = false;
        return result;
    }

    @ModifyReturnValue(
            method = "getColorValue",
            at = @At("RETURN")
    )
    public Integer colormycodes$overrideGetColorValue(Integer original) {
        if (skipOverrides)
            return original;

        return FormattingHelper.get(colormycodes$name).colormycodes$getColorValue();
    }

    @Override
    public Integer colormycodes$getColorValue() {
        skipOverrides = true;
        Integer result = getColorValue();
        skipOverrides = false;
        return result;
    }

    @Override
    public String colormycodes$getName() {
        return colormycodes$name;
    }

    @Override
    public Formatting colormycodes$asFormatting() {
        return (Formatting) (Object) this;
    }

    @Inject(
            method = "strip",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void colormycodes$strip(String string, CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(StripHelper.strip(string));
    }

    @Inject(
            method = "byName",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void colormycodes$byName(String name, CallbackInfoReturnable<Formatting> cir) {
        cir.setReturnValue(ByNameHelper.byName(name));
    }

    @Inject(
            method = "values",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void colormycodes$values(CallbackInfoReturnable<Formatting[]> cir) {
        if (FormattingHelper.isLoaded())
            cir.setReturnValue(FormattingHelper.getAllAsFormatting());
    }
}
