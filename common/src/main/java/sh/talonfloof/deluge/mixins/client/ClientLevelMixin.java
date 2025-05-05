package sh.talonfloof.deluge.mixins.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sh.talonfloof.deluge.client.DelugeClient;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Redirect(method = "getSkyDarken(F)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"))
    public float deluge$terrainDarkenSub1(ClientLevel instance, float v) {
        var previousDarken = DelugeClient.previousEvent.getFogColor() != null ? 1F : instance.getRainLevel(v);
        var currentDarken = DelugeClient.currentEvent.getFogColor() != null ? 1F : instance.getRainLevel(v);
        return Mth.lerp((float)(100-DelugeClient.fadeTime)/100F,previousDarken,currentDarken);
    }
    @Redirect(method = "getSkyDarken(F)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getThunderLevel(F)F"))
    public float deluge$terrainDarkenSub2(ClientLevel instance, float v) {
        var previousDarken = DelugeClient.previousEvent.getFogColor() != null ? 1F : instance.getThunderLevel(v);
        var currentDarken = DelugeClient.currentEvent.getFogColor() != null ? 1F : instance.getThunderLevel(v);
        return Mth.lerp((float)(100-DelugeClient.fadeTime)/100F,previousDarken,currentDarken);
    }
    @Inject(method = "getSkyDarken(F)F", at = @At("RETURN"), cancellable = true)
    public void deluge$terrainDarkenSub3(float partialTick, CallbackInfoReturnable<Float> cir) {
        var thunderLevel = ((ClientLevel)(Object)this).getThunderLevel(partialTick);
        cir.setReturnValue(Mth.lerp(thunderLevel, cir.getReturnValue(),0F));
    }
}
