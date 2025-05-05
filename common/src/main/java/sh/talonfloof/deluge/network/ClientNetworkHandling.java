package sh.talonfloof.deluge.network;

import me.fzzyhmstrs.fzzy_config.networking.api.ClientPlayNetworkContext;
import sh.talonfloof.deluge.client.DelugeClient;

import static sh.talonfloof.deluge.client.DelugeClient.clientWindManager;

public class ClientNetworkHandling {
    public static void handle(EventUpdatePacket payload, ClientPlayNetworkContext context) {
        DelugeClient.changeEvent(payload.eventType());
        DelugeClient.rainLevel = payload.rainLevel();
        DelugeClient.thunderLevel = payload.thunderLevel();
    }
    public static void handle(WindUpdatePacket payload, ClientPlayNetworkContext context) {
        clientWindManager.angle = payload.angle();
        clientWindManager.speed = payload.speed();
    }
}
