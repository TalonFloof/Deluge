package sh.talonfloof.deluge.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ARGB;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Unique;
import sh.talonfloof.deluge.Deluge;

import static sh.talonfloof.deluge.client.DelugeClient.clientConfig;
import static sh.talonfloof.deluge.client.DelugeClient.clientWindManager;

public class DelugeRendering {
    public static void renderSky(LevelRenderer instance, float partialTick) {
        var mc = Minecraft.getInstance();
        var poseStack = new PoseStack();
        var bufferSource = instance.renderBuffers.bufferSource();
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
        poseStack.mulPose(Axis.YN.rotationDegrees((float) mc.level.getGameTime() / 50));
        Matrix4f matrix = poseStack.last().pose();
        if(prevTexture != null && !prevTexture.equals(texture)) {
            var type = DelugeRenderTypes.SKY_DETAILS.apply(prevTexture);
            VertexConsumer vertexConsumer = bufferSource.getBuffer(type);
            vertexConsumer.addVertex(matrix, -renderDistance, 100, -renderDistance).setUv(0.0F, 0.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z, (float)(DelugeClient.fadeTime)/100F);
            vertexConsumer.addVertex(matrix, renderDistance, 100, -renderDistance).setUv(1.0F, 0.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(DelugeClient.fadeTime)/100F);
            vertexConsumer.addVertex(matrix, renderDistance, 100, renderDistance).setUv(1.0F, 1.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(DelugeClient.fadeTime)/100F);
            vertexConsumer.addVertex(matrix, -renderDistance, 100, renderDistance).setUv(0.0F, 1.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(DelugeClient.fadeTime)/100F);
        }
        if(texture != null) {
            var type = DelugeRenderTypes.SKY_DETAILS.apply(texture);
            VertexConsumer vertexConsumer = bufferSource.getBuffer(type);
            vertexConsumer.addVertex(matrix, -renderDistance, 100, -renderDistance).setUv(0.0F, 0.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(100 - DelugeClient.fadeTime)/100F);
            vertexConsumer.addVertex(matrix, renderDistance, 100, -renderDistance).setUv(1.0F, 0.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(100 - DelugeClient.fadeTime)/100F);
            vertexConsumer.addVertex(matrix, renderDistance, 100, renderDistance).setUv(1.0F, 1.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(100 - DelugeClient.fadeTime)/100F);
            vertexConsumer.addVertex(matrix, -renderDistance, 100, renderDistance).setUv(0.0F, 1.0F).setColor(cloudVec4.x,cloudVec4.y,cloudVec4.z,(float)(100 - DelugeClient.fadeTime)/100F);
        }
        poseStack.popPose();
    }

    private static void deluge$drawRect(GuiGraphics gfx, int x, int y, int w, int h, int c) {
        gfx.fill(RenderType.gui(),x,y,x+w,y+h,c);
    }

    public static void renderDebugOverlay(GuiGraphics gfx) {
        var mc = Minecraft.getInstance();
        if(!mc.hasSingleplayerServer() || !clientConfig.showDebugOverlay)
            return;
        var player = mc.player;
        var chunkPos = player.chunkPosition();
        var scale = (float)mc.getWindow().getGuiScale();
        var width = (int)Math.floor((gfx.guiWidth()*scale)/2);
        var height = (int)Math.floor((gfx.guiHeight()*scale)/2);
        gfx.pose().pushPose();
        gfx.pose().scale((1F/scale)*2,(1F/scale)*2,1F);
        for(int y=-64; y < 64; y++) {
            for(int x=-64; x < 64; x++) {
                var vValue = Deluge.getEventNoise(mc.level,chunkPos.x+x,chunkPos.z+y);
                var event = Deluge.selectEventType(vValue);
                var rValue = Deluge.getRainNoise(mc.level,chunkPos.x+x,chunkPos.z+y);
                deluge$drawRect(gfx,width-64+x,height-64+y,1,1,0xff000000 | ARGB.greyscale(ARGB.as8BitChannel((vValue+1.0F)/2F)));
                if(event.canRain() && rValue > 0) {
                    deluge$drawRect(gfx, width - 64 + x, height - 64 + y, 1, 1, ARGB.colorFromFloat(rValue, 0F, 1F, 1F));
                }
            }
        }
        deluge$drawRect(gfx,width - 64, height - 64, 1, 1, ARGB.colorFromFloat(0.75F,1F,0F,0F));
        var font = Minecraft.getInstance().font;
        var windAngleStr = String.format("Wind Angle: %.4f", clientWindManager.angle);
        var windSpeedStr = String.format("Wind Speed: %.4f", clientWindManager.speed);
        gfx.drawString(font,windAngleStr,width - 128 - font.width(windAngleStr),height - 128,0xffffffff);
        gfx.drawString(font,windSpeedStr,width - 128 - font.width(windSpeedStr),height - 128 + font.lineHeight,0xffffffff);

        gfx.pose().popPose();
    }
}
