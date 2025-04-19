package sh.talonfloof.deluge.client;

import net.fabricmc.api.ClientModInitializer;

public class DelugeFabricClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        DelugeClient.init();
    }
}
