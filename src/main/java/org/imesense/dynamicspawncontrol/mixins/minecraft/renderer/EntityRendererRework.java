package org.imesense.dynamicspawncontrol.mixins.minecraft.renderer;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

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
    /**
     * @author OldSerpskiStalker
     * @reason Rendering dark nights
     */
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
    private void setupFog(int startCoords, float partialTicks)
    {
        IEntityRendererAccessor accessor = (IEntityRendererAccessor) this;

        Entity entity = accessor.accessorGetMinecraft().getRenderViewEntity();

        accessor.invokeSetupFogColor(false);

        GlStateManager.glNormal3f(0.0F, -1.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(
                accessor.accessorGetMinecraft().world, entity, partialTicks);

        float farPlaneDistance = accessor.accessorGetFarPlaneDistance();
        boolean cloudFog = accessor.accessorGetCloudFog();

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
        else if (cloudFog)
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
                    GlStateManager.setFogDensity(0.1F -
                            (float) EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.03F);
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
