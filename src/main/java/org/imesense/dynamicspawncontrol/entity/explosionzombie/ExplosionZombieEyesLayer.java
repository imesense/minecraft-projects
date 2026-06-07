package org.imesense.dynamicspawncontrol.entity.explosionzombie;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class ExplosionZombieEyesLayer implements LayerRenderer<ExplosionZombieEntity>
{
    private static final ResourceLocation EYES =
            new ResourceLocation("dynamicspawncontrol",
                    "textures/entity/dsc_explosion_zombie/dsc_explosion_zombie_eyes.png");

    private final ExplosionZombieRender render;

    public ExplosionZombieEyesLayer(ExplosionZombieRender render)
    {
        this.render = render;
    }

    @Override
    public void doRenderLayer(ExplosionZombieEntity entity, float limbSwing, float limbSwingAmount,
                              float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.render.bindTexture(EYES);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

        GlStateManager.depthMask(!entity.isInvisible());

        int i = 61680;

        int j = i % 65536;
        int k = i / 65536;

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);

        this.render.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        i = entity.getBrightnessForRender();

        j = i % 65536;
        k = i / 65536;

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);

        this.render.setLightmap(entity);

        GlStateManager.disableBlend();

        GlStateManager.depthMask(true);
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}
