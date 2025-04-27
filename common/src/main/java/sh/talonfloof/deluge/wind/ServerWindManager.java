package sh.talonfloof.deluge.wind;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import sh.talonfloof.deluge.network.WindUpdatePacket;

import java.util.Random;

import static sh.talonfloof.deluge.Deluge.commonConfig;

public class ServerWindManager implements IWindAccessor {
    public float angle = new Random().nextInt(360); // In Degrees
    public float prevAngle = 0;
    public float initialAngle = 0;
    public float targetAngle = 0;
    public int targetAngleTime = 0;
    public int targetAngleInitialTime = 0; // Used to track the progress of the angle change, needed to properly lerp values
    public float initialSpeed = 0;
    public float targetSpeed = 0;
    public int targetSpeedTime = 0;
    public float prevSpeed = 0;
    public float speed = 0;
    public Random rand = new Random();

    public static final float WIND_MAX_SPEED = 0.050F;
    public static final float WIND_SPEED_MARGIN = 0.010F;

    public void tick(ServerLevel level) {
        var sendUpdate = false;
        prevSpeed = speed;
        prevAngle = angle;
        if(targetAngleTime > 0) {
            targetAngleTime--;
            angle = Mth.lerp((float)(targetAngleInitialTime-targetAngleTime)/targetAngleInitialTime,initialAngle,targetAngle);
            sendUpdate = true;
        }
        if(targetSpeedTime > 0) {
            targetSpeedTime--;
            speed = Mth.lerp((float)(100-targetSpeedTime)/100,initialSpeed,targetSpeed);
            sendUpdate = true;
        }
        if(targetAngleTime <= 0 && rand.nextInt(commonConfig.wind.windAngleChangeChance) == 0) {
            // Select a new angle and calculate the direction and distance needed to get to it (on a unit circle given the angle is theta)
            initialAngle = angle;
            targetAngle = rand.nextInt(360);
            targetAngleInitialTime = Math.round(Math.min(1F,(targetAngle-angle)/180F)*300F);
            targetAngleTime = targetAngleInitialTime;
        }
        if(targetSpeedTime <= 0 && rand.nextInt(commonConfig.wind.windSpeedChangeChance) == 0) {
            initialSpeed = speed;
            targetSpeed = Math.clamp((rand.nextFloat() * (WIND_MAX_SPEED + WIND_SPEED_MARGIN)) - (WIND_SPEED_MARGIN/2F),0F,WIND_MAX_SPEED);
            targetSpeedTime = 100;
        }
        if(sendUpdate) {
            for(var player : level.players()) {
                WindUpdatePacket.send(player,angle,speed);
            }
        }
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
