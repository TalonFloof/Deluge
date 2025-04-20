package sh.talonfloof.deluge.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sh.talonfloof.deluge.client.DelugeClient;

@Mixin(targets = {"net.irisshaders.iris.uniforms.CommonUniforms"}, remap = false)
public class IrisCommonUniformsMixin {
    @Inject(method = "getRainStrength()F", at = @At("RETURN"), cancellable = true, require = 0, remap = false)
    private static void deluge$overrideIrisRain(CallbackInfoReturnable<Float> cir) {
        var currentValue = DelugeClient.currentEvent.getFogColor() != null ? 1F : 0F;
        var previousValue = DelugeClient.previousEvent.getFogColor() != null ? 1F : 0F;
        if(currentValue != 0F || previousValue != 0F) {
            cir.setReturnValue(Mth.lerp((float)(100-DelugeClient.fadeTime)/100F,previousValue,currentValue));
            cir.cancel();
        }
    }
}
