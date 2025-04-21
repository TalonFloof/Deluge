package sh.talonfloof.deluge.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.deluge.client.DelugeClient;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleLogin", at=@At("TAIL"))
    public void deluge$clientPlayLoginEvent(ClientboundLoginPacket packet, CallbackInfo ci) {
        DelugeClient.onClientJoin(Minecraft.getInstance().level);
    }
}
