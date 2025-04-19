package sh.talonfloof.deluge.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import sh.talonfloof.deluge.DelugeEventType;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static sh.talonfloof.deluge.Deluge.LOG;

public class DelugeIrisCompat {
    static {
        try {
            Class<?> api = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            var programEnum = Class.forName("net.irisshaders.iris.api.v0.IrisProgram");
            var apiInstance = api.cast(api.getDeclaredMethod("getInstance").invoke(null));
            var handle = MethodHandles.lookup().findVirtual(api, "assignPipeline", MethodType.methodType(void.class, List.of(RenderPipeline.class, programEnum)));
            var pipelineHandle = ((MethodHandle)MethodHandles.lookup().findStatic(programEnum, "valueOf", MethodType.methodType(programEnum,String.class)));
            handle.invoke(apiInstance,DelugeRenderPipelines.SKY_DETAILS,pipelineHandle.invoke("SKY_TEXTURED"));
            LOG.info("Iris Compatibility Enabled!");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void init() {

    }
}
