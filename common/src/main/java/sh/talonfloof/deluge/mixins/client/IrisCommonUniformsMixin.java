package sh.talonfloof.deluge.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sh.talonfloof.deluge.client.DelugeClient;

@Mixin(targets = {"net.irisshaders.iris.uniforms.CommonUniforms"}, remap = false)
public class IrisCommonUniformsMixin {
    @Inject(method = "getRainStrength()F", at = @At("RETURN"), cancellable = true, require = 0, remap = false)
    private static void deluge$overrideIrisRain(CallbackInfoReturnable<Float> cir) {
        if(DelugeClient.currentEvent.getFogColor() != null) {
            cir.setReturnValue(1f);
            cir.cancel();
        }
    }
}
