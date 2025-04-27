package sh.talonfloof.deluge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import sh.talonfloof.deluge.Deluge;

@EventBusSubscriber(modid = Deluge.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DelugeNeoForgeClientMain {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        DelugeClient.init();
    }
}
