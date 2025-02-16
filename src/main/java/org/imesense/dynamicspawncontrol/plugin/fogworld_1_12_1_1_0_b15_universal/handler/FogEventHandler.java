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
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

public class FogEventHandler
{
    private static double fogX;
    private static double fogZ;
    private static boolean fogInit;
    private static float fogFarPlaneDistance;

    @SubscribeEvent
    public static void onGetFogColor(EntityViewRenderEvent.FogColors event) {
        Vec3d mixedColor;
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = event.getEntity();
            World world = player.field_70170_p;
            int x = MathHelper.func_76128_c(player.field_70165_t);
            int y = MathHelper.func_76128_c(player.field_70163_u);
            int z = MathHelper.func_76128_c(player.field_70161_v);
            IBlockState blockStateAtEyes = ActiveRenderInfo.func_186703_a(world, event.getEntity(), (float) event.getRenderPartialTicks());
            if (blockStateAtEyes.func_185904_a() == Material.field_151587_i) {
                return;
            }
            if (blockStateAtEyes.func_185904_a() == Material.field_151586_h) {
                mixedColor = getFogBlendColorWater(world, player, x, y, z, event.getRenderPartialTicks());
            } else {
                mixedColor = getFogBlendColor(world, player, x, y, z, event.getRed(), event.getGreen(), event.getBlue(), event.getRenderPartialTicks());
            }
            event.setRed((float) mixedColor.field_72450_a);
            event.setGreen((float) mixedColor.field_72448_b);
            event.setBlue((float) mixedColor.field_72449_c);
        }
    }

    @SubscribeEvent
    public static void onRenderFog(EntityViewRenderEvent.RenderFogEvent event) {
        float farPlaneDistance;
        Entity entity = event.getEntity();
        World world = entity.field_70170_p;
        int playerX = MathHelper.func_76128_c(entity.field_70165_t);
        int playerY = MathHelper.func_76128_c(entity.field_70163_u);
        int playerZ = MathHelper.func_76128_c(entity.field_70161_v);
        if (playerX == fogX && playerZ == fogZ && fogInit) {
            renderFog(event.getFogMode(), fogFarPlaneDistance, 0.75f);
            return;
        }
        fogInit = true;
        float fpDistanceBiomeFog = 0.0f;
        float weightBiomeFog = 0.0f;
        for (int weightMixed = -20; weightMixed <= 20; weightMixed++) {
            for (int weightDefault = -20; weightDefault <= 20; weightDefault++) {
                IBiomeFog biomeForCoordsBody = world.getBiomeForCoordsBody(new BlockPos(playerX + weightMixed, playerZ + weightDefault, playerY + weightDefault));
                IDimensionFog iDimensionFog = world.field_73011_w;
                if ((!(iDimensionFog instanceof IDimensionFog) || iDimensionFog.getFogEnabled()) && ((!(biomeForCoordsBody instanceof IBiomeFog) || biomeForCoordsBody.getFogEnabled()) && !DimensionUtil.isDimensionBlacklisted(iDimensionFog.func_186058_p()) && !BiomeUtil.isBiomeBlacklisted(biomeForCoordsBody))) {
                    if (iDimensionFog instanceof IDimensionFog) {
                        farPlaneDistance = iDimensionFog.getFogDensity(playerX + weightMixed, playerY, playerZ + weightDefault);
                    } else if (biomeForCoordsBody instanceof IBiomeFog) {
                        farPlaneDistance = biomeForCoordsBody.getFogDensity(playerX + weightMixed, playerY, playerZ + weightDefault);
                    } else {
                        farPlaneDistance = FogWorldConfig.getFogDensity(playerX + weightMixed, playerY, playerZ + weightDefault);
                    }
                    float farPlaneDistanceScaleBiome = 1.0f;
                    if (weightMixed != (-20)) {
                        if (weightMixed == 20) {
                            double farPlaneDistanceScale = entity.field_70165_t - playerX;
                            farPlaneDistance = (float) (farPlaneDistance * farPlaneDistanceScale);
                            farPlaneDistanceScaleBiome = (float) (1.0f * farPlaneDistanceScale);
                        }
                    } else {
                        double farPlaneDistanceScale2 = 1.0d - (entity.field_70165_t - playerX);
                        farPlaneDistance = (float) (farPlaneDistance * farPlaneDistanceScale2);
                        farPlaneDistanceScaleBiome = (float) (1.0f * farPlaneDistanceScale2);
                    }
                    if (weightDefault != (-20)) {
                        if (weightDefault == 20) {
                            double farPlaneDistanceScale3 = entity.field_70161_v - playerZ;
                            farPlaneDistance = (float) (farPlaneDistance * farPlaneDistanceScale3);
                            farPlaneDistanceScaleBiome = (float) (farPlaneDistanceScaleBiome * farPlaneDistanceScale3);
                        }
                    } else {
                        double farPlaneDistanceScale4 = 1.0d - (entity.field_70161_v - playerZ);
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
        fogX = entity.field_70165_t;
        fogZ = entity.field_70161_v;
        fogFarPlaneDistance = Math.min(farPlaneDistance2, event.getFarPlaneDistance());
        renderFog(event.getFogMode(), fogFarPlaneDistance, var20);
    }

    private static void renderFog(int fogMode, float farPlaneDistance, float farPlaneDistanceScale) {
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
        double darkScale = (player.field_70137_T + ((player.field_70163_u - player.field_70137_T) * renderPartialTicks)) * world.field_73011_w.func_76565_k();
        if (player.func_70644_a(MobEffects.field_76440_q)) {
            int duration = player.func_70660_b(MobEffects.field_76440_q).func_76459_b();
            darkScale *= duration < 20 ? 1.0f - (duration / 20.0f) : 0.0d;
        }
        if (darkScale < 1.0d) {
            double darkScale2 = darkScale < 0.0d ? 0.0d : darkScale * darkScale;
            r *= darkScale2;
            g *= darkScale2;
            b *= darkScale2;
        }
        if (player.func_70644_a(MobEffects.field_76439_r)) {
            int duration2 = player.func_70660_b(MobEffects.field_76439_r).func_76459_b();
            float brightness = duration2 > 200 ? 1.0f : 0.7f + (MathHelper.func_76126_a((float) ((duration2 - renderPartialTicks) * 3.141592653589793d * 0.20000000298023224d)) * 0.3f);
            double scale = Math.min(Math.min(1.0d / r, 1.0d / g), 1.0d / b);
            r = (r * (1.0f - brightness)) + (r * scale * brightness);
            g = (g * (1.0f - brightness)) + (g * scale * brightness);
            b = (b * (1.0f - brightness)) + (b * scale * brightness);
        }
        if (Minecraft.func_71410_x().field_71474_y.field_74337_g) {
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
                        double zDiff = playerEntity.field_70165_t - playerX;
                        bMixed = (float) (bMixed * zDiff);
                        gPart = (float) (gPart * zDiff);
                        bPart = (float) (bPart * zDiff);
                    }
                } else {
                    double zDiff2 = 1.0d - (playerEntity.field_70165_t - playerX);
                    bMixed = (float) (bMixed * zDiff2);
                    gPart = (float) (gPart * zDiff2);
                    bPart = (float) (bPart * zDiff2);
                }
                if (respirationLevel != (-2)) {
                    if (respirationLevel == 2) {
                        double zDiff3 = playerEntity.field_70161_v - playerZ;
                        bMixed = (float) (bMixed * zDiff3);
                        gPart = (float) (gPart * zDiff3);
                        bPart = (float) (bPart * zDiff3);
                    }
                } else {
                    double zDiff4 = 1.0d - (playerEntity.field_70161_v - playerZ);
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
        float var21 = EnchantmentHelper.func_185292_c(playerEntity) * 0.2f;
        float var22 = (((rBiomeFog / 255.0f) * 0.02f) + var21) / var20;
        float var23 = (((gBiomeFog / 255.0f) * 0.02f) + var21) / var20;
        return postProcessColor(world, playerEntity, var22, var23, ((bBiomeFog2 * 0.2f) + var21) / var20, renderPartialTicks);
    }

    private static Vec3d getFogBlendColor(World world, EntityLivingBase playerEntity, int playerX, int playerY, int playerZ, float defR, float defG, float defB, double renderPartialTicks) {
        int bScale;
        GameSettings settings = Minecraft.func_71410_x().field_71474_y;
        int[] ranges = ForgeModContainer.blendRanges;
        int distance = 0;
        if (settings.field_74347_j && settings.field_151451_c >= 0 && settings.field_151451_c < ranges.length) {
            distance = ranges[settings.field_151451_c];
        }
        float rBiomeFog = 0.0f;
        float gBiomeFog = 0.0f;
        float bBiomeFog = 0.0f;
        float weightBiomeFog = 0.0f;
        for (int celestialAngle = -distance; celestialAngle <= distance; celestialAngle++) {
            for (int baseScale = -distance; baseScale <= distance; baseScale++) {
                IBiomeFog biomeForCoordsBody = world.getBiomeForCoordsBody(new BlockPos(playerX + celestialAngle, playerY + celestialAngle, playerZ + baseScale));
                IDimensionFog iDimensionFog = world.field_73011_w;
                if (!DimensionUtil.isDimensionBlacklisted(iDimensionFog.func_186058_p()) && !BiomeUtil.isBiomeBlacklisted(biomeForCoordsBody)) {
                    if (iDimensionFog instanceof IDimensionFog) {
                        bScale = iDimensionFog.getFogColor(playerX + celestialAngle, playerY, playerZ + baseScale);
                    } else if (biomeForCoordsBody instanceof IBiomeFog) {
                        bScale = biomeForCoordsBody.getFogColor(playerX + celestialAngle, playerY, playerZ + baseScale);
                    } else {
                        bScale = FogWorldConfig.getFogColor(playerX + celestialAngle, playerY, playerZ + baseScale);
                    }
                    float rainStrength = (bScale & 16711680) >> 16;
                    float thunderStrength = (bScale & 65280) >> 8;
                    float processedColor = bScale & 255;
                    float weightMixed = 1.0f;
                    if (celestialAngle == (-distance)) {
                        double weightDefault = 1.0d - (playerEntity.field_70165_t - playerX);
                        rainStrength = (float) (rainStrength * weightDefault);
                        thunderStrength = (float) (thunderStrength * weightDefault);
                        processedColor = (float) (processedColor * weightDefault);
                        weightMixed = (float) (1.0f * weightDefault);
                    } else if (celestialAngle == distance) {
                        double weightDefault2 = playerEntity.field_70165_t - playerX;
                        rainStrength = (float) (rainStrength * weightDefault2);
                        thunderStrength = (float) (thunderStrength * weightDefault2);
                        processedColor = (float) (processedColor * weightDefault2);
                        weightMixed = (float) (1.0f * weightDefault2);
                    }
                    if (baseScale == (-distance)) {
                        double weightDefault3 = 1.0d - (playerEntity.field_70161_v - playerZ);
                        rainStrength = (float) (rainStrength * weightDefault3);
                        thunderStrength = (float) (thunderStrength * weightDefault3);
                        processedColor = (float) (processedColor * weightDefault3);
                        weightMixed = (float) (weightMixed * weightDefault3);
                    } else if (baseScale == distance) {
                        double weightDefault4 = playerEntity.field_70161_v - playerZ;
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
        float var28 = world.func_72826_c((float) renderPartialTicks);
        float var29 = MathHelper.func_76131_a((MathHelper.func_76134_b(var28 * 3.1415927f * 2.0f) * 2.0f) + 0.5f, 0.0f, 1.0f);
        float var30 = (var29 * 0.94f) + 0.06f;
        float var31 = (var29 * 0.94f) + 0.06f;
        float var32 = (var29 * 0.91f) + 0.09f;
        float rainStrength2 = world.func_72867_j((float) renderPartialTicks);
        if (rainStrength2 > 0.0f) {
            var30 *= 1.0f - (rainStrength2 * 0.5f);
            var31 *= 1.0f - (rainStrength2 * 0.5f);
            var32 *= 1.0f - (rainStrength2 * 0.4f);
        }
        float thunderStrength2 = world.func_72819_i((float) renderPartialTicks);
        if (thunderStrength2 > 0.0f) {
            var30 *= 1.0f - (thunderStrength2 * 0.5f);
            var31 *= 1.0f - (thunderStrength2 * 0.5f);
            var32 *= 1.0f - (thunderStrength2 * 0.5f);
        }
        Vec3d var33 = postProcessColor(world, playerEntity, rBiomeFog2 * (var30 / weightBiomeFog), gBiomeFog2 * (var31 / weightBiomeFog), bBiomeFog2 * (var32 / weightBiomeFog), renderPartialTicks);
        float rBiomeFog3 = (float) var33.field_72450_a;
        float gBiomeFog3 = (float) var33.field_72448_b;
        float bBiomeFog3 = (float) var33.field_72449_c;
        float weightMixed2 = distance * 2 * distance * 2;
        float var34 = weightMixed2 - weightBiomeFog;
        double rFinal = ((rBiomeFog3 * weightBiomeFog) + (defR * var34)) / weightMixed2;
        double gFinal = ((gBiomeFog3 * weightBiomeFog) + (defG * var34)) / weightMixed2;
        double bFinal = ((bBiomeFog3 * weightBiomeFog) + (defB * var34)) / weightMixed2;
        return new Vec3d(rFinal, gFinal, bFinal);
    }

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityPlayer entityLiving = event.getEntityLiving();
        World world = ((EntityLivingBase) entityLiving).field_70170_p;
        if (FogWorldConfig.poisonousFog && (entityLiving instanceof EntityPlayer) && !entityLiving.func_184812_l_() && !DimensionUtil.isDimensionBlacklisted(world.field_73011_w.func_186058_p()) && !BiomeUtil.isBiomeBlacklisted(world.func_180494_b(new BlockPos(entityLiving.field_70165_t, entityLiving.field_70163_u, entityLiving.field_70161_v))) && entityLiving.field_70173_aa > FogWorldConfig.posionTicks && !(world.field_73011_w instanceof IDimensionFog) && !(world.func_180494_b(new BlockPos(entityLiving.field_70165_t, entityLiving.field_70163_u, entityLiving.field_70161_v)) instanceof IBiomeFog) && world.func_175642_b(EnumSkyBlock.SKY, entityLiving.func_180425_c()) > 10) {
            entityLiving.func_70097_a(FogWorld.DAMAGEFOG, FogWorldConfig.poisonDamage);
        }
    }
}
