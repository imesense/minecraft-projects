package org.imesense.dynamicspawncontrol.mixins.minecraft.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.*;

@Mixin(LayerSpiderEyes.class)
@SuppressWarnings("UnusedMixin")
public abstract class LayerSpiderEyesFix<T extends EntitySpider> implements LayerRenderer<T>
{
    @Shadow @Final private static ResourceLocation SPIDER_EYES;

    @Shadow @Final private RenderSpider<T> spiderRenderer;

    public LayerSpiderEyesFix(RenderSpider<T> spiderRendererIn)
    {
        this.spiderRenderer = spiderRendererIn;
    }

    @Overwrite
    public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount,
                              float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.spiderRenderer.bindTexture(SPIDER_EYES);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());

        int i = 61680;
        int j = i % 65536;
        int k = i / 65536;

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);

        this.spiderRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount,
                ageInTicks, netHeadYaw, headPitch, scale);

        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);

        i = entitylivingbaseIn.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);

        this.spiderRenderer.setLightmap(entitylivingbaseIn);

        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }

    @Overwrite
    public boolean shouldCombineTextures()
    {
        return false;
    }
}
