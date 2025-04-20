package sh.talonfloof.deluge.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sh.talonfloof.deluge.Deluge;

import java.util.Map;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @WrapOperation(method = "createLevels", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private <K, V> V deluge$onLoadLevel(Map<K, V> levels, K registryKey, V serverLevel, Operation<V> original) {
        final V result = original.call(levels, registryKey, serverLevel);
        Deluge.onLevelLoad((MinecraftServer) (Object) this, (ServerLevel)serverLevel);
        return result;
    }
}
