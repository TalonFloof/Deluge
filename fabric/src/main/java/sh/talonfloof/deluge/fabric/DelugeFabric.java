package sh.talonfloof.deluge.fabric;

import sh.talonfloof.deluge.Deluge;
import net.fabricmc.api.ModInitializer;

public final class DelugeFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Deluge.init();
    }
}
