package org.imesense.dynamicspawncontrol.mixins.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.EntityLivingBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.FloatBuffer;

@Mixin(EntityRenderer.class)
@SuppressWarnings("UnusedMixin")
public interface IEntityRendererAccessor
{
    @Accessor("mc")
    Minecraft accessorGetMinecraft();

    @Accessor("lightmapTexture")
    DynamicTexture accessorGetLightmapTexture();

    @Accessor("torchFlickerX")
    float accessorGetTorchFlickerX();

    @Accessor("lightmapColors")
    int[] accessorGetLightmapColors();

    @Accessor("lightmapUpdateNeeded")
    boolean accessorGetLightmapUpdateNeeded();

    @Accessor("lightmapUpdateNeeded")
    void accessorSetLightmapUpdateNeeded(boolean value);

    @Accessor("bossColorModifier")
    float accessorGetBossColorModifier();

    @Accessor("bossColorModifierPrev")
    float accessorGetBossColorModifierPrev();

    @Invoker("getNightVisionBrightness")
    float invokeGetNightVisionBrightness(EntityLivingBase entity, float partialTicks);

    @Accessor("farPlaneDistance")
    float accessorGetFarPlaneDistance();

    @Accessor("cloudFog")
    boolean accessorGetCloudFog();

    @Invoker("setupFogColor")
    void invokeSetupFogColor(boolean black);

    @Accessor("fogColorRed")
    float accessorGetFogColorRed();

    @Accessor("fogColorGreen")
    float accessorGetFogColorGreen();

    @Accessor("fogColorBlue")
    float accessorGetFogColorBlue();

    @Invoker("setFogColorBuffer")
    FloatBuffer invokeSetFogColorBuffer(float red, float green, float blue, float alpha);
}
