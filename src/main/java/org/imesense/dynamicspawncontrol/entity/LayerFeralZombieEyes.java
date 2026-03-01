package org.imesense.dynamicspawncontrol.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.entity.explosionzombie.EntityExplosionZombie;
import org.imesense.dynamicspawncontrol.entity.feralzombie.EntityFeralZombie;
import org.imesense.dynamicspawncontrol.entity.render.RenderFeralZombie;

public class LayerFeralZombieEyes implements LayerRenderer<EntityFeralZombie>
{
    private static final ResourceLocation EYES =
            new ResourceLocation("dynamicspawncontrol",
                    "textures/entity/dsc_feral_zombie_eyes.png");

    private final RenderFeralZombie render;

    public LayerFeralZombieEyes(RenderFeralZombie render)
    {
        this.render = render;
    }

    @Override
    public void doRenderLayer(EntityFeralZombie entity, float limbSwing, float limbSwingAmount,
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
