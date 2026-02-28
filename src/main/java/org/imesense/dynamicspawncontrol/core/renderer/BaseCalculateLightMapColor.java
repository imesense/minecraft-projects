package org.imesense.dynamicspawncontrol.core.renderer;

import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IEntityRendererAccessor;

public abstract class BaseCalculateLightMapColor
{
    public static int calculateLightMapColor(IEntityRendererAccessor accessor, World world, float partialTicks, int i, float f, float f1)
    {
        float f2 = world.provider.getLightBrightnessTable()[i / 16] * f1;
        float f3 = world.provider.getLightBrightnessTable()[i % 16] * (accessor.getTorchFlickerX() * 0.1F + 1.5F);

        if (world.getLastLightningBolt() > 0)
        {
            f2 = world.provider.getLightBrightnessTable()[i / 16];
        }

        float f4 = f2 * (f * 0.65F + 0.35F);
        float f5 = f2 * (f * 0.65F + 0.35F);
        float f6 = f3 * ((f3 * 0.6F + 0.4F) * 0.6F + 0.4F);
        float f7 = f3 * (f3 * f3 * 0.6F + 0.4F);
        float f8 = f4 + f3;
        float f9 = f5 + f6;
        float f10 = f2 + f7;
        f8 = f8 * 0.96F + 0.03F;
        f9 = f9 * 0.96F + 0.03F;
        f10 = f10 * 0.96F + 0.03F;

        if (accessor.getBossColorModifier() > 0.0F)
        {
            float f11 = accessor.getBossColorModifierPrev() + (accessor.getBossColorModifier() - accessor.getBossColorModifierPrev()) * partialTicks;
            f8 = f8 * (1.0F - f11) + f8 * 0.7F * f11;
            f9 = f9 * (1.0F - f11) + f9 * 0.6F * f11;
            f10 = f10 * (1.0F - f11) + f10 * 0.6F * f11;
        }

        if (world.provider.getDimensionType().getId() == 1)
        {
            f8 = 0.22F + f3 * 0.75F;
            f9 = 0.28F + f6 * 0.75F;
            f10 = 0.25F + f7 * 0.75F;
        }

        float[] colors = {f8, f9, f10};
        world.provider.getLightmapColors(partialTicks, f, f2, f3, colors);
        f8 = colors[0]; f9 = colors[1]; f10 = colors[2];

        // Forge: fix MC-58177
        f8 = MathHelper.clamp(f8, 0f, 1f);
        f9 = MathHelper.clamp(f9, 0f, 1f);
        f10 = MathHelper.clamp(f10, 0f, 1f);

        if (accessor.getMinecraft().player.isPotionActive(MobEffects.NIGHT_VISION))
        {
            float f15 = accessor.invokeGetNightVisionBrightness(accessor.getMinecraft().player, partialTicks);
            float f12 = 1.0F / f8;

            if (f12 > 1.0F / f9)
            {
                f12 = 1.0F / f9;
            }

            if (f12 > 1.0F / f10)
            {
                f12 = 1.0F / f10;
            }

            f8 = f8 * (1.0F - f15) + f8 * f12 * f15;
            f9 = f9 * (1.0F - f15) + f9 * f12 * f15;
            f10 = f10 * (1.0F - f15) + f10 * f12 * f15;
        }

        if (f8 > 1.0F)
        {
            f8 = 1.0F;
        }

        if (f9 > 1.0F)
        {
            f9 = 1.0F;
        }

        if (f10 > 1.0F)
        {
            f10 = 1.0F;
        }

        float f16 = accessor.getMinecraft().gameSettings.gammaSetting;
        float f17 = 1.0F - f8;
        float f13 = 1.0F - f9;
        float f14 = 1.0F - f10;
        f17 = 1.0F - f17 * f17 * f17 * f17;
        f13 = 1.0F - f13 * f13 * f13 * f13;
        f14 = 1.0F - f14 * f14 * f14 * f14;
        f8 = f8 * (1.0F - f16) + f17 * f16;
        f9 = f9 * (1.0F - f16) + f13 * f16;
        f10 = f10 * (1.0F - f16) + f14 * f16;
        f8 = f8 * 0.96F + 0.03F;
        f9 = f9 * 0.96F + 0.03F;
        f10 = f10 * 0.96F + 0.03F;

        if (f8 > 1.0F)
        {
            f8 = 1.0F;
        }

        if (f9 > 1.0F)
        {
            f9 = 1.0F;
        }

        if (f10 > 1.0F)
        {
            f10 = 1.0F;
        }

        if (f8 < 0.0F)
        {
            f8 = 0.0F;
        }

        if (f9 < 0.0F)
        {
            f9 = 0.0F;
        }

        if (f10 < 0.0F)
        {
            f10 = 0.0F;
        }

        int j = 255;
        int k = (int)(f8 * 255.0F);
        int l = (int)(f9 * 255.0F);
        int i1 = (int)(f10 * 255.0F);

        return -16777216 | k << 16 | l << 8 | i1;
    }
}
