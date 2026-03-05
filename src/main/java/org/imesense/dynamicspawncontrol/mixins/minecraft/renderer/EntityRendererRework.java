package org.imesense.dynamicspawncontrol.mixins.minecraft.renderer;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
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
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.bloodmoonmanager.ClientBloodmoonHandler;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer.NightRendererData;
import org.imesense.dynamicspawncontrol.core.renderer.night.BaseCalculateLightMapColor;
import org.imesense.dynamicspawncontrol.core.renderer.night.DarkCalculateLightMapColor;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IEntityRendererAccessor;
import org.lwjgl.opengl.GLContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static org.imesense.dynamicspawncontrol.core.renderer.night.light.MoonLightStage.finalPackDarkColor;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererRework
{
    @Overwrite
    private void updateLightmap(float partialTicks)
    {
        IEntityRendererAccessor accessor = (IEntityRendererAccessor) this;

        if (accessor.accessorGetLightmapUpdateNeeded())
        {
            accessor.accessorGetMinecraft().mcProfiler.startSection("lightTex");

            World world = accessor.accessorGetMinecraft().world;
            WorldProvider dimension = world.provider;
            DimensionType dimensionType = dimension.getDimensionType();

            float[] brightnessTable = dimension.getLightBrightnessTable();

            boolean isLightningStorm = world.getLastLightningBolt() > 0;
            boolean isDarkNightEnabled = NightRendererData.isEnableDarkNight();

            boolean hasNightVision = accessor.accessorGetMinecraft().player.isPotionActive(MobEffects.NIGHT_VISION);
            boolean isDimensionBlacklisted = NightRendererData.isDimensionBlacklisted(dimensionType.getId());

            boolean shouldApplyDarkness = isDarkNightEnabled &&
                    !hasNightVision && !isDimensionBlacklisted && !isLightningStorm && !ClientBloodmoonHandler.INSTANCE.isBloodmoonActive();

            float sunBrightness = world.getSunBrightness(1.0f);
            float moonBrightness = finalPackDarkColor(partialTicks, world);

            float vanillaSunBrightness = world.getSunBrightness(1.0F);
            float brightnessModifier = vanillaSunBrightness * 0.95f + 0.05f;

            for (int index = 0; index < 256; ++index)
            {
                int baseColor = BaseCalculateLightMapColor.calculateLightMapColor(accessor, world, partialTicks, index,
                        vanillaSunBrightness, brightnessModifier);

                if (shouldApplyDarkness)
                {
                    int darkenedColor = DarkCalculateLightMapColor.calculateFinalLightMapColor(accessor, world, partialTicks, index,
                            sunBrightness, moonBrightness, brightnessTable, dimensionType, baseColor);

                    accessor.accessorGetLightmapColors()[index] = darkenedColor;
                }
                else
                {
                    accessor.accessorGetLightmapColors()[index] = baseColor;
                }
            }

            accessor.accessorGetLightmapTexture().updateDynamicTexture();
            accessor.accessorSetLightmapUpdateNeeded(false);
            accessor.accessorGetMinecraft().mcProfiler.endSection();
        }
    }


    @Overwrite
    private void updateFogColor(float partialTicks)
    {
        IEntityRendererAccessor accessor = (IEntityRendererAccessor) this;

        World world = accessor.accessorGetMinecraft().world;
        Entity entity = accessor.accessorGetMinecraft().getRenderViewEntity();
        float f = 0.25F + 0.75F * (float)accessor.accessorGetMinecraft().gameSettings.renderDistanceChunks / 32.0F;
        f = 1.0F - (float)Math.pow((double)f, 0.25D);
        Vec3d vec3d = world.getSkyColor(accessor.accessorGetMinecraft().getRenderViewEntity(), partialTicks);
        float f1 = (float)vec3d.x;
        float f2 = (float)vec3d.y;
        float f3 = (float)vec3d.z;
        Vec3d vec3d1 = world.getFogColor(partialTicks);

        // Используем accessor для установки значений
        accessor.accessorSetFogColorRed((float)vec3d1.x);
        accessor.accessorSetFogColorGreen((float)vec3d1.y);
        accessor.accessorSetFogColorBlue((float)vec3d1.z);

        float fogColorRed = accessor.accessorGetFogColorRed();
        float fogColorGreen = accessor.accessorGetFogColorGreen();
        float fogColorBlue = accessor.accessorGetFogColorBlue();

        if (accessor.accessorGetMinecraft().gameSettings.renderDistanceChunks >= 4)
        {
            double d0 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) > 0.0F ? -1.0D : 1.0D;
            Vec3d vec3d2 = new Vec3d(d0, 0.0D, 0.0D);
            float f5 = (float)entity.getLook(partialTicks).dotProduct(vec3d2);

            if (f5 < 0.0F)
            {
                f5 = 0.0F;
            }

            if (f5 > 0.0F)
            {
                float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);

                if (afloat != null)
                {
                    f5 = f5 * afloat[3];
                    fogColorRed = fogColorRed * (1.0F - f5) + afloat[0] * f5;
                    fogColorGreen = fogColorGreen * (1.0F - f5) + afloat[1] * f5;
                    fogColorBlue = fogColorBlue * (1.0F - f5) + afloat[2] * f5;
                }
            }
        }

        fogColorRed += (f1 - fogColorRed) * f;
        fogColorGreen += (f2 - fogColorGreen) * f;
        fogColorBlue += (f3 - fogColorBlue) * f;

        float f8 = world.getRainStrength(partialTicks);

        if (f8 > 0.0F)
        {
            float f4 = 1.0F - f8 * 0.5F;
            float f10 = 1.0F - f8 * 0.4F;
            fogColorRed *= f4;
            fogColorGreen *= f4;
            fogColorBlue *= f10;
        }

        float f9 = world.getThunderStrength(partialTicks);

        if (f9 > 0.0F)
        {
            float f11 = 1.0F - f9 * 0.5F;
            fogColorRed *= f11;
            fogColorGreen *= f11;
            fogColorBlue *= f11;
        }

        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(accessor.accessorGetMinecraft().world, entity, partialTicks);

        if (accessor.accessorGetCloudFog())
        {
            Vec3d vec3d3 = world.getCloudColour(partialTicks);
            fogColorRed = (float)vec3d3.x;
            fogColorGreen = (float)vec3d3.y;
            fogColorBlue = (float)vec3d3.z;
        }
        else if (iblockstate.getMaterial() == Material.WATER)
        {
            float f12 = 0.0F;

            if (entity instanceof EntityLivingBase)
            {
                f12 = (float) EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.2F;

                if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING))
                {
                    f12 = f12 * 0.3F + 0.6F;
                }
            }

            fogColorRed = 0.02F + f12;
            fogColorGreen = 0.02F + f12;
            fogColorBlue = 0.2F + f12;
        }
        else if (iblockstate.getMaterial() == Material.LAVA)
        {
            fogColorRed = 0.6F;
            fogColorGreen = 0.1F;
            fogColorBlue = 0.0F;
        }

        float f13 = accessor.accessorGetFogColor2() + (accessor.accessorGetFogColor1() - accessor.accessorGetFogColor2()) * partialTicks;
        fogColorRed *= f13;
        fogColorGreen *= f13;
        fogColorBlue *= f13;

        double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks) * world.provider.getVoidFogYFactor();

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS))
        {
            int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();

            if (i < 20)
            {
                d1 *= (double)(1.0F - (float)i / 20.0F);
            }
            else
            {
                d1 = 0.0D;
            }
        }

        if (d1 < 1.0D)
        {
            if (d1 < 0.0D)
            {
                d1 = 0.0D;
            }

            d1 = d1 * d1;
            fogColorRed = (float)((double)fogColorRed * d1);
            fogColorGreen = (float)((double)fogColorGreen * d1);
            fogColorBlue = (float)((double)fogColorBlue * d1);
        }

        float bossModifier = accessor.accessorGetBossColorModifier();
        float bossModifierPrev = accessor.accessorGetBossColorModifierPrev();

        if (bossModifier > 0.0F)
        {
            float f14 = bossModifierPrev + (bossModifier - bossModifierPrev) * partialTicks;
            fogColorRed = fogColorRed * (1.0F - f14) + fogColorRed * 0.7F * f14;
            fogColorGreen = fogColorGreen * (1.0F - f14) + fogColorGreen * 0.6F * f14;
            fogColorBlue = fogColorBlue * (1.0F - f14) + fogColorBlue * 0.6F * f14;
        }

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.NIGHT_VISION))
        {
            float f15 = accessor.invokeGetNightVisionBrightness((EntityLivingBase)entity, partialTicks);
            float f6 = 1.0F / fogColorRed;

            if (f6 > 1.0F / fogColorGreen)
            {
                f6 = 1.0F / fogColorGreen;
            }

            if (f6 > 1.0F / fogColorBlue)
            {
                f6 = 1.0F / fogColorBlue;
            }

            fogColorRed = fogColorRed * (1.0F - f15) + fogColorRed * f6 * f15;
            fogColorGreen = fogColorGreen * (1.0F - f15) + fogColorGreen * f6 * f15;
            fogColorBlue = fogColorBlue * (1.0F - f15) + fogColorBlue * f6 * f15;
        }

        if (accessor.accessorGetMinecraft().gameSettings.anaglyph)
        {
            float f16 = (fogColorRed * 30.0F + fogColorGreen * 59.0F + fogColorBlue * 11.0F) / 100.0F;
            float f17 = (fogColorRed * 30.0F + fogColorGreen * 70.0F) / 100.0F;
            float f7 = (fogColorRed * 30.0F + fogColorBlue * 70.0F) / 100.0F;
            fogColorRed = f16;
            fogColorGreen = f17;
            fogColorBlue = f7;
        }

        // Сохраняем финальные значения обратно через accessor
        accessor.accessorSetFogColorRed(fogColorRed);
        accessor.accessorSetFogColorGreen(fogColorGreen);
        accessor.accessorSetFogColorBlue(fogColorBlue);

        GlStateManager.clearColor(fogColorRed, fogColorGreen, fogColorBlue, 0.0F);
    }

    @Overwrite
    private void setupFog(int startCoords, float partialTicks)
    {
        IEntityRendererAccessor accessor = (IEntityRendererAccessor) this;

        Entity entity = accessor.accessorGetMinecraft().getRenderViewEntity();
        accessor.invokeSetupFogColor(false);
        GlStateManager.glNormal3f(0.0F, -1.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(accessor.accessorGetMinecraft().world, entity, partialTicks);

        float farPlaneDistance = accessor.accessorGetFarPlaneDistance();

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS))
        {
            float f1 = 5.0F;
            int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();

            if (i < 20)
            {
                f1 = 5.0F + (farPlaneDistance - 5.0F) * (1.0F - (float)i / 20.0F);
            }

            GlStateManager.setFog(GlStateManager.FogMode.LINEAR);

            if (startCoords == -1)
            {
                GlStateManager.setFogStart(0.0F);
                GlStateManager.setFogEnd(f1 * 0.8F);
            }
            else
            {
                GlStateManager.setFogStart(f1 * 0.25F);
                GlStateManager.setFogEnd(f1);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance)
            {
                GlStateManager.glFogi(34138, 34139);
            }
        }
        else if (accessor.accessorGetCloudFog())
        {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity(0.1F);
        }
        else if (iblockstate.getMaterial() == Material.WATER)
        {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);

            if (entity instanceof EntityLivingBase)
            {
                if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING))
                {
                    GlStateManager.setFogDensity(0.01F);
                }
                else
                {
                    GlStateManager.setFogDensity(0.1F - (float)EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.03F);
                }
            }
            else
            {
                GlStateManager.setFogDensity(0.1F);
            }
        }
        else if (iblockstate.getMaterial() == Material.LAVA)
        {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity(2.0F);
        }
        else
        {
            float f = farPlaneDistance;

            GlStateManager.setFog(GlStateManager.FogMode.LINEAR);

            if (startCoords == -1)
            {
                GlStateManager.setFogStart(0.0F);
                GlStateManager.setFogEnd(f);
            }
            else
            {
                GlStateManager.setFogStart(f * 0.75F);
                GlStateManager.setFogEnd(f);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance)
            {
                GlStateManager.glFogi(34138, 34139);
            }

            if (accessor.accessorGetMinecraft().world.provider.doesXZShowFog((int)entity.posX, (int)entity.posZ) ||
                    accessor.accessorGetMinecraft().ingameGUI.getBossOverlay().shouldCreateFog())
            {
                GlStateManager.setFogStart(f * 0.05F);
                GlStateManager.setFogEnd(Math.min(f, 192.0F) * 0.5F);
            }
        }

        GlStateManager.enableColorMaterial();
        GlStateManager.enableFog();

        GlStateManager.colorMaterial(1028, 4608);
    }
}