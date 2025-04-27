package sh.talonfloof.deluge.wind;

import net.minecraft.util.Mth;

public class ClientWindManager implements IWindAccessor {
    public float angle = 0;
    public float prevAngle = 0;
    public float prevSpeed = 0;
    public float speed = 0;

    public void tick() {
        prevSpeed = speed;
        prevAngle = angle;
    }

    @Override
    public float getWindAngle(float tickDelta) {
        return Mth.lerp(tickDelta,prevAngle,angle);
    }

    @Override
    public float getWindSpeed(float tickDelta) {
        return Mth.lerp(tickDelta,prevSpeed,speed);
    }
}
