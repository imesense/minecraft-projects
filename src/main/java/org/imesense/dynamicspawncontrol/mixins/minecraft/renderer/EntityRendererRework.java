package org.imesense.dynamicspawncontrol.mixins.minecraft.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import org.imesense.dynamicspawncontrol.core.pluginconfig.darkness.PluginDarknessConfig;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IEntityRendererAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.stream.IntStream;

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
                float sunBrightness = world.getSunBrightness(1.0F);
                float adjustedSunBrightness = sunBrightness * 0.95F + 0.05F;

                for (int i = 0; i < 256; ++i)
                {
                    float skyLight = world.provider.getLightBrightnessTable()[i / 16] * adjustedSunBrightness;
                    float blockLight = world.provider.getLightBrightnessTable()[i % 16] * (accessor.getTorchFlickerX() * 0.1F + 1.5F);

                    if (world.getLastLightningBolt() > 0)
                    {
                        skyLight = world.provider.getLightBrightnessTable()[i / 16];
                    }

                    float skyRed = skyLight * (sunBrightness * 0.65F + 0.35F);
                    float skyGreen = skyLight * (sunBrightness * 0.65F + 0.35F);
                    float blockRed = blockLight * ((blockLight * 0.6F + 0.4F) * 0.6F + 0.4F);
                    float blockGreen = blockLight * (blockLight * blockLight * 0.6F + 0.4F);

                    float combinedRed = skyRed + blockLight;
                    float combinedGreen = skyGreen + blockRed;
                    float combinedBlue = skyLight + blockGreen;

                    combinedRed = combinedRed * 0.96F + 0.03F;
                    combinedGreen = combinedGreen * 0.96F + 0.03F;
                    combinedBlue = combinedBlue * 0.96F + 0.03F;

                    if (accessor.getBossColorModifier() > 0.0F)
                    {
                        float bossModifier = accessor.getBossColorModifierPrev() + (accessor.getBossColorModifier() - accessor.getBossColorModifierPrev()) * partialTicks;

                        combinedRed = combinedRed * (1.0F - bossModifier) + combinedRed * 0.7F * bossModifier;
                        combinedGreen = combinedGreen * (1.0F - bossModifier) + combinedGreen * 0.6F * bossModifier;
                        combinedBlue = combinedBlue * (1.0F - bossModifier) + combinedBlue * 0.6F * bossModifier;
                    }

                    if (world.provider.getDimensionType().getId() == 1)
                    {
                        combinedRed = 0.22F + blockLight * 0.75F;
                        combinedGreen = 0.28F + blockRed * 0.75F;
                        combinedBlue = 0.25F + blockGreen * 0.75F;
                    }

                    float[] colors = {combinedRed, combinedGreen, combinedBlue};

                    world.provider.getLightmapColors(partialTicks, sunBrightness, skyLight, blockLight, colors);

                    combinedRed = colors[0];
                    combinedGreen = colors[1];
                    combinedBlue = colors[2];

                    combinedRed = MathHelper.clamp(combinedRed, 0f, 1f);
                    combinedGreen = MathHelper.clamp(combinedGreen, 0f, 1f);
                    combinedBlue = MathHelper.clamp(combinedBlue, 0f, 1f);

                    if (minecraft.player.isPotionActive(MobEffects.NIGHT_VISION))
                    {
                        float nightVisionStrength = getNightVisionBrightness(minecraft.player, partialTicks);
                        float maxComponent = 1.0F / combinedRed;

                        if (maxComponent > 1.0F / combinedGreen)
                        {
                            maxComponent = 1.0F / combinedGreen;
                        }

                        if (maxComponent > 1.0F / combinedBlue)
                        {
                            maxComponent = 1.0F / combinedBlue;
                        }

                        combinedRed = combinedRed * (1.0F - nightVisionStrength) + combinedRed * maxComponent * nightVisionStrength;
                        combinedGreen = combinedGreen * (1.0F - nightVisionStrength) + combinedGreen * maxComponent * nightVisionStrength;
                        combinedBlue = combinedBlue * (1.0F - nightVisionStrength) + combinedBlue * maxComponent * nightVisionStrength;
                    }

                    if (combinedRed > 1.0F)
                    {
                        combinedRed = 1.0F;
                    }

                    if (combinedGreen > 1.0F)
                    {
                        combinedGreen = 1.0F;
                    }

                    if (combinedBlue > 1.0F)
                    {
                        combinedBlue = 1.0F;
                    }

                    float gamma = minecraft.gameSettings.gammaSetting;

                    float inverseRed = 1.0F - combinedRed;
                    float inverseGreen = 1.0F - combinedGreen;
                    float inverseBlue = 1.0F - combinedBlue;

                    inverseRed = 1.0F - inverseRed * inverseRed * inverseRed * inverseRed;
                    inverseGreen = 1.0F - inverseGreen * inverseGreen * inverseGreen * inverseGreen;
                    inverseBlue = 1.0F - inverseBlue * inverseBlue * inverseBlue * inverseBlue;

                    combinedRed = combinedRed * (1.0F - gamma) + inverseRed * gamma;
                    combinedGreen = combinedGreen * (1.0F - gamma) + inverseGreen * gamma;
                    combinedBlue = combinedBlue * (1.0F - gamma) + inverseBlue * gamma;

                    combinedRed = combinedRed * 0.96F + 0.03F;
                    combinedGreen = combinedGreen * 0.96F + 0.03F;
                    combinedBlue = combinedBlue * 0.96F + 0.03F;

                    if (combinedRed > 1.0F)
                    {
                        combinedRed = 1.0F;
                    }

                    if (combinedGreen > 1.0F)
                    {
                        combinedGreen = 1.0F;
                    }

                    if (combinedBlue > 1.0F)
                    {
                        combinedBlue = 1.0F;
                    }

                    if (combinedRed < 0.0F)
                    {
                        combinedRed = 0.0F;
                    }

                    if (combinedGreen < 0.0F)
                    {
                        combinedGreen = 0.0F;
                    }

                    if (combinedBlue < 0.0F)
                    {
                        combinedBlue = 0.0F;
                    }

                    int redPixel = (int) (combinedRed * 255.0F);
                    int greenPixel = (int) (combinedGreen * 255.0F);
                    int bluePixel = (int) (combinedBlue * 255.0F);

                    int result = -16777216 | redPixel << 16 | greenPixel << 8 | bluePixel;

                    accessor.getLightmapColors()[i] = result;
                }

                accessor.getLightmapTexture().updateDynamicTexture();
                accessor.setLightmapUpdateNeeded(false);
                minecraft.mcProfiler.endSection();
            }
        }
    }

    private float getNightVisionBrightness(EntityLivingBase entity, float partialTicks)
    {
        int duration = entity.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
        return duration > 200 ? 1.0F : 0.7F + MathHelper.sin(((float)duration - partialTicks) * (float)Math.PI * 0.2F) * 0.3F;
    }
}