package sh.talonfloof.deluge.mixins;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.deluge.network.WindUpdatePacket;

import static sh.talonfloof.deluge.Deluge.windManager;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    public void deluge$newPlayerSetup(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        WindUpdatePacket.send(player,windManager.angle,windManager.speed);
    }
}
