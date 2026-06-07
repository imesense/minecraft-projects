package org.imesense.dynamicspawncontrol.entity.explosionzombie;

import lombok.NonNull;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;

public final class ExplosionZombieRender extends RenderLiving<ExplosionZombieEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("dynamicspawncontrol", "textures/entity/dsc_explosion_zombie/dsc_explosion_zombie.png");

    public ExplosionZombieRender(RenderManager renderManager)
    {
        super(renderManager, new ModelZombie(), 0.5F);

        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new ExplosionZombieEyesLayer(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(@NonNull ExplosionZombieEntity entityFeralZombie)
    {
        return TEXTURE;
    }
}
