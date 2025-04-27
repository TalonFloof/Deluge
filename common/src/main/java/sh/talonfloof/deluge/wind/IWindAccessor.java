package sh.talonfloof.deluge.wind;

import org.joml.Vector2d;

public interface IWindAccessor {
    float getWindAngle(float tickDelta);
    float getWindSpeed(float tickDelta);

    default Vector2d getWindVec(float tickDelta) {
        var angle = Math.toRadians(getWindAngle(tickDelta) + 90F);
        var speed = getWindSpeed(tickDelta);
        double moveX = (double)speed * Math.cos(angle);
        double moveZ = (double)speed * Math.sin(angle);
        return new Vector2d(moveX * 2F, moveZ * 2F);
    }
}
