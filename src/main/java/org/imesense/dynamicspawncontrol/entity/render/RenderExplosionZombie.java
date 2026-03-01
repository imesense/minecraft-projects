package org.imesense.dynamicspawncontrol.entity.render;

import lombok.NonNull;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerEndermanEyes;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.entity.LayerExplosionZombieEyes;
import org.imesense.dynamicspawncontrol.entity.explosionzombie.EntityExplosionZombie;
import org.imesense.dynamicspawncontrol.entity.feralzombie.EntityFeralZombie;

import javax.annotation.Nullable;

public final class RenderExplosionZombie extends RenderLiving<EntityExplosionZombie>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("dynamicspawncontrol", "textures/entity/dsc_explosion_zombie.png");

    public RenderExplosionZombie(RenderManager renderManager)
    {
        super(renderManager, new ModelZombie(), 0.5F);

        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerExplosionZombieEyes(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(@NonNull EntityExplosionZombie entityFeralZombie)
    {
        return TEXTURE;
    }
}
