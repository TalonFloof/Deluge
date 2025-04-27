package sh.talonfloof.deluge.mixins.client;

import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sh.talonfloof.deluge.client.DelugeClient;

@Mixin(Level.class)
public class LevelMixinClient {
    @Inject(method = "getRainLevel", at = @At(value = "RETURN"), cancellable = true)
    public void deluge$localizedRainHandler(float partialTick, CallbackInfoReturnable<Float> cir) {
        if(DelugeClient.currentEvent.canRain()) {
            cir.setReturnValue(Mth.lerp((100 - DelugeClient.fadeTime) / 100F, 0F, DelugeClient.rainLevel));
        } else {
            cir.setReturnValue(0f);
        }
    }
}
