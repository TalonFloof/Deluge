package sh.talonfloof.deluge.neoforge;

import sh.talonfloof.deluge.Deluge;
import net.neoforged.fml.common.Mod;

@Mod(Deluge.MOD_ID)
public final class DelugeNeoForge {
    public DelugeNeoForge() {
        // Run our common setup.
        Deluge.init();
    }
}
