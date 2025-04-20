package sh.talonfloof.deluge.mixins.client;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.deluge.client.DelugeClient;

@Mixin(Minecraft.class) // TODO: Create a cross-platform library to handle events, instead of using mixins
public class MinecraftMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    public void deluge$clientTickBegin(CallbackInfo ci) {
        DelugeClient.onClientTick((Minecraft)(Object)this);
    }
}
