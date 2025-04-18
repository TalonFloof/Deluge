package sh.talonfloof.deluge.client;

import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;

import java.util.function.Function;

public class DelugeRenderTypes {
    public static final Function<ResourceLocation, RenderType> SKY_DETAILS = Util.memoize((resourceLocation) -> {
        return RenderType.create("deluge_sky_details",1536,false,false,DelugeRenderPipelines.SKY_DETAILS, RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, TriState.FALSE, false)).createCompositeState(false));
    });
}
