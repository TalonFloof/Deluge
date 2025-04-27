package sh.talonfloof.deluge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import sh.talonfloof.deluge.Deluge;

@EventBusSubscriber(modid = Deluge.MOD_ID, value = Dist.CLIENT)
public class DelugeNeoForgeClientEvents {
    @SubscribeEvent
    public static void onDebugOverlayRender(CustomizeGuiOverlayEvent.DebugText event) {
        DelugeRendering.renderDebugOverlay(event.getGuiGraphics());
    }
}
