package sh.talonfloof.deluge;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public enum DelugeEventType {
    CLEAR(null,null),
    MAMMATUS(new Vector3f(72/255F,94/255F,133/255F),Deluge.path("textures/mammatus.png")),
    OVERCAST(new Vector3f(109/255F,112/255F,120/255F),Deluge.path("textures/storm.png"));

    private final Vector3f fogColor;
    private final ResourceLocation texture;

    DelugeEventType(@Nullable Vector3f fogColor, @Nullable ResourceLocation texture) {
        this.fogColor = fogColor;
        this.texture = texture;
    }

    public Vector3f getFogColor() {
        return fogColor;
    }
    public ResourceLocation getTexture() {
        return texture;
    }
}
