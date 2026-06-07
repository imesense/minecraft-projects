package org.imesense.dynamicspawncontrol.entity.feralzombie;

import lombok.NonNull;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;

public final class FeralZombieRender extends RenderLiving<FeralZombieEntity>
{
    private static final ResourceLocation[] TEXTURES =
    {
        new ResourceLocation("dynamicspawncontrol", "textures/entity/dsc_feral_zombie/dsc_feral_zombie_1.png"),
        new ResourceLocation("dynamicspawncontrol", "textures/entity/dsc_feral_zombie/dsc_feral_zombie_2.png")
    };

    public FeralZombieRender(RenderManager renderManager)
    {
        super(renderManager, new ModelZombie(), 0.5F);

        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new FeralZombieEyesLayer(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(@NonNull FeralZombieEntity entity)
    {
        long hash = entity.getUniqueID().getLeastSignificantBits();
        int index = Math.abs((int)(hash % TEXTURES.length));

        return TEXTURES[index];
    }
}
