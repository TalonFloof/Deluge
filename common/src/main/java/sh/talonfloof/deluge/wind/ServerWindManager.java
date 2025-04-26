package sh.talonfloof.deluge.wind;

import net.minecraft.util.Mth;

import java.util.Random;

import static sh.talonfloof.deluge.Deluge.commonConfig;

public class ServerWindManager implements IWindAccessor {
    public float angle = 0; // In Degrees
    public float prevAngle = 0;
    public float initialAngle = 0;
    public float targetAngle = 0;
    public int targetAngleTime = 0; // 600 tick (30 second) max if turn was =360 degrees
    public int targetAngleInitialTime = 0; // Used to track the progress of the angle change, needed to properly lerp values
    public float prevSpeed = 0;
    public float speed = 0;
    public Random rand = new Random();

    public void tick() {
        prevSpeed = speed;
        prevAngle = angle;
        if(targetAngleTime > 0) {
            targetAngleTime--;
            angle = Mth.lerp((float)(targetAngleInitialTime-targetAngleTime)/targetAngleInitialTime,initialAngle,targetAngle);
        }
        if(targetAngleTime <= 0 && rand.nextInt(commonConfig.wind.windAngleChangeChance) == 0) {
            // Select a new angle and calculate the direction and distance needed to get to it (on a unit circle given the angle is theta)
           targetAngle = rand.nextInt(360);
           targetAngleInitialTime = Math.round(((targetAngle-angle)/360F)*600F);
           targetAngleTime = targetAngleInitialTime;
        }
    }
}
