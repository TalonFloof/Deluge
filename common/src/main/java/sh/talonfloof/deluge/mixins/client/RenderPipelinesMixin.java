package sh.talonfloof.deluge.mixins.client;

import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.deluge.client.DelugeRenderPipelines;

@Mixin(RenderPipelines.class)
public class RenderPipelinesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void deluge$customPipelineRegister(CallbackInfo ci) {
        RenderPipelines.PIPELINES_BY_LOCATION.put(DelugeRenderPipelines.SKY_DETAILS.getLocation(),DelugeRenderPipelines.SKY_DETAILS);
    }
}
