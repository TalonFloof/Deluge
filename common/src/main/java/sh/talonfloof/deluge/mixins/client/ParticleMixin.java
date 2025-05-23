package sh.talonfloof.deluge.mixins.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.joml.Vector2d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static sh.talonfloof.deluge.client.DelugeClient.clientWindManager;

@Mixin(Particle.class)
public abstract class ParticleMixin {
    @Shadow protected boolean hasPhysics;

    @Shadow public abstract void setParticleSpeed(double xd, double yd, double zd);

    @Shadow protected double xd;
    @Shadow protected double yd;
    @Shadow protected double zd;

    @Shadow @Final protected ClientLevel level;

    @Shadow protected double x;
    @Shadow protected double y;
    @Shadow protected double z;

    @Inject(method = "tick", at = @At("HEAD"))
    public void deluge$moveWithWind(CallbackInfo ci) {
        if(this.hasPhysics) {
            if(!level.canSeeSky(new BlockPos((int)x,(int)y,(int)z)))
                return;
            if(this.getClass().getSuperclass() != null && this.getClass().getSuperclass().getName().equals("pigcart.particlerain.particle.WeatherParticle"))
                return;
            var windMovement = clientWindManager.getWindVec(1);
            var movement = new Vector2d(xd,zd);
            if(movement.length() <= windMovement.length()) {
                xd = Mth.lerp(0.2,xd,windMovement.x);
                zd = Mth.lerp(0.2,zd,windMovement.y);
            }
        }
    }
}
