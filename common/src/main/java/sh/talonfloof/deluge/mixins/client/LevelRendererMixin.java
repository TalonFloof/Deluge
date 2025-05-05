package sh.talonfloof.deluge.mixins.client;

import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.deluge.client.DelugeClient;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Redirect(method = { "method_62215", "lambda$addSkyPass$13", "lambda$addSkyPass$9" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F")) // lambda of addSkyPass
    public float deluge$overcastCelestialRemoval(ClientLevel instance, float v) {
        float previousFog = DelugeClient.previousEvent.getFogColor() != null ? 1 : 0;
        float currentFog = DelugeClient.currentEvent.getFogColor() != null ? 1 : 0;
        return Mth.lerp((float)(100-DelugeClient.fadeTime)/100F,previousFog,currentFog);
    }
    @Inject(method = "addCloudsPass", at = @At("HEAD"), cancellable = true)
    public void deluge$cancelCloudRendering(FrameGraphBuilder p_361907_, CloudStatus p_364196_, Vec3 p_362985_, float p_365209_, int p_362342_, float p_362337_, CallbackInfo ci) {
        ci.cancel();
    }
}
