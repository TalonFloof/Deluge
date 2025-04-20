package sh.talonfloof.deluge.client;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.minecraft.client.Minecraft;
import sh.talonfloof.deluge.DelugeEventType;
import sh.talonfloof.deluge.client.config.DelugeClientConfig;

public class DelugeClient {
    public static DelugeEventType previousEvent = DelugeEventType.CLEAR;
    public static DelugeEventType currentEvent = DelugeEventType.CLEAR;
    public static int fadeTime = 0;

    public static void changeEvent(DelugeEventType newEvent) {
        if(newEvent != currentEvent) {
            previousEvent = currentEvent;
            currentEvent = newEvent;
            if(fadeTime <= 0)
                fadeTime = 100;
            else
                fadeTime = 100-fadeTime;
        }
    }

    public static DelugeClientConfig clientConfig = ConfigApiJava.registerAndLoadConfig(DelugeClientConfig::new, RegisterType.CLIENT);

    public static void init() {
        DelugeIrisCompat.init();
    }

    public static void onClientTick(Minecraft mc) {
        if(mc.level != null && !mc.isPaused()) {
            if (fadeTime > 0) {
                fadeTime--;
            } else if(previousEvent != currentEvent) {
                previousEvent = currentEvent;
            }
        }
    }
}
