package sh.talonfloof.deluge.mixins.client;

import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.deluge.client.DelugeClient;
import sh.talonfloof.deluge.client.DelugeRenderTypes;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Redirect(method = { "method_62215", "lambda$addSkyPass$13" }, require = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F")) // lambda of addSkyPass
    public float deluge$overcastCelestialRemoval(ClientLevel instance, float v) {
        return DelugeClient.currentEvent.getFogColor() != null ? 1 : 0;
    }
    @Inject(method = "addCloudsPass", at = @At("HEAD"), cancellable = true)
    public void deluge$disableCloudRendering(FrameGraphBuilder frameGraphBuilder, CloudStatus cloudStatus, Vec3 vec3, float f, int i, float g, CallbackInfo ci) {
        ci.cancel();
    }
}
