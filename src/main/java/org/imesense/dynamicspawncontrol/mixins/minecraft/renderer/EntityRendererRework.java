package org.imesense.dynamicspawncontrol.mixins.minecraft.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer.NightRendererData;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IEntityRendererAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererRework
{
    @Overwrite
    private void updateLightmap(float partialTicks)
    {
        IEntityRendererAccessor accessor = (IEntityRendererAccessor) this;
        Minecraft minecraft = accessor.getMinecraft();

        if (accessor.getLightmapUpdateNeeded())
        {
            minecraft.mcProfiler.startSection("lightTex");
            World world = minecraft.world;

            if (world != null)
            {
                float sunBrightness = world.getSunBrightness(1.f);

                float upMultiplier;
                float downAdditive;
                float adjustedSunBrightness;

                boolean darkNightEnabled = NightRendererData.isEnableDarkNight();
                boolean nightVisionActive = minecraft.player.isPotionActive(MobEffects.NIGHT_VISION);

                if (nightVisionActive)
                {
                    adjustedSunBrightness = sunBrightness * 0.95f + 0.05f;
                    upMultiplier = 0.96f;
                    downAdditive = 0.03f;
                }
                else
                {
                    if (darkNightEnabled)
                    {
                        adjustedSunBrightness = sunBrightness;
                        upMultiplier = 1.0F;
                        downAdditive = 0.0F;
                    }
                    else
                    {
                        adjustedSunBrightness = sunBrightness * 0.95F + 0.05F;
                        upMultiplier = 0.96F;
                        downAdditive = 0.03F;
                    }
                }

                for (int i = 0; i < 256; ++i)
                {
                    float skyLight = world.provider.getLightBrightnessTable()[i / 16] * adjustedSunBrightness;
                    float blockLight = world.provider.getLightBrightnessTable()[i % 16] * (accessor.getTorchFlickerX() * 0.1f + 1.5f);

                    if (world.getLastLightningBolt() > 0)
                    {
                        skyLight = world.provider.getLightBrightnessTable()[i / 16];
                    }

                    float skyRed = skyLight * (sunBrightness * 0.65f + 0.35f);
                    float skyGreen = skyLight * (sunBrightness * 0.65f + 0.35f);
                    float blockRed = blockLight * ((blockLight * 0.6f + 0.4f) * 0.6f + 0.4f);
                    float blockGreen = blockLight * (blockLight * blockLight * 0.6f + 0.4f);

                    float combinedRed = skyRed + blockLight;
                    float combinedGreen = skyGreen + blockRed;
                    float combinedBlue = skyLight + blockGreen;

                    combinedRed = combinedRed * upMultiplier + downAdditive;
                    combinedGreen = combinedGreen * upMultiplier + downAdditive;
                    combinedBlue = combinedBlue * upMultiplier + downAdditive;

                    if (accessor.getBossColorModifier() > 0.f)
                    {
                        float bossModifier = accessor.getBossColorModifierPrev() +
                                (accessor.getBossColorModifier() - accessor.getBossColorModifierPrev()) * partialTicks;

                        combinedRed = combinedRed * (1.f - bossModifier) + combinedRed * 0.7f * bossModifier;
                        combinedGreen = combinedGreen * (1.f - bossModifier) + combinedGreen * 0.6f * bossModifier;
                        combinedBlue = combinedBlue * (1.f - bossModifier) + combinedBlue * 0.6f * bossModifier;
                    }

                    if (world.provider.getDimensionType().getId() == 1)
                    {
                        combinedRed = 0.22f + blockLight * 0.75f;
                        combinedGreen = 0.28f + blockRed * 0.75f;
                        combinedBlue = 0.25f + blockGreen * 0.75f;
                    }

                    float[] colors = { combinedRed, combinedGreen, combinedBlue };

                    world.provider.getLightmapColors(partialTicks, sunBrightness, skyLight, blockLight, colors);

                    combinedRed = colors[0];
                    combinedGreen = colors[1];
                    combinedBlue = colors[2];

                    combinedRed = MathHelper.clamp(combinedRed, 0.f, 1.f);
                    combinedGreen = MathHelper.clamp(combinedGreen, 0.f, 1.f);
                    combinedBlue = MathHelper.clamp(combinedBlue, 0.f, 1.f);

                    if (nightVisionActive)
                    {
                        float nightVisionStrength = accessor.invokeGetNightVisionBrightness(minecraft.player, partialTicks);
                        float maxComponent = 1.f / combinedRed;

                        if (maxComponent > 1.f / combinedGreen)
                        {
                            maxComponent = 1.f / combinedGreen;
                        }

                        if (maxComponent > 1.f / combinedBlue)
                        {
                            maxComponent = 1.f / combinedBlue;
                        }

                        combinedRed = combinedRed * (1.f - nightVisionStrength) + combinedRed * maxComponent * nightVisionStrength;
                        combinedGreen = combinedGreen * (1.f - nightVisionStrength) + combinedGreen * maxComponent * nightVisionStrength;
                        combinedBlue = combinedBlue * (1.f - nightVisionStrength) + combinedBlue * maxComponent * nightVisionStrength;
                    }

                    if (combinedRed > 1.f)
                    {
                        combinedRed = 1.f;
                    }

                    if (combinedGreen > 1.f)
                    {
                        combinedGreen = 1.f;
                    }

                    if (combinedBlue > 1.f)
                    {
                        combinedBlue = 1.f;
                    }

                    float gamma = minecraft.gameSettings.gammaSetting;

                    float inverseRed = 1.f - combinedRed;
                    float inverseGreen = 1.f - combinedGreen;
                    float inverseBlue = 1.f - combinedBlue;

                    inverseRed = 1.f - inverseRed * inverseRed * inverseRed * inverseRed;
                    inverseGreen = 1.f - inverseGreen * inverseGreen * inverseGreen * inverseGreen;
                    inverseBlue = 1.f - inverseBlue * inverseBlue * inverseBlue * inverseBlue;

                    combinedRed = combinedRed * (1.f - gamma) + inverseRed * gamma;
                    combinedGreen = combinedGreen * (1.f - gamma) + inverseGreen * gamma;
                    combinedBlue = combinedBlue * (1.f - gamma) + inverseBlue * gamma;

                    if (nightVisionActive)
                    {
                        combinedRed = combinedRed * 0.96f + 0.03f;
                        combinedGreen = combinedGreen * 0.96f + 0.03f;
                        combinedBlue = combinedBlue * 0.96f + 0.03f;
                    }
                    else
                    {
                        combinedRed = MathHelper.clamp(combinedRed, 0.f, 1.f);
                        combinedGreen = MathHelper.clamp(combinedGreen, 0f, 1.f);
                        combinedBlue = MathHelper.clamp(combinedBlue, 0.f, 1.f);
                    }

                    if (combinedRed > 1.f)
                    {
                        combinedRed = 1.f;
                    }

                    if (combinedGreen > 1.f)
                    {
                        combinedGreen = 1.f;
                    }

                    if (combinedBlue > 1.f)
                    {
                        combinedBlue = 1.f;
                    }

                    if (combinedRed < 0.f)
                    {
                        combinedRed = 0.f;
                    }

                    if (combinedGreen < 0.f)
                    {
                        combinedGreen = 0.f;
                    }

                    if (combinedBlue < 0.f)
                    {
                        combinedBlue = 0.f;
                    }

                    int redPixel = (int) (combinedRed * 255.f);
                    int greenPixel = (int) (combinedGreen * 255.f);
                    int bluePixel = (int) (combinedBlue * 255.f);

                    int result = -16777216 | redPixel << 16 | greenPixel << 8 | bluePixel;

                    accessor.getLightmapColors()[i] = result;
                }

                accessor.getLightmapTexture().updateDynamicTexture();
                accessor.setLightmapUpdateNeeded(false);
                minecraft.mcProfiler.endSection();
            }
        }
    }
}