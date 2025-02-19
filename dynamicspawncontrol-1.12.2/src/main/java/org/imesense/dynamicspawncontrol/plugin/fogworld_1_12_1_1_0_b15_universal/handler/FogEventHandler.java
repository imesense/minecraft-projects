package org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.handler;

//import com.henrikstabell.fogworld.FogWorld;
//import com.henrikstabell.fogworld.api.interfaces.IBiomeFog;
//import com.henrikstabell.fogworld.api.interfaces.IDimensionFog;
//import com.henrikstabell.fogworld.config.FogWorldConfig;
//import com.henrikstabell.fogworld.util.BiomeUtil;
//import com.henrikstabell.fogworld.util.DimensionUtil;

import javax.annotation.Nullable;
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
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.api.interfaces.IBiomeFog;
import org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.api.interfaces.IDimensionFog;
import org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.config.DataFogWorld;
import org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.util.BiomeUtil;
import org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.util.DimensionUtil;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public class FogEventHandler
{
//todo, избавиться от тупых интерфейсов, переделать логику спавна тумана нахер
    // писали блять ногами код походу дела
    
    private static boolean instanceExists = false;

    public FogEventHandler()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }
        instanceExists = true;
    }

    private static double fogX;
    private static double fogZ;
    private static boolean fogInit;
    private static float fogFarPlaneDistance;

    @SubscribeEvent
    public static void onGetFogColor(EntityViewRenderEvent.FogColors event) {
        Log.writeDataToLogFile(0, "onGetFogColor");

        Vec3d mixedColor;
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            World world = player.world;
            int x = MathHelper.floor(player.posX);
            int y = MathHelper.floor(player.posY);
            int z = MathHelper.floor(player.posZ);
            IBlockState blockStateAtEyes = ActiveRenderInfo.getBlockStateAtEntityViewpoint(world, event.getEntity(), (float) event.getRenderPartialTicks());
            if (blockStateAtEyes.getMaterial() == Material.LAVA) {
                return;
            }
            if (blockStateAtEyes.getMaterial() == Material.WATER) {
                mixedColor = getFogBlendColorWater(world, player, x, y, z, event.getRenderPartialTicks());
            } else {
                mixedColor = getFogBlendColor(world, player, x, y, z, event.getRed(), event.getGreen(), event.getBlue(), event.getRenderPartialTicks());
            }
            event.setRed((float) mixedColor.x);
            event.setGreen((float) mixedColor.y);
            event.setBlue((float) mixedColor.z);
        }
    }

    @SubscribeEvent
    public static void onRenderFog(EntityViewRenderEvent.RenderFogEvent event) {
        Log.writeDataToLogFile(0, "onRenderFog");
        float farPlaneDistance;
        Entity entity = event.getEntity();
        World world = entity.world;
        int playerX = MathHelper.floor(entity.posX);
        int playerY = MathHelper.floor(entity.posY);
        int playerZ = MathHelper.floor(entity.posZ);

        if (playerX == fogX && playerZ == fogZ && fogInit) {
            renderFog(event.getFogMode(), fogFarPlaneDistance, 0.75f);
            return;
        }

        fogInit = true;
        float fpDistanceBiomeFog = 0.0f;
        float weightBiomeFog = 0.0f;

        for (int weightMixed = -20; weightMixed <= 20; weightMixed++) {
            for (int weightDefault = -20; weightDefault <= 20; weightDefault++) {
                Biome biomeForCoordsBody = world.getBiomeForCoordsBody(new BlockPos(playerX + weightMixed, playerY, playerZ + weightDefault));
                DimensionType dimensionType = world.provider.getDimensionType();

                if (!DimensionUtil.isDimensionBlacklisted(dimensionType) && !BiomeUtil.isBiomeBlacklisted(biomeForCoordsBody)) {
                    farPlaneDistance = 0.1f;
                    float farPlaneDistanceScaleBiome = 1.0f;

                    if (weightMixed != (-20)) {
                        if (weightMixed == 20) {
                            double farPlaneDistanceScale = entity.posX - playerX;
                            farPlaneDistance = (float) (farPlaneDistance * farPlaneDistanceScale);
                            farPlaneDistanceScaleBiome = (float) (1.0f * farPlaneDistanceScale);
                        }
                    } else {
                        double farPlaneDistanceScale2 = 1.0d - (entity.posX - playerX);
                        farPlaneDistance = (float) (farPlaneDistance * farPlaneDistanceScale2);
                        farPlaneDistanceScaleBiome = (float) (1.0f * farPlaneDistanceScale2);
                    }

                    if (weightDefault != (-20)) {
                        if (weightDefault == 20) {
                            double farPlaneDistanceScale3 = entity.posZ - playerZ;
                            farPlaneDistance = (float) (farPlaneDistance * farPlaneDistanceScale3);
                            farPlaneDistanceScaleBiome = (float) (farPlaneDistanceScaleBiome * farPlaneDistanceScale3);
                        }
                    } else {
                        double farPlaneDistanceScale4 = 1.0d - (entity.posZ - playerZ);
                        farPlaneDistance = (float) (farPlaneDistance * farPlaneDistanceScale4);
                        farPlaneDistanceScaleBiome = (float) (farPlaneDistanceScaleBiome * farPlaneDistanceScale4);
                    }

                    fpDistanceBiomeFog += farPlaneDistance;
                    weightBiomeFog += farPlaneDistanceScaleBiome;
                }
            }
        }

        float var17 = 20 * 2 * 20 * 2;
        float var18 = var17 - weightBiomeFog;
        float var19 = weightBiomeFog == 0.0f ? 0.0f : fpDistanceBiomeFog / weightBiomeFog;
        float farPlaneDistance2 = ((fpDistanceBiomeFog * 240.0f) + (event.getFarPlaneDistance() * var18)) / var17;
        float farPlaneDistanceScaleBiome2 = (0.1f * (1.0f - var19)) + (0.75f * var19);
        float var20 = ((farPlaneDistanceScaleBiome2 * weightBiomeFog) + (0.75f * var18)) / var17;

        fogX = entity.posX;
        fogZ = entity.posZ;
        fogFarPlaneDistance = Math.min(farPlaneDistance2, event.getFarPlaneDistance());
        renderFog(event.getFogMode(), fogFarPlaneDistance, var20);
    }

    private static void renderFog(int fogMode, float farPlaneDistance, float farPlaneDistanceScale) {
        Log.writeDataToLogFile(0, "render_fog " + fogMode);
        if (fogMode < 0) {
            GL11.glFogf(2915, 0.0f);
            GL11.glFogf(2916, farPlaneDistance);
            return;
        }
        GL11.glFogf(2915, farPlaneDistance * farPlaneDistanceScale);
        GL11.glFogf(2916, farPlaneDistance);
    }

    @Nullable
    private static Vec3d postProcessColor(World world, EntityLivingBase player, double r, double g, double b, double renderPartialTicks) {
        double darkScale = (player.lastTickPosY + ((player.posY - player.lastTickPosY) * renderPartialTicks)) * world.provider.getVoidFogYFactor();
        if (player.isPotionActive(MobEffects.BLINDNESS)) {
            int duration = player.getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
            darkScale *= duration < 20 ? 1.0f - (duration / 20.0f) : 0.0d;
        }
        if (darkScale < 1.0d) {
            double darkScale2 = darkScale < 0.0d ? 0.0d : darkScale * darkScale;
            r *= darkScale2;
            g *= darkScale2;
            b *= darkScale2;
        }
        if (player.isPotionActive(MobEffects.NIGHT_VISION)) {
            int duration2 = player.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
            float brightness = duration2 > 200 ? 1.0f : 0.7f + (MathHelper.sin((float) ((duration2 - renderPartialTicks) * 3.141592653589793d * 0.20000000298023224d)) * 0.3f);
            double scale = Math.min(Math.min(1.0d / r, 1.0d / g), 1.0d / b);
            r = (r * (1.0f - brightness)) + (r * scale * brightness);
            g = (g * (1.0f - brightness)) + (g * scale * brightness);
            b = (b * (1.0f - brightness)) + (b * scale * brightness);
        }
        if (Minecraft.getMinecraft().gameSettings.anaglyph) {
            double aR = (((r * 30.0d) + (g * 59.0d)) + (b * 11.0d)) / 100.0d;
            double aG = ((r * 30.0d) + (g * 70.0d)) / 100.0d;
            double aB = ((r * 30.0d) + (b * 70.0d)) / 100.0d;
            r = aR;
            g = aG;
            b = aB;
        }
        return new Vec3d(r, g, b);
    }

    private static Vec3d getFogBlendColorWater(World world, EntityLivingBase playerEntity, int playerX, int playerY, int playerZ, double renderPartialTicks) {
        float rBiomeFog = 0.0f;
        float gBiomeFog = 0.0f;
        float bBiomeFog = 0.0f;
        for (int weight = -2; weight <= 2; weight++) {
            for (int respirationLevel = -2; respirationLevel <= 2; respirationLevel++) {
                Biome rMixed = world.getBiomeForCoordsBody(new BlockPos(playerX + weight, playerY + weight, playerZ + respirationLevel));
                int gMixed = rMixed.getWaterColorMultiplier();
                float bMixed = (gMixed & 16711680) >> 16;
                float gPart = (gMixed & 65280) >> 8;
                float bPart = gMixed & 255;
                if (weight != (-2)) {
                    if (weight == 2) {
                        double zDiff = playerEntity.posX - playerX;
                        bMixed = (float) (bMixed * zDiff);
                        gPart = (float) (gPart * zDiff);
                        bPart = (float) (bPart * zDiff);
                    }
                } else {
                    double zDiff2 = 1.0d - (playerEntity.posX - playerX);
                    bMixed = (float) (bMixed * zDiff2);
                    gPart = (float) (gPart * zDiff2);
                    bPart = (float) (bPart * zDiff2);
                }
                if (respirationLevel != (-2)) {
                    if (respirationLevel == 2) {
                        double zDiff3 = playerEntity.posZ - playerZ;
                        bMixed = (float) (bMixed * zDiff3);
                        gPart = (float) (gPart * zDiff3);
                        bPart = (float) (bPart * zDiff3);
                    }
                } else {
                    double zDiff4 = 1.0d - (playerEntity.posZ - playerZ);
                    bMixed = (float) (bMixed * zDiff4);
                    gPart = (float) (gPart * zDiff4);
                    bPart = (float) (bPart * zDiff4);
                }
                rBiomeFog += bMixed;
                gBiomeFog += gPart;
                bBiomeFog += bPart;
            }
        }
        float bBiomeFog2 = bBiomeFog / 255.0f;
        float var20 = 2 * 2 * 2 * 2;
        float var21 = EnchantmentHelper.getRespirationModifier(playerEntity) * 0.2f;
        float var22 = (((rBiomeFog / 255.0f) * 0.02f) + var21) / var20;
        float var23 = (((gBiomeFog / 255.0f) * 0.02f) + var21) / var20;
        return postProcessColor(world, playerEntity, var22, var23, ((bBiomeFog2 * 0.2f) + var21) / var20, renderPartialTicks);
    }

    private static Vec3d getFogBlendColor(World world, EntityLivingBase playerEntity, int playerX, int playerY, int playerZ, float defR, float defG, float defB, double renderPartialTicks) {
        int bScale;
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        int[] ranges = ForgeModContainer.blendRanges;
        int distance = 0;
        if (settings.fancyGraphics && settings.renderDistanceChunks >= 0 && settings.renderDistanceChunks < ranges.length) {
            distance = ranges[settings.renderDistanceChunks];
        }
        float rBiomeFog = 0.0f;
        float gBiomeFog = 0.0f;
        float bBiomeFog = 0.0f;
        float weightBiomeFog = 0.0f;
        for (int celestialAngle = -distance; celestialAngle <= distance; celestialAngle++) {
            for (int baseScale = -distance; baseScale <= distance; baseScale++) {
                Biome biomeForCoordsBody = world.getBiomeForCoordsBody(new BlockPos(playerX + celestialAngle, playerY, playerZ + baseScale));
                DimensionType dimensionType = world.provider.getDimensionType();

                if (!DimensionUtil.isDimensionBlacklisted(dimensionType) && !BiomeUtil.isBiomeBlacklisted(biomeForCoordsBody)) {
                    bScale = 0xFFFFFF;
                    float rainStrength = (bScale & 16711680) >> 16;
                    float thunderStrength = (bScale & 65280) >> 8;
                    float processedColor = bScale & 255;
                    float weightMixed = 1.0f;

                    if (celestialAngle == (-distance)) {
                        double weightDefault = 1.0d - (playerEntity.posX - playerX);
                        rainStrength = (float) (rainStrength * weightDefault);
                        thunderStrength = (float) (thunderStrength * weightDefault);
                        processedColor = (float) (processedColor * weightDefault);
                        weightMixed = (float) (1.0f * weightDefault);
                    } else if (celestialAngle == distance) {
                        double weightDefault2 = playerEntity.posX - playerX;
                        rainStrength = (float) (rainStrength * weightDefault2);
                        thunderStrength = (float) (thunderStrength * weightDefault2);
                        processedColor = (float) (processedColor * weightDefault2);
                        weightMixed = (float) (1.0f * weightDefault2);
                    }
                    if (baseScale == (-distance)) {
                        double weightDefault3 = 1.0d - (playerEntity.posZ - playerZ);
                        rainStrength = (float) (rainStrength * weightDefault3);
                        thunderStrength = (float) (thunderStrength * weightDefault3);
                        processedColor = (float) (processedColor * weightDefault3);
                        weightMixed = (float) (weightMixed * weightDefault3);
                    } else if (baseScale == distance) {
                        double weightDefault4 = playerEntity.posZ - playerZ;
                        rainStrength = (float) (rainStrength * weightDefault4);
                        thunderStrength = (float) (thunderStrength * weightDefault4);
                        processedColor = (float) (processedColor * weightDefault4);
                        weightMixed = (float) (weightMixed * weightDefault4);
                    }
                    rBiomeFog += rainStrength;
                    gBiomeFog += thunderStrength;
                    bBiomeFog += processedColor;
                    weightBiomeFog += weightMixed;
                }
            }
        }
        if (weightBiomeFog == 0.0f) {
            return new Vec3d(defR, defG, defB);
        }
        float rBiomeFog2 = rBiomeFog / 255.0f;
        float gBiomeFog2 = gBiomeFog / 255.0f;
        float bBiomeFog2 = bBiomeFog / 255.0f;
        float var28 = world.getCelestialAngle((float) renderPartialTicks);
        float var29 = MathHelper.clamp((MathHelper.cos(var28 * 3.1415927f * 2.0f) * 2.0f) + 0.5f, 0.0f, 1.0f);
        float var30 = (var29 * 0.94f) + 0.06f;
        float var31 = (var29 * 0.94f) + 0.06f;
        float var32 = (var29 * 0.91f) + 0.09f;
        float rainStrength2 = world.getRainStrength((float) renderPartialTicks);
        if (rainStrength2 > 0.0f) {
            var30 *= 1.0f - (rainStrength2 * 0.5f);
            var31 *= 1.0f - (rainStrength2 * 0.5f);
            var32 *= 1.0f - (rainStrength2 * 0.4f);
        }
        float thunderStrength2 = world.getThunderStrength((float) renderPartialTicks);
        if (thunderStrength2 > 0.0f) {
            var30 *= 1.0f - (thunderStrength2 * 0.5f);
            var31 *= 1.0f - (thunderStrength2 * 0.5f);
            var32 *= 1.0f - (thunderStrength2 * 0.5f);
        }
        Vec3d var33 = postProcessColor(world, playerEntity, rBiomeFog2 * (var30 / weightBiomeFog), gBiomeFog2 * (var31 / weightBiomeFog), bBiomeFog2 * (var32 / weightBiomeFog), renderPartialTicks);
        float rBiomeFog3 = (float) var33.x;
        float gBiomeFog3 = (float) var33.y;
        float bBiomeFog3 = (float) var33.z;
        float weightMixed2 = distance * 2 * distance * 2;
        float var34 = weightMixed2 - weightBiomeFog;
        double rFinal = ((rBiomeFog3 * weightBiomeFog) + (defR * var34)) / weightMixed2;
        double gFinal = ((gBiomeFog3 * weightBiomeFog) + (defG * var34)) / weightMixed2;
        double bFinal = ((bBiomeFog3 * weightBiomeFog) + (defB * var34)) / weightMixed2;
        return new Vec3d(rFinal, gFinal, bFinal);
    }
}
