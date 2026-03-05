package org.imesense.dynamicspawncontrol.core.renderer.fog;

import java.util.Random;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CompletableFuture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldType;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import org.imesense.dynamicspawncontrol.bloodmoonmanager.ClientBloodmoonHandler;
import org.imesense.dynamicspawncontrol.core.taskmanager.TaskManager;
import org.imesense.dynamicspawncontrol.core.taskmanager.Task;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.lwjgl.opengl.GLContext;

public final class BedrockVoidFog
{
    int dimensionIdVoid = 255;

    private static volatile BedrockVoidFog _INSTANCE;
    private static TaskManager taskManager;

    private final Queue<ParticleTask> particleQueue = new ConcurrentLinkedQueue<>();
    private final Random random = new Random();

    private static final int MAX_PARTICLES_PER_TICK = 120;
    private static final int MAX_PARTICLES_PER_BATCH = 300;
    private static final int MAX_QUEUE_SIZE = 2000;

    private static final int PARTICLE_SPAWN_HEIGHT = 30;

    private static final int FOG_START_HEIGHT = 16;
    private static final int FOG_FULL_HEIGHT = 12;
    private static final float MIN_FOG_DISTANCE = 5.0f;
    private static final float MAX_FOG_DISTANCE = 100.0f;

    static
    {
        taskManager = TaskManager.getInstance();
    }

    public static BedrockVoidFog getInstance()
    {
        return CodeGeneric.getInstance(BedrockVoidFog.class);
    }

    public BedrockVoidFog()
    {
    }

    private static class ParticleTask
    {
        final EnumParticleTypes type;
        final double x, y, z;
        final double vx, vy, vz;

        ParticleTask(EnumParticleTypes type, double x, double y, double z, double vx, double vy, double vz)
        {
            this.type = type;
            this.x = x; this.y = y; this.z = z;
            this.vx = vx; this.vy = vy; this.vz = vz;
        }
    }

    private class ParticleCalculationTask extends Task<Void>
    {
        private final double centerX, centerY, centerZ;
        private final int dimension;
        private final WorldType worldType;
        private final boolean isNether;
        private final boolean isVoidDim;

        public ParticleCalculationTask(double centerX, double centerY, double centerZ,
                                       int dimension, WorldType worldType, boolean isNether, boolean isVoidDim)
        {
            super("ParticleCalcTask", TaskManager.TaskPriority.LOW);

            this.centerX = centerX;
            this.centerY = centerY;
            this.centerZ = centerZ;
            this.dimension = dimension;
            this.worldType = worldType;
            this.isNether = isNether;
            this.isVoidDim = isVoidDim;
        }

        @Override
        public Void execute() throws Exception
        {
            try
            {
                calculateParticlePositions();
            }
            catch (Exception exception)
            {
                LogManager.error("Error calculating particle positions: " + exception.getMessage());
                throw exception;
            }

            return null;
        }

        private void calculateParticlePositions()
        {
            int particlesToCalculate = isVoidDim ? MAX_PARTICLES_PER_BATCH : MAX_PARTICLES_PER_BATCH / 2;

            for (int i = 0; i < particlesToCalculate; i++)
            {
                if (particleQueue.size() >= MAX_QUEUE_SIZE)
                {
                    break;
                }

                int x = (int)centerX + random.nextInt(24) - random.nextInt(24);
                int y = (int)centerY + random.nextInt(24) - random.nextInt(24);
                int z = (int)centerZ + random.nextInt(24) - random.nextInt(24);

                if (shouldSpawnParticle(y, dimension, worldType, isNether, isVoidDim)) {
                    particleQueue.add(new ParticleTask(
                            EnumParticleTypes.SUSPENDED_DEPTH,
                            x + random.nextFloat(),
                            y + random.nextFloat(),
                            z + random.nextFloat(),
                            0.0d, 0.0d, 0.0d
                    ));
                }
            }
        }

        private boolean shouldSpawnParticle(int y, int dimension,
                                            WorldType worldType, boolean isNether, boolean isVoidDim)
        {
            if (isVoidDim) return true;
            if (worldType == WorldType.FLAT || isNether) return false;

            return y < PARTICLE_SPAWN_HEIGHT;
        }
    }

    public void handleFogVoidParticles(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.ClientTickEvent.Phase.END)
        {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.isGamePaused() || mc.player == null || mc.world == null)
        {
            return;
        }

        EntityPlayerSP player = mc.player;
        WorldClient world = mc.world;

        if (!taskManager.isShuttingDown())
        {
            submitParticleCalculationTask(player, world);
        }

