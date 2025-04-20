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
    @Redirect(method = "renderSkyDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 0))
    public void deluge$fadeSkyDisc(float red, float green, float blue, float alpha) {
        var initialColor = new Vector4f(red,green,blue,alpha);
        var mc = Minecraft.getInstance();
        var cloudColor = mc.level.getCloudColor(0);
        var vec = ARGB.vector3fFromRGB24(cloudColor);
        var previousFogColorVec = DelugeClient.previousEvent.getFogColor() != null ? new Vector4f(DelugeClient.previousEvent.getFogColor().mul(vec),1.0F) : initialColor;
        var currentFogColorVec = DelugeClient.currentEvent.getFogColor() != null ? new Vector4f(DelugeClient.currentEvent.getFogColor().mul(vec),1.0F) : initialColor;
        var previousFogColor = ARGB.color(255,ARGB.as8BitChannel(previousFogColorVec.x),ARGB.as8BitChannel(previousFogColorVec.y),ARGB.as8BitChannel(previousFogColorVec.z));
        var currentFogColor = ARGB.color(255,ARGB.as8BitChannel(currentFogColorVec.x),ARGB.as8BitChannel(currentFogColorVec.y),ARGB.as8BitChannel(currentFogColorVec.z));
        var finalColor = ARGB.lerp((float)(100-DelugeClient.fadeTime)/100F,previousFogColor,currentFogColor);
        RenderSystem.setShaderColor(ARGB.redFloat(finalColor),ARGB.greenFloat(finalColor),ARGB.blueFloat(finalColor),alpha);
    }
}
