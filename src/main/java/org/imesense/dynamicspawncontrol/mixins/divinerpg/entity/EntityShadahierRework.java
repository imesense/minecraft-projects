package org.imesense.dynamicspawncontrol.mixins.divinerpg.entity;

import divinerpg.config.Config; // не забыть
import divinerpg.objects.entities.entity.vethea.EntityShadahier;
import divinerpg.registry.LootTableRegistry;
import divinerpg.registry.SoundRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//TODO
@Mixin(value = EntityShadahier.class, remap = false)
public abstract class EntityShadahierRework extends EntityMob
{
    public EntityShadahierRework(World worldIn)
    {
        super(worldIn);
    }
}