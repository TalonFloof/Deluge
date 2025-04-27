package sh.talonfloof.deluge;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public enum DelugeEventType {
    CLEAR(null,null,false),
    CIRRUS(null,Deluge.path("textures/cirrus.png"),false),
    ALTOCUMULUS(null,Deluge.path("textures/altocumulus.png"),false),
    CUMULONIMBUS(new Vector3f(109/255F,112/255F,120/255F),Deluge.path("textures/storm.png"),true),
    NIMBOSTRATUS(new Vector3f(109/255F,112/255F,120/255F),Deluge.path("textures/nimbostratus.png"),true),
    MAMMATUS(new Vector3f(72/255F,94/255F,133/255F),Deluge.path("textures/mammatus.png"),false);

    private final Vector3f fogColor;
    private final ResourceLocation texture;
    private final boolean canRain;

    DelugeEventType(@Nullable Vector3f fogColor, @Nullable ResourceLocation texture, boolean canRain) {
        this.fogColor = fogColor;
        this.texture = texture;
        this.canRain = canRain;
    }

    public Vector3f getFogColor() {
        return fogColor;
    }
    public ResourceLocation getTexture() {
        return texture;
    }
    public boolean canRain() {
        return canRain;
    }
}
