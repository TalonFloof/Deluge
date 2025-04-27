package sh.talonfloof.deluge.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProgressListener;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.deluge.Deluge;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ServerLevel;handlingTick:Z", opcode = Opcodes.PUTFIELD, ordinal = 0, shift = At.Shift.AFTER))
    public void deluge$levelTickStart(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        Deluge.onServerLevelTick((ServerLevel)(Object)this);
    }
    @Inject(method = "save", at = @At("TAIL"))
    public void deluge$saveEvent(ProgressListener progress, boolean flush, boolean skipSave, CallbackInfo ci) {
        if(!skipSave) {

        }
    }
}