        spawnParticlesFromQueue(world);
    }

    private void submitParticleCalculationTask(EntityPlayerSP player, WorldClient world)
    {
        if (particleQueue.size() > MAX_QUEUE_SIZE * 0.9)
        {
            return;
        }

        ParticleCalculationTask task = new ParticleCalculationTask(
                player.posX, player.posY, player.posZ,
                world.provider.getDimension(),
                world.getWorldInfo().getTerrainType(),
                world.provider.isNether(),
                world.provider.getDimension() == dimensionIdVoid
        );

        CompletableFuture<Void> future = taskManager.submitTask(task);

        future.exceptionally(throwable ->
        {
            LogManager.error("Particle calculation task failed: " + throwable.getMessage());
            return null;
        });
    }

    private void spawnParticlesFromQueue(WorldClient world)
    {
        if (world == null) return;

        int spawnedThisTick = 0;
        ParticleTask task;

        int targetCount = MAX_PARTICLES_PER_TICK / 2 + random.nextInt(MAX_PARTICLES_PER_TICK / 2);

        while (spawnedThisTick < targetCount && (task = particleQueue.poll()) != null)
        {
            world.spawnParticle(
                    task.type,
                    task.x, task.y, task.z,
                    task.vx, task.vy, task.vz
            );

            spawnedThisTick++;
        }

        if (particleQueue.size() > MAX_QUEUE_SIZE)
        {
            int toRemove = (particleQueue.size() - MAX_QUEUE_SIZE) / 2;

            for (int i = 0; i < toRemove; i++)
            {
                particleQueue.poll();
            }
        }
    }

    public void handleFogVoidRender(EntityViewRenderEvent.RenderFogEvent event)
    {
        if (ClientBloodmoonHandler.INSTANCE.isBloodmoonActive())
        {
            return;
        }

        EntityPlayer entity = (EntityPlayer) event.getEntity();
        WorldClient world = Minecraft.getMinecraft().world;

        if (world == null) return;

        if (entity.capabilities.isCreativeMode) return;

        boolean isVoidDim = world.provider.getDimension() == dimensionIdVoid;

        if (!isVoidDim)
        {
            if (world.getWorldInfo().getTerrainType() == WorldType.FLAT ||
                    world.provider.isNether()) return;

            // Если выше 16 - вообще никакого тумана
            if (entity.posY > FOG_START_HEIGHT) return;
        }

        float farPlane = calculateFogDistance(entity, isVoidDim);

        GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
        GlStateManager.setFogStart(0.0f);
        GlStateManager.setFogEnd(farPlane);

        if (GLContext.getCapabilities().GL_NV_fog_distance)
        {
            GlStateManager.glFogi(34138, 34139);
        }

        if (world.provider.doesXZShowFog((int)entity.posX, (int)entity.posZ))
        {
            GlStateManager.setFogStart(farPlane * 0.05f);
            GlStateManager.setFogEnd(Math.min(farPlane, 192.0f) * 0.5f);
        }
    }

    private float calculateFogDistance(EntityPlayer entity, boolean isVoidDim)
    {
        if (isVoidDim)
        {
            return MIN_FOG_DISTANCE;
        }

        double playerY = entity.posY;

        if (playerY >= FOG_START_HEIGHT)
        {
            return MAX_FOG_DISTANCE;
        }

        if (playerY <= FOG_FULL_HEIGHT)
        {
            return MIN_FOG_DISTANCE;
        }

        double range = FOG_START_HEIGHT - FOG_FULL_HEIGHT;
        double t = (FOG_START_HEIGHT - playerY) / range;
        t = MathHelper.clamp(t, 0.0, 1.0);

        t = t * t * (3.0 - 2.0 * t);

        float fogDistance = (float)(MAX_FOG_DISTANCE - (MAX_FOG_DISTANCE - MIN_FOG_DISTANCE) * t);

        return fogDistance;
    }

    public void handleFogVoidColor(EntityViewRenderEvent.FogColors e)
    {
        if (ClientBloodmoonHandler.INSTANCE.isBloodmoonActive())
        {
            return;
        }

        Entity entity = e.getEntity();
        WorldClient world = Minecraft.getMinecraft().world;

        if (world == null) return;

        boolean isVoidDim = world.provider.getDimension() == dimensionIdVoid;

        if (!isVoidDim)
        {
            if (world.getWorldInfo().getTerrainType() == WorldType.FLAT ||
                    world.provider.isNether()) return;

            if (entity.posY > FOG_START_HEIGHT) return;
        }

        double d0 = entity.lastTickPosY +
                ((entity.posY - entity.lastTickPosY) * e.getRenderPartialTicks() *
                        world.provider.getVoidFogYFactor());

        if (d0 < 1.0d)
        {
            d0 = Math.max(d0, 0.0d);
            double d02 = d0 * d0;

            if (isVoidDim)
            {
                d02 = 0.0d;
            }
            else
            {
                double playerY = entity.posY;

                if (playerY <= FOG_FULL_HEIGHT)
                {
                    d02 = d02 * 0.1;
                }
                else if (playerY < FOG_START_HEIGHT)
                {
                    double range = FOG_START_HEIGHT - FOG_FULL_HEIGHT;
                    double progress = (FOG_START_HEIGHT - playerY) / range;
                    double darkFactor = 0.1 + (1.0 - 0.1) * (1.0 - progress);
                    d02 = d02 * darkFactor;
                }
            }

            e.setRed((float) (e.getRed() * d02));
            e.setGreen((float) (e.getGreen() * d02));
            e.setBlue((float) (e.getBlue() * d02));
        }
    }

    public void shutdown()
    {
        particleQueue.clear();
    }
}