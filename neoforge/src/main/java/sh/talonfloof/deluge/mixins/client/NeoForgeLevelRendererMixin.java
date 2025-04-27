package sh.talonfloof.deluge.mixins.client;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.LevelRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.deluge.client.DelugeRendering;

@Mixin(LevelRenderer.class)
public class NeoForgeLevelRendererMixin {
    @Inject(method = "lambda$addSkyPass$13", at = @At("HEAD"))
    public void deluge$neoSkyRender(float partialTick, Matrix4f modelViewMatrix, Camera camera, Matrix4f projectionMatrix, FogParameters fog, DimensionSpecialEffects.SkyType dimensionspecialeffects$skytype, DimensionSpecialEffects dimensionspecialeffects, CallbackInfo ci) {
        DelugeRendering.renderSky((LevelRenderer)(Object)this,partialTick);
    }
}
