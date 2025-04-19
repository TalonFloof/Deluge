package sh.talonfloof.deluge.mixins.client;

import net.minecraft.client.CloudStatus;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sh.talonfloof.deluge.client.DelugeClient;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Redirect(method = { "method_62215", "lambda$addSkyPass$13" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F")) // lambda of addSkyPass
    public float deluge$overcastCelestialRemoval(ClientLevel instance, float v) {
        return DelugeClient.currentEvent.getFogColor() != null ? 1 : 0;
    }
    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getCloudsType()Lnet/minecraft/client/CloudStatus;"))
    public CloudStatus deluge$disableClouds(Options instance) {
        return CloudStatus.OFF;
    }
}
