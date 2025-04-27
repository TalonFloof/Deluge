package sh.talonfloof.deluge;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.networking.api.NetworkApi;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sh.talonfloof.deluge.config.DelugeConfig;
import sh.talonfloof.deluge.network.ClientNetworkHandling;
import sh.talonfloof.deluge.network.EventUpdatePacket;
import sh.talonfloof.deluge.network.WindUpdatePacket;
import sh.talonfloof.deluge.utils.FastNoiseLite;
import sh.talonfloof.deluge.wind.ServerWindManager;

import java.util.Random;

public final class Deluge {
    public static final String MOD_ID = "deluge";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);
    public static NetworkApi NETWORK;

    public static DelugeConfig commonConfig = ConfigApiJava.registerAndLoadConfig(DelugeConfig::new, RegisterType.BOTH);

    public static FastNoiseLite voronoiEventNoise;
    public static FastNoiseLite simplexPrecipitationNoise;
    public static int refreshTick = 0;

    public static ServerWindManager windManager = new ServerWindManager();

    static {
        voronoiEventNoise = new FastNoiseLite();
        voronoiEventNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
        voronoiEventNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
        voronoiEventNoise.SetCellularDistanceFunction(FastNoiseLite.CellularDistanceFunction.Hybrid);
        voronoiEventNoise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2Reduced);
        voronoiEventNoise.SetDomainWarpAmp(34F);
        voronoiEventNoise.SetFrequency(0.015F);
        simplexPrecipitationNoise = new FastNoiseLite();
        simplexPrecipitationNoise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S);
        simplexPrecipitationNoise.SetFrequency(0.004F);
    }

    public static void init() {
        NETWORK = ConfigApiJava.network(); // TODO: Create our own cross-platform library to handle this stuff, instead of using Fzzy Config to handle it
        NETWORK.registerS2C(EventUpdatePacket.TYPE,EventUpdatePacket.STREAM_CODEC, ClientNetworkHandling::handle);
        NETWORK.registerS2C(WindUpdatePacket.TYPE,WindUpdatePacket.STREAM_CODEC, ClientNetworkHandling::handle);
    }

    public static void onServerLevelTick(ServerLevel level) {
        if(level.dimensionTypeRegistration().unwrapKey().get().equals(BuiltinDimensionTypes.OVERWORLD)) {
            windManager.tick(level);
            refreshTick++;
            if (refreshTick >= 20) {
                refreshTick = 0;
                for (var player : level.players()) {
                    var chunkPos = player.chunkPosition();
                    var value = selectEventType(getEventNoise(level, chunkPos.x, chunkPos.z));
                    var rainValue = getRainNoise(level, chunkPos.x, chunkPos.z);
                    EventUpdatePacket.send(player, value, rainValue);
                }
            }
        }
    }

    private static int RARE_RANGE_BEGIN = 5;

    public static float getEventNoise(Level level, double x, double z) {
        return voronoiEventNoise.GetNoise(x, z, ((double)level.getGameTime()/1000));
    }

    public static float getRainNoise(Level level, double x, double z) {
        return Math.clamp(simplexPrecipitationNoise.GetNoise(x,z, ((double)level.getGameTime()/100)),0F,.250F)/.250F;
    }

    public static DelugeEventType selectEventType(float value) {
        var rand = new Random(Float.floatToIntBits(value));
        var len = DelugeEventType.values().length;
        if(rand.nextInt(10) == 0) {
            // Select a rare event
            return DelugeEventType.values()[rand.nextInt(RARE_RANGE_BEGIN,len)];
        }
        return DelugeEventType.values()[rand.nextInt(RARE_RANGE_BEGIN)];
    }

    public static void onLevelLoad(MinecraftServer server, ServerLevel level) {
        if(level.dimensionTypeRegistration().unwrapKey().get().equals(BuiltinDimensionTypes.OVERWORLD)) {
            LOG.info("Loading Voronoi Noise with seed {}", (int) level.getSeed());
            voronoiEventNoise.SetSeed((int) level.getSeed());
            simplexPrecipitationNoise.SetSeed((int) (level.getSeed() + 1));
        }
    }

    public static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID,path);
    }
}
