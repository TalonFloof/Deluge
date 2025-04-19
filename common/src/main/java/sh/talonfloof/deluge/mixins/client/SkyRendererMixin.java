package sh.talonfloof.deluge.mixins.client;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
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
import sh.talonfloof.deluge.client.DelugeIrisCompat;
import sh.talonfloof.deluge.client.DelugeRenderTypes;

import static sh.talonfloof.deluge.client.DelugeClient.clientConfig;

@Mixin(SkyRenderer.class)
public class SkyRendererMixin {
    @Inject(method = "renderSkyDisc", at = @At(value = "HEAD"), cancellable = true)
    public void deluge$removeSkyDisc(float f, float g, float h, CallbackInfo ci) {
        if(DelugeClient.currentEvent.getFogColor() != null)
            ci.cancel();
    }
    @Inject(method = "renderSunMoonAndStars", at = @At("TAIL"))
    private void deluge$skyRender(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float f, int i, float g, float h, FogParameters fogParameters, CallbackInfo ci) {
        var texture = DelugeClient.currentEvent.getTexture();
        if(texture != null) {
            var mc = Minecraft.getInstance();
            var cloudColor = mc.level.getCloudColor(0);
            var cloudVec4 = new Vector4f(ARGB.vector3fFromRGB24(cloudColor),ARGB.alphaFloat(cloudColor));
            if(DelugeIrisCompat.isShaderEnabled() && clientConfig.iris.irisDarkenClouds) {
                if(DelugeClient.currentEvent.getFogColor() != null)
                    cloudVec4 = cloudVec4.mul(0.5F, 0.5F, 0.5F, 1.0F);
                else
                    cloudVec4 = cloudVec4.mul(0.8F, 0.8F, 0.8F, 1.0F);
            }
            var renderDistance = (Math.min(mc.options.getEffectiveRenderDistance(),16) * 16) * 2F;
            // 2 (+ -), 3 (- -), 6 (+ +), 7 (- +)
            poseStack.pushPose();
            poseStack.mulPose(Axis.YN.rotationDegrees((float) mc.level.getGameTime() / 100));
            Matrix4f matrix = poseStack.last().pose();
            var type = DelugeRenderTypes.SKY_DETAILS.apply(texture);
            VertexConsumer vertexConsumer = bufferSource.getBuffer(type);
            vertexConsumer.addVertex(matrix, -renderDistance, 100, -renderDistance).setUv(0.0F, 0.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,cloudVec4.w);
            vertexConsumer.addVertex(matrix, renderDistance, 100, -renderDistance).setUv(1.0F, 0.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,cloudVec4.w);
            vertexConsumer.addVertex(matrix, renderDistance, 100, renderDistance).setUv(1.0F, 1.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,cloudVec4.w);
            vertexConsumer.addVertex(matrix, -renderDistance, 100, renderDistance).setUv(0.0F, 1.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,cloudVec4.w);
            poseStack.popPose();
            bufferSource.endBatch(type);
        }
    }
}
