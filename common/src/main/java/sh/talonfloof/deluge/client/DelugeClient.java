package sh.talonfloof.deluge.client;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import sh.talonfloof.deluge.DelugeEventType;
import sh.talonfloof.deluge.client.config.DelugeClientConfig;

public class DelugeClient {
    public static DelugeEventType currentEvent = DelugeEventType.CIRRUS;

    public static DelugeClientConfig clientConfig = ConfigApiJava.registerAndLoadConfig(DelugeClientConfig::new, RegisterType.CLIENT);

    public static void init() {
        DelugeIrisCompat.init();
    }
}
