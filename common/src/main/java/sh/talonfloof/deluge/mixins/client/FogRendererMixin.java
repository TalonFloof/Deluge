package sh.talonfloof.deluge.mixins.client;

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

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "computeFogColor", at = @At("RETURN"), cancellable = true)
    private static void setupColor(Camera camera, float tickDelta, ClientLevel clientLevel, int i, float g, CallbackInfoReturnable<Vector4f> cir) {
        var mc = Minecraft.getInstance();
        var cloudColor = mc.level.getCloudColor(tickDelta);
        var vec = ARGB.vector3fFromRGB24(cloudColor);
        cir.setReturnValue(new Vector4f((109/255F)*vec.x,(112/255F)*vec.y,(120/255F)*vec.z,1F));
        cir.cancel();
    }
}
