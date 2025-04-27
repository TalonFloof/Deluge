package sh.talonfloof.deluge.mixins.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static sh.talonfloof.deluge.client.DelugeClient.clientWindManager;

@Mixin(FallingLeavesParticle.class)
public abstract class FallingLeavesParticleMixin extends TextureSheetParticle {
    protected FallingLeavesParticleMixin(ClientLevel p_108323_, double p_108324_, double p_108325_, double p_108326_) {
        super(p_108323_, p_108324_, p_108325_, p_108326_);
    }

    @Inject(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/particle/FallingLeavesParticle;flowAway:Z", ordinal = 0), cancellable = true)
    public void deluge$leavesWithWindTick(CallbackInfo ci) {
        if(clientWindManager.speed > .0100F) {
            var windMovement = clientWindManager.getWindVec(1).mul(2F);
            xd = Mth.lerp(0.2,xd,windMovement.x);
            zd = Mth.lerp(0.2,zd,windMovement.y);
            super.tick();
            ci.cancel();
        }
    }
}
