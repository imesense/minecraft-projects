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
    int[] getLightmapColors();

    @Accessor("lightmapUpdateNeeded")
    boolean getLightmapUpdateNeeded();

    @Accessor("lightmapUpdateNeeded")
    void setLightmapUpdateNeeded(boolean value);

    @Accessor("mc")
    Minecraft getMinecraft();

    @Accessor("bossColorModifier")
    float getBossColorModifier();

    @Accessor("bossColorModifierPrev")
    float getBossColorModifierPrev();

    @Accessor("torchFlickerX")
    float getTorchFlickerX();

    @Accessor("lightmapTexture")
    DynamicTexture getLightmapTexture();

    @Invoker("getNightVisionBrightness")
    float invokeGetNightVisionBrightness(EntityLivingBase entity, float partialTicks);
}
