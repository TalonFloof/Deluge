package sh.talonfloof.deluge.wind;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.LevelResource;
import sh.talonfloof.deluge.Deluge;
import sh.talonfloof.deluge.network.WindUpdatePacket;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import static sh.talonfloof.deluge.Deluge.commonConfig;

public class ServerWindManager implements IWindAccessor {
    public float angle = 0; // In Degrees
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

    public static final float WIND_MAX_SPEED = 0.200F;
    public static final float WIND_SPEED_MARGIN = 0.030F;

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
            targetAngleInitialTime = Math.round(Math.min(1F,Math.abs((targetAngle-angle)/180F))*300F);
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

    public void save(MinecraftServer server) {
        var map = new HashMap<String, Object>();
        map.put("angle",angle);
        map.put("initialAngle",initialAngle);
        map.put("targetAngle",targetAngle);
        map.put("targetAngleTime",targetAngleTime);
        map.put("targetAngleInitialTime",targetAngleInitialTime);
        map.put("initialSpeed",initialSpeed);
        map.put("targetSpeed",targetSpeed);
        map.put("targetSpeedTime",targetSpeedTime);
        map.put("speed",speed);

        var path = server.getWorldPath(LevelResource.ROOT).toAbsolutePath().resolve("deluge").resolve("wind.toml");
        var file = path.toFile();
        try {
            path.getParent().toFile().mkdir();
            file.delete();
            file.createNewFile();
            new TomlWriter().write(map, file);
            Deluge.LOG.info("Saved wind data to TOML");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void load(MinecraftServer server) {
        var path = server.getWorldPath(LevelResource.ROOT).toAbsolutePath().resolve("deluge").resolve("wind.toml");
        var file = path.toFile();
        if(file.exists()) {
            var toml = new Toml().read(file);
            angle = toml.getDouble("angle").floatValue();
            initialAngle = toml.getDouble("initialAngle").floatValue();
            targetAngle = toml.getDouble("targetAngle").floatValue();
            targetAngleTime = toml.getLong("targetAngleTime").intValue();
            targetAngleInitialTime = toml.getLong("targetAngleInitialTime").intValue();
            initialSpeed = toml.getDouble("initialSpeed").floatValue();
            targetSpeed = toml.getDouble("targetSpeed").floatValue();
            targetSpeedTime = toml.getLong("targetSpeedTime").intValue();
            speed = toml.getDouble("speed").floatValue();
            Deluge.LOG.info("Loaded wind data from TOML");
        } else {
            angle = rand.nextInt(360);
            initialAngle = 0;
            targetAngle = 0;
            targetAngleTime = 0;
            targetAngleInitialTime = 0;
            initialSpeed = 0;
            targetSpeed = 0;
            targetSpeedTime = 0;
            speed = rand.nextFloat(WIND_MAX_SPEED);
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
