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
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.core.renderer.night.color.ColorCombineBloodMoon;
import org.lwjgl.opengl.GLContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import org.imesense.dynamicspawncontrol.bloodmoonmanager.ClientBloodmoonHandler;
import org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer.NightRendererData;
import org.imesense.dynamicspawncontrol.core.renderer.night.BaseCalculateLightMapColor;
import org.imesense.dynamicspawncontrol.core.renderer.night.DarkCalculateLightMapColor;
import org.imesense.dynamicspawncontrol.mixins.minecraft.IEntityRendererAccessor;

import static org.imesense.dynamicspawncontrol.core.renderer.night.light.MoonLightStage.finalPackDarkColor;

@Mixin(EntityRenderer.class)
@SuppressWarnings("UnusedMixin")
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

            boolean canApplyDarkNight = isDarkNightEnabled &&
                    !hasNightVision &&
                    !isDimensionBlacklisted &&
                    !isLightningStorm;

            float bloodFactor = 0.0f;

            if (ClientBloodmoonHandler.INSTANCE.isBloodmoonActive())
            {
                float time = world.getWorldTime() % 24000.0f;

                if (time >= 11000.0f && time < 12000.0f)
                {
                    bloodFactor = (time - 11000.0f) / 1000.0f;
                }
                else if (time >= 12000.0f && time < 23000.0f)
                {
                    bloodFactor = 1.0f;
                }
                else if (time >= 23000.0f && time < 24000.0f)
                {
                    bloodFactor = 1.0f - ((time - 23000.0f) / 1000.0f);
                }
                else
                {
                    bloodFactor = 0.0f;
                }
            }

            bloodFactor = MathHelper.clamp(bloodFactor, 0.0f, 1.0f);

            bloodFactor = Math.max(bloodFactor, (float) ClientBloodmoonHandler.BLOODMOON_FOG_FACTOR);

            float darkNightFactor = canApplyDarkNight ? 1.0f : 0.0f;

            float finalDarkFactor = darkNightFactor * (1.0f - bloodFactor);
            finalDarkFactor = MathHelper.clamp(finalDarkFactor, 0.0f, 1.0f);

            float sunBrightness = world.getSunBrightness(1.0f);
            float moonBrightness = finalPackDarkColor(partialTicks, world);

            float vanillaSunBrightness = world.getSunBrightness(1.0F);
            float brightnessModifier = vanillaSunBrightness * 0.95f + 0.05f;

            for (int index = 0; index < 256; ++index)
            {
                int baseColor = BaseCalculateLightMapColor.calculateLightMapColor(
                        accessor, world, partialTicks, index,
                        vanillaSunBrightness, brightnessModifier
                );

                if (finalDarkFactor > 0.001f)
                {
                    int darkColor = DarkCalculateLightMapColor.calculateFinalLightMapColor(
                            accessor, world, partialTicks, index,
                            sunBrightness, moonBrightness, brightnessTable, dimensionType, baseColor
                    );

                    accessor.accessorGetLightmapColors()[index] = ColorCombineBloodMoon.blendRGB(baseColor, darkColor, finalDarkFactor);
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
    private void setupFog(int startCoords, float partialTicks)
    {
        IEntityRendererAccessor accessor = (IEntityRendererAccessor) this;

        Entity entity = accessor.accessorGetMinecraft().getRenderViewEntity();

        accessor.invokeSetupFogColor(false);

        GlStateManager.glNormal3f(0.0f, -1.0f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(
                accessor.accessorGetMinecraft().world, entity, partialTicks);

        float farPlaneDistance = accessor.accessorGetFarPlaneDistance();
        boolean cloudFog = accessor.accessorGetCloudFog();

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS))
        {
            float blindFogDistance = 5.0f;
            int blindDuration = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();

            if (blindDuration < 20)
            {
                blindFogDistance = 5.0f + (farPlaneDistance - 5.0f) * (1.0f - (float)blindDuration / 20.0f);
            }

            GlStateManager.setFog(GlStateManager.FogMode.LINEAR);

            if (startCoords == -1)
            {
                GlStateManager.setFogStart(0.0F);
                GlStateManager.setFogEnd(blindFogDistance * 0.8f);
            }
            else
            {
                GlStateManager.setFogStart(blindFogDistance * 0.25f);
                GlStateManager.setFogEnd(blindFogDistance);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance)
            {
                GlStateManager.glFogi(34138, 34139);
            }
        }
        else if (cloudFog)
        {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity(0.1f);
        }
        else if (iblockstate.getMaterial() == Material.WATER)
        {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);

            if (entity instanceof EntityLivingBase)
            {
                if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING))
                {
                    GlStateManager.setFogDensity(0.01f);
                }
                else
                {
                    GlStateManager.setFogDensity(0.1f -
                            (float) EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.03f);
                }
            }
            else
            {
                GlStateManager.setFogDensity(0.1f);
            }
        }
        else if (iblockstate.getMaterial() == Material.LAVA)
        {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity(2.0f);
        }
        else
        {
            float fogEndDistance = farPlaneDistance;
            GlStateManager.setFog(GlStateManager.FogMode.LINEAR);

            if (ClientBloodmoonHandler.BLOODMOON_FOG_FACTOR > 0.0f)
            {
                final float TARGET_START_MULT = 0.05f;
                final float TARGET_END_MULT = 0.1f;

                final float NORMAL_START_MULT = 0.75f;
                final float NORMAL_END_MULT = 1.0f;

                float factor = ClientBloodmoonHandler.BLOODMOON_FOG_FACTOR;

                factor = MathHelper.clamp(factor, 0.0f, 1.0f);
                factor = factor * factor * (3.0f - 2.0f * factor);

                if (factor > 1.0f)
                {
                    factor = 1.0f;
                }

                if (factor < 0.0f)
                {
                    factor = 0.0f;
                }

                float fogStartMultiplier = NORMAL_START_MULT + (TARGET_START_MULT - NORMAL_START_MULT) * factor;
                float fogEndMultiplier = NORMAL_END_MULT + (TARGET_END_MULT - NORMAL_END_MULT) * factor;

                if (startCoords == -1)
                {
                    GlStateManager.setFogStart(0.0f);
                    GlStateManager.setFogEnd(fogEndDistance * fogEndMultiplier);
                }
                else
                {
                    GlStateManager.setFogStart(fogEndDistance * fogStartMultiplier);
                    GlStateManager.setFogEnd(fogEndDistance * fogEndMultiplier);
                }
            }
            else
            {
                if (startCoords == -1)
                {
                    GlStateManager.setFogStart(0.0f);
                    GlStateManager.setFogEnd(fogEndDistance);
                }
                else
                {
                    GlStateManager.setFogStart(fogEndDistance * 0.75f);
                    GlStateManager.setFogEnd(fogEndDistance);
                }
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance)
            {
                GlStateManager.glFogi(34138, 34139);
            }

            if (accessor.accessorGetMinecraft().world.provider.doesXZShowFog((int)entity.posX, (int)entity.posZ) ||
                    accessor.accessorGetMinecraft().ingameGUI.getBossOverlay().shouldCreateFog())
            {
                GlStateManager.setFogStart(fogEndDistance * 0.05f);
                GlStateManager.setFogEnd(Math.min(fogEndDistance, 192.0f) * 0.5f);
            }
        }

        GlStateManager.enableColorMaterial();
        GlStateManager.enableFog();
        GlStateManager.colorMaterial(1028, 4608);
    }

    @Overwrite
    public void setupFogColor(boolean black)
    {
        IEntityRendererAccessor accessor = (IEntityRendererAccessor) this;

        if (black)
        {
            GlStateManager.glFog(2918,
                    accessor.invokeSetFogColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        }
        else
        {
            float red = accessor.accessorGetFogColorRed();
            float green = accessor.accessorGetFogColorGreen();
            float blue = accessor.accessorGetFogColorBlue();

            if (ClientBloodmoonHandler.BLOODMOON_FOG_FACTOR > 0.0f)
            {
                GlStateManager.glFog(2918, accessor.invokeSetFogColorBuffer(red, green, blue, 1.0f));
            }
            else
            {
                GlStateManager.glFog(2918, accessor.invokeSetFogColorBuffer(red, green, blue, 1.0f));
            }
        }
    }

    @Overwrite
    private void updateFogColor(float partialTicks)
    {
        IEntityRendererAccessor accessor = (IEntityRendererAccessor) this;
        Minecraft mc = accessor.accessorGetMinecraft();
        World world = mc.world;
        Entity entity = mc.getRenderViewEntity();

        float f = 0.25F + 0.75F * (float) mc.gameSettings.renderDistanceChunks / 32.0F;
        f = 1.0F - (float) Math.pow(f, 0.25F);

        Vec3d vec3d = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
        float f1 = (float) vec3d.x;
        float f2 = (float) vec3d.y;
        float f3 = (float) vec3d.z;

        Vec3d vec3d1 = world.getFogColor(partialTicks);
        accessor.accessorSetFogColorRed((float) vec3d1.x);
        accessor.accessorSetFogColorGreen((float) vec3d1.y);
        accessor.accessorSetFogColorBlue((float) vec3d1.z);

        if (mc.gameSettings.renderDistanceChunks >= 4)
        {
            double d0 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) > 0.0F ? -1.0F : 1.0F;
            Vec3d vec3d2 = new Vec3d(d0, 0.0D, 0.0D);
            float f5 = (float) entity.getLook(partialTicks).dotProduct(vec3d2);

            if (f5 < 0.0F) f5 = 0.0F;

            if (f5 > 0.0F)
            {
                float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
                if (afloat != null)
                {
                    f5 *= afloat[3];
                    accessor.accessorSetFogColorRed(accessor.accessorGetFogColorRed() * (1.0F - f5) + afloat[0] * f5);
                    accessor.accessorSetFogColorGreen(accessor.accessorGetFogColorGreen() * (1.0F - f5) + afloat[1] * f5);
                    accessor.accessorSetFogColorBlue(accessor.accessorGetFogColorBlue() * (1.0F - f5) + afloat[2] * f5);
                }
            }
        }

        accessor.accessorSetFogColorRed(accessor.accessorGetFogColorRed() + (f1 - accessor.accessorGetFogColorRed()) * f);
        accessor.accessorSetFogColorGreen(accessor.accessorGetFogColorGreen() + (f2 - accessor.accessorGetFogColorGreen()) * f);
        accessor.accessorSetFogColorBlue(accessor.accessorGetFogColorBlue() + (f3 - accessor.accessorGetFogColorBlue()) * f);

        float rainStrength = world.getRainStrength(partialTicks);

        if (rainStrength > 0.0F)
        {
            float f4 = 1.0F - rainStrength * 0.5F;
            float f10 = 1.0F - rainStrength * 0.4F;
            accessor.accessorSetFogColorRed(accessor.accessorGetFogColorRed() * f4);
            accessor.accessorSetFogColorGreen(accessor.accessorGetFogColorGreen() * f4);
            accessor.accessorSetFogColorBlue(accessor.accessorGetFogColorBlue() * f10);
        }

        float thunderStrength = world.getThunderStrength(partialTicks);
        if (thunderStrength > 0.0F)
        {
            float f11 = 1.0F - thunderStrength * 0.5F;
            accessor.accessorSetFogColorRed(accessor.accessorGetFogColorRed() * f11);
            accessor.accessorSetFogColorGreen(accessor.accessorGetFogColorGreen() * f11);
            accessor.accessorSetFogColorBlue(accessor.accessorGetFogColorBlue() * f11);
        }

        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(world, entity, partialTicks);

        if (accessor.accessorGetCloudFog())
        {
            Vec3d cloudColor = world.getCloudColour(partialTicks);
            accessor.accessorSetFogColorRed((float) cloudColor.x);
            accessor.accessorSetFogColorGreen((float) cloudColor.y);
            accessor.accessorSetFogColorBlue((float) cloudColor.z);
        }
        else
        {
            Vec3d viewport = ActiveRenderInfo.projectViewFromEntity(entity, partialTicks);
            BlockPos viewportPos = new BlockPos(viewport);
            IBlockState viewportState = world.getBlockState(viewportPos);
            Vec3d materialColor = viewportState.getBlock().getFogColor(world, viewportPos, viewportState, entity,
                    new Vec3d(accessor.accessorGetFogColorRed(), accessor.accessorGetFogColorGreen(), accessor.accessorGetFogColorBlue()), partialTicks);
            accessor.accessorSetFogColorRed((float) materialColor.x);
            accessor.accessorSetFogColorGreen((float) materialColor.y);
            accessor.accessorSetFogColorBlue((float) materialColor.z);
        }

        float f13 = accessor.accessorGetFogColor2() + (accessor.accessorGetFogColor1() - accessor.accessorGetFogColor2()) * partialTicks;
        accessor.accessorSetFogColorRed(accessor.accessorGetFogColorRed() * f13);
        accessor.accessorSetFogColorGreen(accessor.accessorGetFogColorGreen() * f13);
        accessor.accessorSetFogColorBlue(accessor.accessorGetFogColorBlue() * f13);

        double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks) * world.provider.getVoidFogYFactor();

        if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(MobEffects.BLINDNESS))
        {
            int duration = ((EntityLivingBase) entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();

            if (duration < 20)
            {
                d1 *= (1.0F - (float) duration / 20.0F);
            }
            else
            {
                d1 = 0.0D;
            }
        }

        if (d1 < 1.0D)
        {
            if (d1 < 0.0D) d1 = 0.0D;
            d1 *= d1;
            accessor.accessorSetFogColorRed((float) (accessor.accessorGetFogColorRed() * d1));
            accessor.accessorSetFogColorGreen((float) (accessor.accessorGetFogColorGreen() * d1));
            accessor.accessorSetFogColorBlue((float) (accessor.accessorGetFogColorBlue() * d1));
        }

        float bossModifier = accessor.accessorGetBossColorModifier();

        if (bossModifier > 0.0F)
        {
            float f14 = accessor.accessorGetBossColorModifierPrev() + (bossModifier - accessor.accessorGetBossColorModifierPrev()) * partialTicks;
            accessor.accessorSetFogColorRed(accessor.accessorGetFogColorRed() * (1.0F - f14) + accessor.accessorGetFogColorRed() * 0.7F * f14);
            accessor.accessorSetFogColorGreen(accessor.accessorGetFogColorGreen() * (1.0F - f14) + accessor.accessorGetFogColorGreen() * 0.6F * f14);
            accessor.accessorSetFogColorBlue(accessor.accessorGetFogColorBlue() * (1.0F - f14) + accessor.accessorGetFogColorBlue() * 0.6F * f14);
        }

        if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(MobEffects.NIGHT_VISION))
        {
            float nightVision = accessor.invokeGetNightVisionBrightness((EntityLivingBase) entity, partialTicks);
            float maxComponent = 1.0F / accessor.accessorGetFogColorRed();

            if (maxComponent > 1.0F / accessor.accessorGetFogColorGreen())
                maxComponent = 1.0F / accessor.accessorGetFogColorGreen();
            if (maxComponent > 1.0F / accessor.accessorGetFogColorBlue())
                maxComponent = 1.0F / accessor.accessorGetFogColorBlue();
            if (Float.isInfinite(maxComponent))
                maxComponent = Math.nextAfter(maxComponent, 0.0F);

            accessor.accessorSetFogColorRed(accessor.accessorGetFogColorRed() * (1.0F - nightVision) + accessor.accessorGetFogColorRed() * maxComponent * nightVision);
            accessor.accessorSetFogColorGreen(accessor.accessorGetFogColorGreen() * (1.0F - nightVision) + accessor.accessorGetFogColorGreen() * maxComponent * nightVision);
            accessor.accessorSetFogColorBlue(accessor.accessorGetFogColorBlue() * (1.0F - nightVision) + accessor.accessorGetFogColorBlue() * maxComponent * nightVision);
        }

        float bloodFactor = MathHelper.clamp(ClientBloodmoonHandler.BLOODMOON_FOG_FACTOR, 0.0f, 1.0f);

        if (bloodFactor > 0.0f && ClientBloodmoonHandler.INSTANCE.isBloodmoonActive())
        {
            float time = world.getWorldTime() % 24000.0f;

            float targetRed = 0.65f;
            float targetGreen = 0.15f;
            float targetBlue = 0.15f;

            float blendStrength;

            if (time >= 12000.0f && time < 14000.0f)
            {
                blendStrength = (time - 12000.0f) / 2000.0f;
                blendStrength = blendStrength * blendStrength * (3.0f - 2.0f * blendStrength);
            }
            else if (time >= 14000.0f && time < 22000.0f)
            {
                blendStrength = 1.0f;
            }
            else if (time >= 22000.0f && time < 24000.0f)
            {
                blendStrength = 1.0f - ((time - 22000.0f) / 2000.0f);
                blendStrength = blendStrength * blendStrength * (3.0f - 2.0f * blendStrength);
            }
            else if (time >= 11000.0f && time < 12000.0f)
            {
                blendStrength = (time - 11000.0f) / 1000.0f * 0.2f;
            }
            else
            {
                blendStrength = 0.0f;
            }

            blendStrength = MathHelper.clamp(blendStrength, 0.0f, 1.0f);
            blendStrength = blendStrength * bloodFactor;

            float currentRed = accessor.accessorGetFogColorRed();
            float currentGreen = accessor.accessorGetFogColorGreen();
            float currentBlue = accessor.accessorGetFogColorBlue();

            float sunsetStrength = MathHelper.clamp((currentRed - Math.max(currentGreen, currentBlue)) * 2.0f, 0.0f, 1.0f);

            if (sunsetStrength > 0.3f && time < 12000.0f)
            {
                float finalRed = currentRed * (1.0f - blendStrength * 0.7f) + targetRed * blendStrength * 0.7f;
                float finalGreen = currentGreen * (1.0f - blendStrength * 0.5f);
                float finalBlue = currentBlue * (1.0f - blendStrength * 0.6f);

                accessor.accessorSetFogColorRed(MathHelper.clamp(finalRed, 0.0f, 1.0f));
                accessor.accessorSetFogColorGreen(MathHelper.clamp(finalGreen, 0.0f, 1.0f));
                accessor.accessorSetFogColorBlue(MathHelper.clamp(finalBlue, 0.0f, 1.0f));
            }
            else
            {
                float finalRed = currentRed * (1.0f - blendStrength) + targetRed * blendStrength;
                float finalGreen = currentGreen * (1.0f - blendStrength) + targetGreen * blendStrength;
                float finalBlue = currentBlue * (1.0f - blendStrength) + targetBlue * blendStrength;

                accessor.accessorSetFogColorRed(MathHelper.clamp(finalRed, 0.0f, 1.0f));
                accessor.accessorSetFogColorGreen(MathHelper.clamp(finalGreen, 0.0f, 1.0f));
                accessor.accessorSetFogColorBlue(MathHelper.clamp(finalBlue, 0.0f, 1.0f));
            }
        }

        if (mc.gameSettings.anaglyph)
        {
            float r = accessor.accessorGetFogColorRed();
            float g = accessor.accessorGetFogColorGreen();
            float b = accessor.accessorGetFogColorBlue();
            float gray = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float redGreen = (r * 30.0F + g * 70.0F) / 100.0F;
            float redBlue = (r * 30.0F + b * 70.0F) / 100.0F;
            accessor.accessorSetFogColorRed(gray);
            accessor.accessorSetFogColorGreen(redGreen);
            accessor.accessorSetFogColorBlue(redBlue);
        }

        EntityViewRenderEvent.FogColors event = new EntityViewRenderEvent.FogColors((EntityRenderer) (Object) this, entity, iblockstate, partialTicks,
                accessor.accessorGetFogColorRed(), accessor.accessorGetFogColorGreen(), accessor.accessorGetFogColorBlue());
        MinecraftForge.EVENT_BUS.post(event);

        accessor.accessorSetFogColorRed(event.getRed());
        accessor.accessorSetFogColorGreen(event.getGreen());
        accessor.accessorSetFogColorBlue(event.getBlue());

        GlStateManager.clearColor(accessor.accessorGetFogColorRed(), accessor.accessorGetFogColorGreen(), accessor.accessorGetFogColorBlue(), 0.0F);
    }
}
