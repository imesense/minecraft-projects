package org.imesense.dynamicspawncontrol.core.renderer.night;

import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.bloodmoonmanager.ClientBloodmoonHandler;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IEntityRendererAccessor;

public abstract class BaseCalculateLightMapColor
{
    public static int calculateLightMapColor(IEntityRendererAccessor accessor, World world, float partialTicks, int i, float f, float f1)
    {
        float f2 = world.provider.getLightBrightnessTable()[i / 16] * f1;
        float f3 = world.provider.getLightBrightnessTable()[i % 16] * (accessor.accessorGetTorchFlickerX() * 0.1f + 1.5f);

        if (world.getLastLightningBolt() > 0)
        {
            f2 = world.provider.getLightBrightnessTable()[i / 16];
        }

        float f4 = f2 * (f * 0.65f + 0.35f);
        float f5 = f2 * (f * 0.65f + 0.35f);
        float f6 = f3 * ((f3 * 0.6f + 0.4f) * 0.6f + 0.4f);
        float f7 = f3 * (f3 * f3 * 0.6f + 0.4f);
        float f8 = f4 + f3;
        float f9 = f5 + f6;
        float f10 = f2 + f7;
        f8 = f8 * 0.96f + 0.03f;
        f9 = f9 * 0.96f + 0.03f;
        f10 = f10 * 0.96f + 0.03f;

        if (accessor.accessorGetBossColorModifier() > 0.0f)
        {
            float f11 = accessor.accessorGetBossColorModifierPrev() + (accessor.accessorGetBossColorModifier() -
                    accessor.accessorGetBossColorModifierPrev()) * partialTicks;
            f8 = f8 * (1.0f - f11) + f8 * 0.7f * f11;
            f9 = f9 * (1.0f - f11) + f9 * 0.6f * f11;
            f10 = f10 * (1.0f - f11) + f10 * 0.6f * f11;
        }

        if (world.provider.getDimensionType().getId() == 1)
        {
            f8 = 0.22f + f3 * 0.75f;
            f9 = 0.28f + f6 * 0.75f;
            f10 = 0.25f + f7 * 0.75f;
        }

        float[] colors = { f8, f9, f10 };
        world.provider.getLightmapColors(partialTicks, f, f2, f3, colors);
        f8 = colors[0]; f9 = colors[1]; f10 = colors[2];

        if (ClientBloodmoonHandler.INSTANCE.isBloodmoonActive())
        {
            int red = (int)(f8 * 255.0f);
            int green = (int)(f9 * 255.0f);
            int blue = (int)(f10 * 255.0f);

            red = ClientBloodmoonHandler.INSTANCE.manipulateRed(i, red);
            green = ClientBloodmoonHandler.INSTANCE.manipulateGreen(i, green);
            blue = ClientBloodmoonHandler.INSTANCE.manipulateBlue(i, blue);

            f8 = MathHelper.clamp(red / 255.0f, 0.0f, 1.0f);
            f9 = MathHelper.clamp(green / 255.0f, 0.0f, 1.0f);
            f10 = MathHelper.clamp(blue / 255.0f, 0.0f, 1.0f);
        }

        // Forge: fix MC-58177
        f8 = MathHelper.clamp(f8, 0.0f, 1.0f);
        f9 = MathHelper.clamp(f9, 0.0f, 1.0f);
        f10 = MathHelper.clamp(f10, 0.0f, 1.0f);

        if (accessor.accessorGetMinecraft().player.isPotionActive(MobEffects.NIGHT_VISION))
        {
            float f15 = accessor.invokeGetNightVisionBrightness(accessor.accessorGetMinecraft().player, partialTicks);
            float f12 = 1.0f / f8;

            if (f12 > 1.0f / f9)
            {
                f12 = 1.0f / f9;
            }

            if (f12 > 1.0f / f10)
            {
                f12 = 1.0f / f10;
            }

            f8 = f8 * (1.0f - f15) + f8 * f12 * f15;
            f9 = f9 * (1.0f - f15) + f9 * f12 * f15;
            f10 = f10 * (1.0f - f15) + f10 * f12 * f15;
        }

        if (f8 > 1.0f)
        {
            f8 = 1.0f;
        }

        if (f9 > 1.0f)
        {
            f9 = 1.0f;
        }

        if (f10 > 1.0f)
        {
            f10 = 1.0f;
        }

        float f16 = accessor.accessorGetMinecraft().gameSettings.gammaSetting;
        float f17 = 1.0f - f8;
        float f13 = 1.0f - f9;
        float f14 = 1.0f - f10;
        f17 = 1.0f - f17 * f17 * f17 * f17;
        f13 = 1.0f - f13 * f13 * f13 * f13;
        f14 = 1.0f - f14 * f14 * f14 * f14;
        f8 = f8 * (1.0f - f16) + f17 * f16;
        f9 = f9 * (1.0f - f16) + f13 * f16;
        f10 = f10 * (1.0f - f16) + f14 * f16;
        f8 = f8 * 0.96f + 0.03f;
        f9 = f9 * 0.96f + 0.03f;
        f10 = f10 * 0.96f + 0.03f;

        if (f8 > 1.0f)
        {
            f8 = 1.0f;
        }

        if (f9 > 1.0f)
        {
            f9 = 1.0f;
        }

        if (f10 > 1.0f)
        {
            f10 = 1.0f;
        }

        if (f8 < 0.0f)
        {
            f8 = 0.0f;
        }

        if (f9 < 0.0f)
        {
            f9 = 0.0f;
        }

        if (f10 < 0.0f)
        {
            f10 = 0.0f;
        }

        int k = (int) (f8 * 255.0f);
        int l = (int) (f9 * 255.0f);
        int i1 = (int) (f10 * 255.0f);

        return -16777216 | k << 16 | l << 8 | i1;
    }
}
