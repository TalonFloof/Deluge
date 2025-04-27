package sh.talonfloof.deluge.mixins.client;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.deluge.client.DelugeRendering;

@Mixin(LevelRenderer.class)
public class FabricLevelRendererMixin {
    @Inject(method = "method_62215", at = @At("HEAD"))
    public void deluge$fabricSkyRender(FogParameters fogParameters, DimensionSpecialEffects.SkyType skyType, float f, DimensionSpecialEffects dimensionSpecialEffects, CallbackInfo ci) {
        DelugeRendering.renderSky((LevelRenderer)(Object)this,f);
    }
}
