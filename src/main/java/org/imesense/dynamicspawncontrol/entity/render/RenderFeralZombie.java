package org.imesense.dynamicspawncontrol.entity.render;

import lombok.NonNull;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.entity.LayerExplosionZombieEyes;
import org.imesense.dynamicspawncontrol.entity.LayerFeralZombieEyes;
import org.imesense.dynamicspawncontrol.entity.feralzombie.EntityFeralZombie;

public final class RenderFeralZombie extends RenderLiving<EntityFeralZombie>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("dynamicspawncontrol", "textures/entity/dsc_feral_zombie.png");

    public RenderFeralZombie(RenderManager renderManager)
    {
        super(renderManager, new ModelZombie(), 0.5F);

        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerFeralZombieEyes(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(@NonNull EntityFeralZombie entityFeralZombie)
    {
        return TEXTURE;
    }
}
