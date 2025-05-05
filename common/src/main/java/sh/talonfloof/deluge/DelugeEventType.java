package sh.talonfloof.deluge;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public enum DelugeEventType {
    CLEAR(null,null,false,false, 1F),
    CIRRUS(null,Deluge.path("textures/cirrus.png"),false,false, 1F),
    ALTOCUMULUS(null,Deluge.path("textures/altocumulus.png"),false,false, 1F),
    CUMULONIMBUS(new Vector3f(109/255F,112/255F,120/255F),Deluge.path("textures/storm.png"),true,true, 1F),
    NIMBOSTRATUS(new Vector3f(109/255F,112/255F,120/255F),Deluge.path("textures/nimbostratus.png"),true,false, 1F),
    MAMMATUS(new Vector3f(72/255F,94/255F,133/255F),Deluge.path("textures/mammatus.png"),false,false, 1F),
    SUPERCELL(new Vector3f(109/255F,112/255F,120/255F),Deluge.path("textures/storm.png"),true,true, 4F);

    private final Vector3f fogColor;
    private final ResourceLocation texture;
    private final boolean canRain;
    private final boolean canThunder;
    private final float rotationMultiplier;

    DelugeEventType(@Nullable Vector3f fogColor, @Nullable ResourceLocation texture, boolean canRain, boolean canThunder, float rotationMultiplier) {
        this.fogColor = fogColor;
        this.texture = texture;
        this.canRain = canRain;
        this.canThunder = canThunder;
        this.rotationMultiplier = rotationMultiplier;
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
    public boolean canThunder() {
        return canThunder;
    }
    public float getRotationMultiplier() {
        return rotationMultiplier;
    }
}
