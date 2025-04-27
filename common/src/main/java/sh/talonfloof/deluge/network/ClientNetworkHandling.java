package sh.talonfloof.deluge.network;

import me.fzzyhmstrs.fzzy_config.networking.api.ClientPlayNetworkContext;
import sh.talonfloof.deluge.client.DelugeClient;

public class ClientNetworkHandling {
    public static void handle(EventUpdatePacket payload, ClientPlayNetworkContext context) {
        DelugeClient.changeEvent(payload.eventType());
        DelugeClient.rainLevel = payload.rainLevel();
    }
}
