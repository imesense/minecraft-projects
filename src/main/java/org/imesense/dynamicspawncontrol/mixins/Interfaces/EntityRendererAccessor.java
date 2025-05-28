package org.imesense.dynamicspawncontrol.mixins.Interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityRenderer.class)
public interface EntityRendererAccessor
{
    @Accessor("lightmapColors")
    int[] getLightmapColors();

    @Accessor("lightmapUpdateNeeded")
    boolean getLightmapUpdateNeeded();

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
}
