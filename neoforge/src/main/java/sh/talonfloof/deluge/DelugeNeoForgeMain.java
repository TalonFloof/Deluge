package sh.talonfloof.deluge;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Deluge.MOD_ID)
public class DelugeNeoForgeMain {
    public DelugeNeoForgeMain(IEventBus eventBus) {
        Deluge.init();
    }
}