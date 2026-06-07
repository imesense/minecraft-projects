package org.imesense.dynamicspawncontrol.entity.zombie;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static org.imesense.dynamicspawncontrol.entity.zombie.ZombieShieldConfig.*;

public final class ZombieShieldUtils
{
    public static ItemStack getShieldItem(EntityZombie zombie)
    {
        ItemStack mainHand = zombie.getHeldItem(EnumHand.MAIN_HAND);

        if (!mainHand.isEmpty() && mainHand.getItem() instanceof ItemShield)
        {
            return mainHand;
        }

        ItemStack offHand = zombie.getHeldItem(EnumHand.OFF_HAND);

        if (!offHand.isEmpty() && offHand.getItem() instanceof ItemShield)
        {
            return offHand;
        }

        return null;
    }

    public static void checkAndRemoveBrokenShield(EntityZombie zombie, ItemStack shield)
    {
        if (shield == null || shield.isEmpty()) return;

        if (shield.getItemDamage() >= shield.getMaxDamage())
        {
            if (zombie.getHeldItem(EnumHand.MAIN_HAND) == shield)
            {
                zombie.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
            }
            else if (zombie.getHeldItem(EnumHand.OFF_HAND) == shield)
            {
                zombie.setHeldItem(EnumHand.OFF_HAND, ItemStack.EMPTY);
            }

            zombie.world.playSound(null, zombie.posX, zombie.posY, zombie.posZ,
                    net.minecraft.init.SoundEvents.ENTITY_ITEM_BREAK,
                    SoundCategory.HOSTILE, 1.0F, 1.0F);

            NBTTagCompound dscData = ZombieShieldNbt.getDSCData(zombie);
            dscData.setFloat(ZombieShieldNbt.SHIELD_HEALTH, 0);
            dscData.setInteger(ZombieShieldNbt.SHIELD_COOLDOWN, SHIELD_BREAK_COOLDOWN);

            if (DESTROY_SHIELD_ON_BREAK)
            {
                shield.setCount(0);
            }
        }
    }

    public static void repairShieldIfNeeded(EntityZombie zombie, ItemStack shield, float shieldHealth)
    {
        if (shield == null || shield.isEmpty()) return;

        if (ALLOW_SHIELD_REPAIR && shieldHealth >= MAX_SHIELD_HEALTH * 0.9F)
        {
            int currentDamage = shield.getItemDamage();

            if (currentDamage > 0)
            {
                int repairAmount = Math.min(currentDamage, 10);
                shield.setItemDamage(currentDamage - repairAmount);
            }
        }
    }

    public static boolean isAttackFromFront(EntityZombie zombie, DamageSource source)
    {
        if (!REQUIRE_FRONT_ATTACK)
        {
            return true;
        }

        Entity attacker = source.getImmediateSource();

        if (attacker == null)
        {
            return true;
        }

        Vec3d zombieLook = zombie.getLookVec();
        Vec3d toAttacker = new Vec3d(attacker.posX - zombie.posX,
                0, attacker.posZ - zombie.posZ).normalize();

        double dot = zombieLook.dotProduct(toAttacker);
        boolean isFromFront = dot < 0;

        return isFromFront;
    }

    public static void knockbackAttacker(EntityZombie zombie, DamageSource source)
    {
        if (KNOCKBACK_STRENGTH <= 0)
        {
            return;
        }

        Entity attacker = source.getImmediateSource();

        if (attacker instanceof EntityLivingBase && attacker != zombie)
        {
            EntityLivingBase livingAttacker = (EntityLivingBase) attacker;

            livingAttacker.knockBack(zombie, KNOCKBACK_STRENGTH,
                    MathHelper.sin(livingAttacker.rotationYaw * 0.017453292f),
                    -MathHelper.cos(livingAttacker.rotationYaw * 0.017453292f));
        }
    }
}
