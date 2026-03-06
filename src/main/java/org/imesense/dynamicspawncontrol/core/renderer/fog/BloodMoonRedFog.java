package org.imesense.dynamicspawncontrol.core.renderer.fog;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.Mod;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.lwjgl.opengl.GL11;

public class BloodMoonRedFog
{
    private static volatile BloodMoonRedFog _INSTANCE;

    public static BloodMoonRedFog getInstance()
    {
        return CodeGeneric.getInstance(BloodMoonRedFog.class);
    }

    public BloodMoonRedFog()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    private static double lastPlayerX;
    private static double lastPlayerZ;
    private static boolean fogInitialized;
    private static float lastCalculatedFarPlaneDistance;

    public void handleGetFogColor(EntityViewRenderEvent.FogColors event)
    {
        Vec3d mixedColor;

        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            World world = player.world;
            int playerBlockX = MathHelper.floor(player.posX);
            int playerBlockY = MathHelper.floor(player.posY);
            int playerBlockZ = MathHelper.floor(player.posZ);
            IBlockState blockStateAtEyes = ActiveRenderInfo.getBlockStateAtEntityViewpoint(world, event.getEntity(), (float) event.getRenderPartialTicks());

            if (blockStateAtEyes.getMaterial() == Material.LAVA)
            {
                return;
            }

            if (blockStateAtEyes.getMaterial() == Material.WATER)
            {
                mixedColor = getWaterFogColor(world, player, playerBlockX, playerBlockY, playerBlockZ, event.getRenderPartialTicks());
            }
            else
            {
                mixedColor = getBlendedFogColor(world, player, playerBlockX, playerBlockY, playerBlockZ, event.getRed(), event.getGreen(), event.getBlue(), event.getRenderPartialTicks());
            }

            event.setRed((float) mixedColor.x);
            event.setGreen((float) mixedColor.y);
            event.setBlue((float) mixedColor.z);
        }
    }

    public void handleRenderFog(EntityViewRenderEvent.RenderFogEvent event)
    {
        Entity entity = event.getEntity();
        World world = entity.world;

        int playerBlockX = MathHelper.floor(entity.posX);
        int playerBlockY = MathHelper.floor(entity.posY);
        int playerBlockZ = MathHelper.floor(entity.posZ);

        if (playerBlockX == lastPlayerX && playerBlockZ == lastPlayerZ && fogInitialized)
        {
            applyFog(event.getFogMode(), lastCalculatedFarPlaneDistance, 0.75f);
            return;
        }

        fogInitialized = true;

        float totalBiomeFogDistance = 0.0f;
        float totalBiomeWeight = 0.0f;

        int samplingRadius = 20;

        for (int offsetX = -samplingRadius; offsetX <= samplingRadius; offsetX++)
        {
            for (int offsetZ = -samplingRadius; offsetZ <= samplingRadius; offsetZ++)
            {
                Biome biome = world.getBiomeForCoordsBody(new BlockPos(playerBlockX + offsetX, playerBlockY, playerBlockZ + offsetZ));
                DimensionType dimensionType = world.provider.getDimensionType();

                float biomeFogDistance = 1.0f; // БАЗОВОЕ ЗНАЧЕНИЕ - делает туман очень близким
                float biomeWeight = 1.0f;

                // Расчет весов на основе позиции игрока внутри блока (X координата)
                if (offsetX == -samplingRadius)
                {
                    double weightX = 1.0d - (entity.posX - playerBlockX);
                    biomeFogDistance = (float) (biomeFogDistance * weightX);
                    biomeWeight = (float) (1.0f * weightX);
                }
                else if (offsetX == samplingRadius)
                {
                    double weightX = entity.posX - playerBlockX;
                    biomeFogDistance = (float) (biomeFogDistance * weightX);
                    biomeWeight = (float) (1.0f * weightX);
                }

                if (offsetZ == -samplingRadius)
                {
                    double weightZ = 1.0d - (entity.posZ - playerBlockZ);
                    biomeFogDistance = (float) (biomeFogDistance * weightZ);
                    biomeWeight = (float) (biomeWeight * weightZ);
                }
                else if (offsetZ == samplingRadius)
                {
                    double weightZ = entity.posZ - playerBlockZ;
                    biomeFogDistance = (float) (biomeFogDistance * weightZ);
                    biomeWeight = (float) (biomeWeight * weightZ);
                }

                totalBiomeFogDistance += biomeFogDistance;
                totalBiomeWeight += biomeWeight;
            }
        }

        float totalSamples = (samplingRadius * 2) * (samplingRadius * 2);
        float weightDifference = totalSamples - totalBiomeWeight;

        float averageBiomeFogDistance = totalBiomeWeight == 0.0f ? 0.0f : totalBiomeFogDistance / totalBiomeWeight;

        // Смешиваем с дефолтным значением дальности тумана (240.0f - видимо хардкод из ваниллы)
        float blendedFarPlaneDistance = ((totalBiomeFogDistance * 240.0f) + (event.getFarPlaneDistance() * weightDifference)) / totalSamples;

        float fogStartScale = (0.1f * (1.0f - averageBiomeFogDistance)) + (0.75f * averageBiomeFogDistance);
        float blendedFogStartScale = ((fogStartScale * totalBiomeWeight) + (0.75f * weightDifference)) / totalSamples;

        lastPlayerX = entity.posX;
        lastPlayerZ = entity.posZ;
        lastCalculatedFarPlaneDistance = Math.min(blendedFarPlaneDistance, event.getFarPlaneDistance());

        applyFog(event.getFogMode(), lastCalculatedFarPlaneDistance, blendedFogStartScale);
    }

    private static void applyFog(int fogMode, float farPlaneDistance, float fogStartScale)
    {
        if (fogMode < 0)
        { // Экспоненциальный туман
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.0f);
            GL11.glFogf(GL11.GL_FOG_END, farPlaneDistance);
            return;
        }

        // Линейный туман
        GL11.glFogf(GL11.GL_FOG_START, farPlaneDistance * fogStartScale); // Начало тумана (ближняя граница)
        GL11.glFogf(GL11.GL_FOG_END, farPlaneDistance);                    // Конец тумана (дальняя граница)
    }

    private static Vec3d applyPostProcessing(World world, EntityLivingBase player, double red, double green, double blue, double renderPartialTicks)
    {
        double darkScale = (player.lastTickPosY + ((player.posY - player.lastTickPosY) * renderPartialTicks)) * world.provider.getVoidFogYFactor();

        if (player.isPotionActive(MobEffects.BLINDNESS))
        {
            int duration = player.getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
            darkScale *= duration < 20 ? 1.0f - (duration / 20.0f) : 0.0d;
        }

        if (darkScale < 1.0d)
        {
            double darkScale2 = darkScale < 0.0d ? 0.0d : darkScale * darkScale;
            red *= darkScale2;
            green *= darkScale2;
            blue *= darkScale2;
        }

        if (player.isPotionActive(MobEffects.NIGHT_VISION))
        {
            int duration = player.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
            float brightness = duration > 200 ? 1.0f : 0.7f + (MathHelper.sin((float) ((duration - renderPartialTicks) * Math.PI * 0.20000000298023224d)) * 0.3f);
            double scale = Math.min(Math.min(1.0d / red, 1.0d / green), 1.0d / blue);
            red = (red * (1.0f - brightness)) + (red * scale * brightness);
            green = (green * (1.0f - brightness)) + (green * scale * brightness);
            blue = (blue * (1.0f - brightness)) + (blue * scale * brightness);
        }

        if (Minecraft.getMinecraft().gameSettings.anaglyph)
        {
            double anaglyphRed = (((red * 30.0d) + (green * 59.0d)) + (blue * 11.0d)) / 100.0d;
            double anaglyphGreen = ((red * 30.0d) + (green * 70.0d)) / 100.0d;
            double anaglyphBlue = ((red * 30.0d) + (blue * 70.0d)) / 100.0d;
            red = anaglyphRed;
            green = anaglyphGreen;
            blue = anaglyphBlue;
        }

        return new Vec3d(red, green, blue);
    }

    private static Vec3d getWaterFogColor(World world, EntityLivingBase player, int playerBlockX, int playerBlockY, int playerBlockZ, double renderPartialTicks)
    {
        float totalRed = 0.0f;
        float totalGreen = 0.0f;
        float totalBlue = 0.0f;

        int waterSamplingRadius = 2;

        for (int offsetX = -waterSamplingRadius; offsetX <= waterSamplingRadius; offsetX++)
        {
            for (int offsetZ = -waterSamplingRadius; offsetZ <= waterSamplingRadius; offsetZ++)
            {
                Biome biome = world.getBiomeForCoordsBody(new BlockPos(playerBlockX + offsetX, playerBlockY + offsetX, playerBlockZ + offsetZ));
                int waterColor = biome.getWaterColorMultiplier();

                float biomeRed = (waterColor & 0xFF0000) >> 16;
                float biomeGreen = (waterColor & 0xFF00) >> 8;
                float biomeBlue = waterColor & 0xFF;

                // Интерполяция по X
                if (offsetX == -waterSamplingRadius)
                {
                    double weightX = 1.0d - (player.posX - playerBlockX);
                    biomeRed = (float) (biomeRed * weightX);
                    biomeGreen = (float) (biomeGreen * weightX);
                    biomeBlue = (float) (biomeBlue * weightX);
                }
                else if (offsetX == waterSamplingRadius)
                {
                    double weightX = player.posX - playerBlockX;
                    biomeRed = (float) (biomeRed * weightX);
                    biomeGreen = (float) (biomeGreen * weightX);
                    biomeBlue = (float) (biomeBlue * weightX);
                }

                // Интерполяция по Z
                if (offsetZ == -waterSamplingRadius)
                {
                    double weightZ = 1.0d - (player.posZ - playerBlockZ);
                    biomeRed = (float) (biomeRed * weightZ);
                    biomeGreen = (float) (biomeGreen * weightZ);
                    biomeBlue = (float) (biomeBlue * weightZ);
                }
                else if (offsetZ == waterSamplingRadius)
                {
                    double weightZ = player.posZ - playerBlockZ;
                    biomeRed = (float) (biomeRed * weightZ);
                    biomeGreen = (float) (biomeGreen * weightZ);
                    biomeBlue = (float) (biomeBlue * weightZ);
                }

                totalRed += biomeRed;
                totalGreen += biomeGreen;
                totalBlue += biomeBlue;
            }
        }

        float normalizedBlue = totalBlue / 255.0f;
        float totalSamples = (waterSamplingRadius * 2) * (waterSamplingRadius * 2);
        float respirationModifier = EnchantmentHelper.getRespirationModifier(player) * 0.2f;

        float finalRed = (((totalRed / 255.0f) * 0.02f) + respirationModifier) / totalSamples;
        float finalGreen = (((totalGreen / 255.0f) * 0.02f) + respirationModifier) / totalSamples;
        float finalBlue = (((normalizedBlue * 0.2f) + respirationModifier) / totalSamples);

        return applyPostProcessing(world, player, finalRed, finalGreen, finalBlue, renderPartialTicks);
    }

    private static Vec3d getBlendedFogColor(World world, EntityLivingBase player, int playerBlockX, int playerBlockY, int playerBlockZ, float defaultRed, float defaultGreen, float defaultBlue, double renderPartialTicks)
    {
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        int[] blendRanges = ForgeModContainer.blendRanges;
        int samplingDistance = 0;

        if (settings.fancyGraphics && settings.renderDistanceChunks >= 0 && settings.renderDistanceChunks < blendRanges.length)
        {
            samplingDistance = blendRanges[settings.renderDistanceChunks];
        }

        float totalBiomeRed = 0.0f;
        float totalBiomeGreen = 0.0f;
        float totalBiomeBlue = 0.0f;
        float totalBiomeWeight = 0.0f;

        for (int offsetX = -samplingDistance; offsetX <= samplingDistance; offsetX++)
        {
            for (int offsetZ = -samplingDistance; offsetZ <= samplingDistance; offsetZ++)
            {
                Biome biome = world.getBiomeForCoordsBody(new BlockPos(playerBlockX + offsetX, playerBlockY, playerBlockZ + offsetZ));
                DimensionType dimensionType = world.provider.getDimensionType();

                int biomeColor = 0xFFFFFF; // TODO: Получать реальный цвет из биома/измерения?
                float biomeRed = (biomeColor & 0xFF0000) >> 16;
                float biomeGreen = (biomeColor & 0xFF00) >> 8;
                float biomeBlue = biomeColor & 0xFF;
                float weight = 1.0f;

                // Интерполяция по X
                if (offsetX == -samplingDistance)
                {
                    double weightX = 1.0d - (player.posX - playerBlockX);
                    biomeRed = (float) (biomeRed * weightX);
                    biomeGreen = (float) (biomeGreen * weightX);
                    biomeBlue = (float) (biomeBlue * weightX);
                    weight = (float) (1.0f * weightX);
                }
                else if (offsetX == samplingDistance)
                {
                    double weightX = player.posX - playerBlockX;
                    biomeRed = (float) (biomeRed * weightX);
                    biomeGreen = (float) (biomeGreen * weightX);
                    biomeBlue = (float) (biomeBlue * weightX);
                    weight = (float) (1.0f * weightX);
                }

                // Интерполяция по Z
                if (offsetZ == -samplingDistance)
                {
                    double weightZ = 1.0d - (player.posZ - playerBlockZ);
                    biomeRed = (float) (biomeRed * weightZ);
                    biomeGreen = (float) (biomeGreen * weightZ);
                    biomeBlue = (float) (biomeBlue * weightZ);
                    weight = (float) (weight * weightZ);
                }
                else if (offsetZ == samplingDistance)
                {
                    double weightZ = player.posZ - playerBlockZ;
                    biomeRed = (float) (biomeRed * weightZ);
                    biomeGreen = (float) (biomeGreen * weightZ);
                    biomeBlue = (float) (biomeBlue * weightZ);
                    weight = (float) (weight * weightZ);
                }

                totalBiomeRed += biomeRed;
                totalBiomeGreen += biomeGreen;
                totalBiomeBlue += biomeBlue;
                totalBiomeWeight += weight;
            }
        }

        if (totalBiomeWeight == 0.0f)
        {
            return new Vec3d(defaultRed, defaultGreen, defaultBlue);
        }

        float normalizedBiomeRed = totalBiomeRed / 255.0f;
        float normalizedBiomeGreen = totalBiomeGreen / 255.0f;
        float normalizedBiomeBlue = totalBiomeBlue / 255.0f;

        // Коррекция по времени суток
        float celestialAngle = world.getCelestialAngle((float) renderPartialTicks);
        float dayNightFactor = MathHelper.clamp((MathHelper.cos(celestialAngle * (float)Math.PI * 2.0f) * 2.0f) + 0.5f, 0.0f, 1.0f);

        float dayRed = (dayNightFactor * 0.94f) + 0.06f;
        float dayGreen = (dayNightFactor * 0.94f) + 0.06f;
        float dayBlue = (dayNightFactor * 0.91f) + 0.09f;

        // Коррекция по дождю
        float rainStrength = world.getRainStrength((float) renderPartialTicks);
        if (rainStrength > 0.0f)
        {
            dayRed *= 1.0f - (rainStrength * 0.5f);
            dayGreen *= 1.0f - (rainStrength * 0.5f);
            dayBlue *= 1.0f - (rainStrength * 0.4f);
        }

        // Коррекция по грозе
        float thunderStrength = world.getThunderStrength((float) renderPartialTicks);
        if (thunderStrength > 0.0f)
        {
            dayRed *= 1.0f - (thunderStrength * 0.5f);
            dayGreen *= 1.0f - (thunderStrength * 0.5f);
            dayBlue *= 1.0f - (thunderStrength * 0.5f);
        }

        Vec3d postProcessedColor = applyPostProcessing(
                world,
                player,
                normalizedBiomeRed * (dayRed / totalBiomeWeight),
                normalizedBiomeGreen * (dayGreen / totalBiomeWeight),
                normalizedBiomeBlue * (dayBlue / totalBiomeWeight),
                renderPartialTicks
        );

        float processedRed = (float) postProcessedColor.x;
        float processedGreen = (float) postProcessedColor.y;
        float processedBlue = (float) postProcessedColor.z;

        float totalSamples = samplingDistance * 2 * samplingDistance * 2;
        float weightDifference = totalSamples - totalBiomeWeight;

        double finalRed = ((processedRed * totalBiomeWeight) + (defaultRed * weightDifference)) / totalSamples;
        double finalGreen = ((processedGreen * totalBiomeWeight) + (defaultGreen * weightDifference)) / totalSamples;
        double finalBlue = ((processedBlue * totalBiomeWeight) + (defaultBlue * weightDifference)) / totalSamples;

        return new Vec3d(finalRed, finalGreen, finalBlue);
    }
}
