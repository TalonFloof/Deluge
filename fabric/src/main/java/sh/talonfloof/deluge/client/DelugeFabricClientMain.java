package sh.talonfloof.deluge.client;

import net.fabricmc.api.ClientModInitializer;

public class DelugeFabricClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DelugeClient.init();
    }
}
