package sh.talonfloof.deluge.client;

import sh.talonfloof.deluge.DelugeEventType;

public class DelugeClient {
    public static DelugeEventType currentEvent = DelugeEventType.OVERCAST;

    public static void init() {
        DelugeIrisCompat.init();
    }
}
