package org.imesense.dynamicspawncontrol.entity.render;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.entity.feralzombie.EntityFeralZombie;

/**
 *
 */
public final class RenderFeralZombie extends RenderLiving<EntityFeralZombie>
{
    /**
     *
     */
    private static final ResourceLocation TEXTURE = new ResourceLocation("dynamicspawncontrol", "textures/entity/dsc_feral_zombie.png");

    /**
     *
     * @param renderManager
     */
    public RenderFeralZombie(RenderManager renderManager)
    {
        super(renderManager, new ModelZombie(), 0.5F);

        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
    }

    /**
     *
     * @param entityFeralZombie
     * @return
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityFeralZombie entityFeralZombie)
    {
        return TEXTURE;
    }
}
