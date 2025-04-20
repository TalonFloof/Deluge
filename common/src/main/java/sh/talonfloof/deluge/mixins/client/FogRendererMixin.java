package sh.talonfloof.deluge.mixins.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.ARGB;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sh.talonfloof.deluge.client.DelugeClient;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "computeFogColor", at = @At("RETURN"), cancellable = true)
    private static void setupColor(Camera camera, float tickDelta, ClientLevel clientLevel, int i, float g, CallbackInfoReturnable<Vector4f> cir) {
        var initialFog = cir.getReturnValue();
        var mc = Minecraft.getInstance();
        var cloudColor = mc.level.getCloudColor(tickDelta);
        var vec = ARGB.vector3fFromRGB24(cloudColor);
        var previousFogColorVec = DelugeClient.previousEvent.getFogColor() != null ? new Vector4f(DelugeClient.previousEvent.getFogColor().mul(vec),1.0F) : initialFog;
        var currentFogColorVec = DelugeClient.currentEvent.getFogColor() != null ? new Vector4f(DelugeClient.currentEvent.getFogColor().mul(vec),1.0F) : initialFog;
        var previousFogColor = ARGB.color(255,ARGB.as8BitChannel(previousFogColorVec.x),ARGB.as8BitChannel(previousFogColorVec.y),ARGB.as8BitChannel(previousFogColorVec.z));
        var currentFogColor = ARGB.color(255,ARGB.as8BitChannel(currentFogColorVec.x),ARGB.as8BitChannel(currentFogColorVec.y),ARGB.as8BitChannel(currentFogColorVec.z));
        var finalColor = ARGB.lerp((float)(100-DelugeClient.fadeTime)/100F,previousFogColor,currentFogColor);
        cir.setReturnValue(new Vector4f(ARGB.vector3fFromRGB24(finalColor),ARGB.alphaFloat(finalColor)));
        cir.cancel();
    }
}
