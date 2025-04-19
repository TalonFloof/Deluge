package sh.talonfloof.deluge.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;

import static sh.talonfloof.deluge.Deluge.LOG;

public class DelugeIrisCompat {
    private static Class<?> api;
    private static Object apiInstance;
    private static MethodHandle isShaderPackInUse;

    static {
        try {
            api = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            var programEnum = Class.forName("net.irisshaders.iris.api.v0.IrisProgram");
            apiInstance = api.cast(api.getDeclaredMethod("getInstance").invoke(null));
            isShaderPackInUse = MethodHandles.lookup().findVirtual(api, "isShaderPackInUse", MethodType.methodType(boolean.class));
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

    public static boolean isShaderEnabled() {
        if (isShaderPackInUse == null)
            return false;
        try {
            return (Boolean)isShaderPackInUse.invoke(apiInstance);
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
