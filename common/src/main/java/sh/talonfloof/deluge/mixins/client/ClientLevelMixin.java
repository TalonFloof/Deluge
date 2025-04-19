package sh.talonfloof.deluge.mixins.client;

import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sh.talonfloof.deluge.client.DelugeClient;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Redirect(method = "getSkyDarken", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"))
    public float deluge$terrainDarkenSub1(ClientLevel instance, float v) {
        if(DelugeClient.currentEvent.getFogColor() != null)
            return 1F;
        return instance.getRainLevel(v);
    }
    @Redirect(method = "getSkyDarken", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getThunderLevel(F)F"))
    public float deluge$terrainDarkenSub2(ClientLevel instance, float v) {
        if(DelugeClient.currentEvent.getFogColor() != null)
            return 1F;
        return instance.getThunderLevel(v);
    }
}
