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
    @Accessor("lightmapColors")
    int[] accessorGetLightmapColors();

    @Accessor("lightmapUpdateNeeded")
    boolean accessorGetLightmapUpdateNeeded();

    @Accessor("lightmapUpdateNeeded")
    void accessorSetLightmapUpdateNeeded(boolean value);

    @Accessor("mc")
    Minecraft accessorGetMinecraft();

    @Accessor("bossColorModifier")
    float accessorGetBossColorModifier();

    @Accessor("bossColorModifierPrev")
    float accessorGetBossColorModifierPrev();

    @Accessor("torchFlickerX")
    float accessorGetTorchFlickerX();

    @Accessor("lightmapTexture")
    DynamicTexture accessorGetLightmapTexture();

    @Invoker("getNightVisionBrightness")
    float invokeGetNightVisionBrightness(EntityLivingBase entity, float partialTicks);
}
