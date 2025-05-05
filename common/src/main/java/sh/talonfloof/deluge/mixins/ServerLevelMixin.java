package sh.talonfloof.deluge.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.deluge.Deluge;

import java.util.function.BooleanSupplier;

import static sh.talonfloof.deluge.Deluge.windManager;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Shadow @Final private MinecraftServer server;

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ServerLevel;handlingTick:Z", opcode = Opcodes.PUTFIELD, ordinal = 0, shift = At.Shift.AFTER))
    public void deluge$levelTickStart(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        Deluge.onServerLevelTick((ServerLevel)(Object)this);
    }
    @Inject(method = "save", at = @At("TAIL"))
    public void deluge$saveEvent(ProgressListener progress, boolean flush, boolean skipSave, CallbackInfo ci) {
        if(!skipSave) {
            if(((ServerLevel)(Object)this).dimensionTypeRegistration().unwrapKey().get().equals(BuiltinDimensionTypes.OVERWORLD))
                windManager.save(((ServerLevel)(Object)this).getServer());
        }
    }

    @Redirect(method = "tickThunder", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isRaining()Z"))
    public boolean deluge$thunderLocalTick1(ServerLevel instance) {
        return true;
    }
    @Redirect(method = "tickThunder", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isThundering()Z"))
    public boolean deluge$thunderLocalTick2(ServerLevel instance) {
        return true;
    }
    @Redirect(method = "tickThunder", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isRainingAt(Lnet/minecraft/core/BlockPos;)Z", ordinal = 0))
    public boolean deluge$thunderLocalTick3(ServerLevel instance, BlockPos pos) {
        return Deluge.getThunderLevel(instance, pos.getX()/16, pos.getZ()/16) > 0F;
    }
}
