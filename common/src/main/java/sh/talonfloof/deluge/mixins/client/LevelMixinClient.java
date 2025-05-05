package sh.talonfloof.deluge.mixins.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
    @Inject(method = "getThunderLevel", at = @At(value = "RETURN"), cancellable = true)
    public void deluge$localizedThunderHandler(float partialTick, CallbackInfoReturnable<Float> cir) {
        if(DelugeClient.currentEvent.canRain() && DelugeClient.currentEvent.canThunder()) {
            var rain = Mth.lerp((100 - DelugeClient.fadeTime) / 100F, 0F, DelugeClient.rainLevel);
            cir.setReturnValue(Mth.clamp(DelugeClient.thunderLevel, 0F, rain));
        } else {
            cir.setReturnValue(0f);
        }
    }
}
