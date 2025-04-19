package sh.talonfloof.deluge;

import net.fabricmc.api.ModInitializer;

public class DelugeFabricMain implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Deluge.init();
    }
}
