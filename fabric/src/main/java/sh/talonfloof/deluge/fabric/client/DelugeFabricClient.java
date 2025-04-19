package sh.talonfloof.deluge.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import sh.talonfloof.deluge.client.DelugeClient;

public final class DelugeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        DelugeClient.init();
    }
}
