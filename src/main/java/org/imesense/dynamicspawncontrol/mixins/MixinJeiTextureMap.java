package org.imesense.dynamicspawncontrol.mixins;

import com.google.common.collect.Lists;
import mezz.jei.gui.textures.JeiTextureMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JeiTextureMap.class)
public abstract class MixinJeiTextureMap
{
    @Shadow(remap = false)
    private TextureAtlasSprite missingImage;

    @Redirect(
            method = "loadTexture",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/gui/textures/JeiTextureMap;initMissingImage()V",
                    remap = false
            )
    )
    private void redirectInitMissingImage(JeiTextureMap instance)
    {
        // Инициализация missingImage вручную
        int[] aint = TextureUtil.MISSING_TEXTURE_DATA;
        missingImage.setIconWidth(16);
        missingImage.setIconHeight(16);
        int[][] aint1 = new int[1][];
        aint1[0] = aint;
        missingImage.setFramesTextureData(Lists.newArrayList());
    }
}