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
import sh.talonfloof.deluge.client.DelugeRenderTypes;

@Mixin(SkyRenderer.class)
public class SkyRendererMixin {
    @Redirect(method = "renderSkyDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 0))
    public void deluge$newSkyColor(float f, float g, float h, float i) {
        var mc = Minecraft.getInstance();
        var cloudColor = mc.level.getCloudColor(0);
        var vec = ARGB.vector3fFromRGB24(cloudColor);
        RenderSystem.setShaderColor((109/255F)*vec.x,(112/255F)*vec.y,(120/255F)*vec.z,i);
    }
    @Inject(method = "renderSunMoonAndStars", at = @At("TAIL"))
    public void deluge$skyRender(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float celestialRotation, int i, float celestialIntensity, float tickDelta, FogParameters fogParameters, CallbackInfo ci) {
        bufferSource.endBatch();
        var mc = Minecraft.getInstance();
        var cloudColor = mc.level.getCloudColor(tickDelta);
        var renderDistance = (mc.options.getEffectiveRenderDistance()*16)*2F;
        // 2 (+ -), 3 (- -), 6 (+ +), 7 (- +)
        var fog = new FogParameters(fogParameters.start(),fogParameters.end(), FogShape.CYLINDER, fogParameters.red(), fogParameters.green(), fogParameters.blue(), fogParameters.alpha());
        RenderSystem.setShaderFog(fog);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees((float)mc.level.getGameTime() / 100));
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(DelugeRenderTypes.SKY_DETAILS.apply(Deluge.path("textures/storm.png")));
        vertexConsumer.addVertex(matrix, -renderDistance,100,-renderDistance).setUv(0.0F, 0.0F).setColor(cloudColor);
        vertexConsumer.addVertex(matrix, renderDistance,100,-renderDistance).setUv(1.0F, 0.0F).setColor(cloudColor);
        vertexConsumer.addVertex(matrix, renderDistance,100,renderDistance).setUv(1.0F, 1.0F).setColor(cloudColor);
        vertexConsumer.addVertex(matrix, -renderDistance,100,renderDistance).setUv(0.0F, 1.0F).setColor(cloudColor);
        poseStack.popPose();
        bufferSource.endBatch();
    }
}
