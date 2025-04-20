package sh.talonfloof.deluge.mixins.client;

import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.deluge.client.DelugeClient;
import sh.talonfloof.deluge.client.DelugeIrisCompat;
import sh.talonfloof.deluge.client.DelugeRenderTypes;

import static sh.talonfloof.deluge.client.DelugeClient.clientConfig;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Redirect(method = { "method_62215", "lambda$addSkyPass$13", "lambda$addSkyPass$9" }, require = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F")) // lambda of addSkyPass
    public float deluge$overcastCelestialRemoval(ClientLevel instance, float v) {
        float previousFog = DelugeClient.previousEvent.getFogColor() != null ? 1 : 0;
        float currentFog = DelugeClient.currentEvent.getFogColor() != null ? 1 : 0;
        return Mth.lerp((float)(100-DelugeClient.fadeTime)/100F,previousFog,currentFog);
    }
    @Inject(method = {"method_62215", "lambda$addSkyPass$12", "lambda$addSkyPass$9"}, require = 1, at = @At("HEAD"), cancellable = true)
    public void deluge$skyCloudRendering(FogParameters fog, DimensionSpecialEffects.SkyType dimensionspecialeffects$skytype, float partialTick, DimensionSpecialEffects dimensionspecialeffects, CallbackInfo ci) {
        var mc = Minecraft.getInstance();
        var poseStack = new PoseStack();
        var bufferSource = ((LevelRenderer)(Object)this).renderBuffers.bufferSource();
        var cloudColor = mc.level.getCloudColor(partialTick);
        var cloudVec4 = new Vector4f(ARGB.vector3fFromRGB24(cloudColor),ARGB.alphaFloat(cloudColor));
        if(DelugeIrisCompat.isShaderEnabled() && clientConfig.iris.irisDarkenClouds) {
            if(DelugeClient.currentEvent.getFogColor() != null)
                cloudVec4 = cloudVec4.mul(0.5F, 0.5F, 0.5F, 1.0F);
            else
                cloudVec4 = cloudVec4.mul(0.8F, 0.8F, 0.8F, 1.0F);
        }
        var renderDistance = (Math.min(mc.options.getEffectiveRenderDistance(),16) * 16) * 2F;

        var prevTexture = DelugeClient.previousEvent.getTexture();
        var texture = DelugeClient.currentEvent.getTexture();
        poseStack.pushPose();
        poseStack.mulPose(Axis.YN.rotationDegrees((float) mc.level.getGameTime() / 100));
        Matrix4f matrix = poseStack.last().pose();
        if(prevTexture != null && !prevTexture.equals(texture)) {
            var type = DelugeRenderTypes.SKY_DETAILS.apply(prevTexture);
            VertexConsumer vertexConsumer = bufferSource.getBuffer(type);
            vertexConsumer.addVertex(matrix, -renderDistance, 100, -renderDistance).setUv(0.0F, 0.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z, (float)(DelugeClient.fadeTime)/100F);
            vertexConsumer.addVertex(matrix, renderDistance, 100, -renderDistance).setUv(1.0F, 0.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(DelugeClient.fadeTime)/100F);
            vertexConsumer.addVertex(matrix, renderDistance, 100, renderDistance).setUv(1.0F, 1.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(DelugeClient.fadeTime)/100F);
            vertexConsumer.addVertex(matrix, -renderDistance, 100, renderDistance).setUv(0.0F, 1.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(DelugeClient.fadeTime)/100F);
            bufferSource.endBatch(type);
        }
        if(texture != null) {
            var type = DelugeRenderTypes.SKY_DETAILS.apply(texture);
            VertexConsumer vertexConsumer = bufferSource.getBuffer(type);
            vertexConsumer.addVertex(matrix, -renderDistance, 100, -renderDistance).setUv(0.0F, 0.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(100 - DelugeClient.fadeTime)/100F);
            vertexConsumer.addVertex(matrix, renderDistance, 100, -renderDistance).setUv(1.0F, 0.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(100 - DelugeClient.fadeTime)/100F);
            vertexConsumer.addVertex(matrix, renderDistance, 100, renderDistance).setUv(1.0F, 1.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(100 - DelugeClient.fadeTime)/100F);
            vertexConsumer.addVertex(matrix, -renderDistance, 100, renderDistance).setUv(0.0F, 1.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(100 - DelugeClient.fadeTime)/100F);
            bufferSource.endBatch(type);
        }
        poseStack.popPose();
    }
}
