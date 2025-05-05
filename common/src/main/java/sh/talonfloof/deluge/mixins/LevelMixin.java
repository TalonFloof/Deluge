package sh.talonfloof.deluge.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sh.talonfloof.deluge.Deluge;

@Mixin(Level.class)
public class LevelMixin {
    @Inject(method = "isRainingAt(Lnet/minecraft/core/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
    private void deluge$checkIfInRain(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Level self = (Level)(Object)this;
        if(!self.isClientSide()) {
            if (!self.canSeeSky(pos)) {
                cir.setReturnValue(false);
            } else if (self.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()) {
                cir.setReturnValue(false);
            } else {
                Biome biome = self.getBiome(pos).value();
                if (biome.getPrecipitationAt(pos, self.getSeaLevel()) == Biome.Precipitation.RAIN) {
                    cir.setReturnValue(Deluge.getRainLevel(self, pos.getX() / 16, pos.getZ() / 16) > 0);
                } else {
                    cir.setReturnValue(false);
                }
            }
            cir.cancel();
        }
    }
}
