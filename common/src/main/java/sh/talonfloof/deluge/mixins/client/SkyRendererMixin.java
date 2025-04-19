package sh.talonfloof.deluge.mixins.client;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.deluge.Deluge;
import sh.talonfloof.deluge.DelugeEventType;
import sh.talonfloof.deluge.client.DelugeClient;
import sh.talonfloof.deluge.client.DelugeRenderTypes;

@Mixin(SkyRenderer.class)
public class SkyRendererMixin {
    @Inject(method = "renderSkyDisc", at = @At(value = "HEAD"), cancellable = true)
    public void deluge$removeSkyDisc(float f, float g, float h, CallbackInfo ci) {
        if(DelugeClient.currentEvent.getFogColor() != null)
            ci.cancel();
    }
    @Inject(method = "renderSunMoonAndStars", at = @At("TAIL"))
    public void deluge$skyRender(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float celestialRotation, int i, float celestialIntensity, float tickDelta, FogParameters fogParameters, CallbackInfo ci) {
        var texture = DelugeClient.currentEvent.getTexture();
        if(texture != null) {
            bufferSource.endBatch();
            var mc = Minecraft.getInstance();
            var cloudColor = mc.level.getCloudColor(tickDelta);
            var renderDistance = (mc.options.getEffectiveRenderDistance() * 16) * 2F;
            // 2 (+ -), 3 (- -), 6 (+ +), 7 (- +)
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees((float) mc.level.getGameTime() / 100));
            Matrix4f matrix = poseStack.last().pose();
            VertexConsumer vertexConsumer = bufferSource.getBuffer(DelugeRenderTypes.SKY_DETAILS.apply(texture));
            vertexConsumer.addVertex(matrix, -renderDistance, 100, -renderDistance).setUv(0.0F, 0.0F).setColor(cloudColor);
            vertexConsumer.addVertex(matrix, renderDistance, 100, -renderDistance).setUv(1.0F, 0.0F).setColor(cloudColor);
            vertexConsumer.addVertex(matrix, renderDistance, 100, renderDistance).setUv(1.0F, 1.0F).setColor(cloudColor);
            vertexConsumer.addVertex(matrix, -renderDistance, 100, renderDistance).setUv(0.0F, 1.0F).setColor(cloudColor);
            poseStack.popPose();
            bufferSource.endBatch();
        }
    }
}
