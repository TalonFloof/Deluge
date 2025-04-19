package sh.talonfloof.deluge;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sh.talonfloof.deluge.config.DelugeConfig;
import sh.talonfloof.deluge.utils.FastNoiseLite;

public final class Deluge {
    public static final String MOD_ID = "deluge";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

    public static DelugeConfig commonConfig = ConfigApiJava.registerAndLoadConfig(DelugeConfig::new, RegisterType.BOTH);

    public static FastNoiseLite voronoiEventNoise;

    static {
        voronoiEventNoise = new FastNoiseLite();
        voronoiEventNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
        voronoiEventNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
    }

    public static void init() {
        // Write common init code here.
    }

    public static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID,path);
    }
}
