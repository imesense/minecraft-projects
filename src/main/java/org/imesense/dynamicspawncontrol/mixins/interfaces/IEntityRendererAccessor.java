package org.imesense.dynamicspawncontrol.mixins.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
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

    @Accessor("fogColorRed")
    float accessorGetFogColorRed();

    @Accessor("fogColorRed")
    void accessorSetFogColorRed(float value);

    @Accessor("fogColorGreen")
    float accessorGetFogColorGreen();

    @Accessor("fogColorGreen")
    void accessorSetFogColorGreen(float value);

    @Accessor("fogColorBlue")
    float accessorGetFogColorBlue();

    @Accessor("fogColorBlue")
    void accessorSetFogColorBlue(float value);

    @Accessor("fogColor1")
    float accessorGetFogColor1();

    @Accessor("fogColor2")
    float accessorGetFogColor2();

    @Accessor("bossColorModifier")
    float accessorGetBossColorModifier();

    @Accessor("bossColorModifierPrev")
    float accessorGetBossColorModifierPrev();

    @Accessor("farPlaneDistance")
    float accessorGetFarPlaneDistance();

    @Accessor("cloudFog")
    boolean accessorGetCloudFog();

    @Invoker("getNightVisionBrightness")
    float invokeGetNightVisionBrightness(EntityLivingBase entity, float partialTicks);

    @Invoker("setupFogColor")
    void invokeSetupFogColor(boolean black);
}