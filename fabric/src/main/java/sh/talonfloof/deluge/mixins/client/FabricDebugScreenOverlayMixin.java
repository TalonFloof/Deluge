package sh.talonfloof.deluge.mixins.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.deluge.client.DelugeRendering;

@Mixin(DebugScreenOverlay.class)
public class FabricDebugScreenOverlayMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;drawSystemInformation(Lnet/minecraft/client/gui/GuiGraphics;)V", shift = At.Shift.AFTER))
    public void deluge$fabricDebugRender(GuiGraphics gfx, CallbackInfo ci) {
        DelugeRendering.renderDebugOverlay(gfx);
    }
}
