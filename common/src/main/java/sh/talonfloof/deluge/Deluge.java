package sh.talonfloof.deluge;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.resources.ResourceLocation;

public final class Deluge {
    public static final String MOD_ID = "deluge";

    public static void init() {
        // Write common init code here.
    }

    public static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID,path);
    }
}
