package org.imesense.dynamicspawncontrol.mixins.divinerpg.entity;

import divinerpg.objects.entities.entity.vethea.EntityShadahier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = EntityShadahier.class, remap = false)
public abstract class EntityShadahierRework extends EntityMob
{
    public EntityShadahierRework(World worldIn)
    {
        super(worldIn);
    }
}
