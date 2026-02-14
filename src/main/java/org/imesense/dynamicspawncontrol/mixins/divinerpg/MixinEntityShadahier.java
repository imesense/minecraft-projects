package org.imesense.dynamicspawncontrol.mixins.divinerpg;

import divinerpg.objects.entities.entity.vethea.EntityShadahier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = EntityShadahier.class, remap = false)
public abstract class MixinEntityShadahier extends net.minecraft.entity.monster.EntityMob
{
    public MixinEntityShadahier(World worldIn)
    {
        super(worldIn);
    }
}
