package org.imesense.dynamicspawncontrol.mixins.minecraft.renderer;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.init.MobEffects;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import org.imesense.dynamicspawncontrol.bloodmoonmanager.ClientBloodmoonHandler;
import org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer.NightRendererData;
import org.imesense.dynamicspawncontrol.core.renderer.night.BaseCalculateLightMapColor;
import org.imesense.dynamicspawncontrol.core.renderer.night.DarkCalculateLightMapColor;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IEntityRendererAccessor;

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
}
