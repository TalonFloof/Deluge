package sh.talonfloof.deluge.client;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import sh.talonfloof.deluge.Deluge;

public class DelugeRenderPipelines {
    public static final RenderPipeline SKY_DETAILS = RenderPipeline.builder(new RenderPipeline.Snippet[]{RenderPipelines.MATRICES_COLOR_SNIPPET}).withLocation(Deluge.path("pipeline/sky_details")).withVertexShader("core/position_tex_color").withFragmentShader("core/position_tex_color").withSampler("Sampler0").withBlend(BlendFunction.TRANSLUCENT).withDepthWrite(false).withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS).build();
}
