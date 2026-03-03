package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
public final class DropHeadMob
{
    private static volatile DropHeadMob _INSTANCE;

    public static DropHeadMob getInstance()
    {
        return CodeGeneric.getInstance(DropHeadMob.class);
    }

    public DropHeadMob()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handleEntityDeath(LivingDeathEvent event)
    {
        EntityLivingBase victim = event.getEntityLiving();

        if (!(victim instanceof EntitySkeleton) &&
                !(victim instanceof EntityZombie) &&
                !(victim instanceof EntityCreeper))
        {
            return;
        }

        if (!(event.getSource().getTrueSource() instanceof EntityLivingBase))
        {
            return;
        }

        EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();

        ItemStack heldItem = attacker.getHeldItemMainhand();

        float dropChance = 0.0f;

        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemSword)
        {
            dropChance = calculateBaseDropChance(attacker);

            dropChance += calculateSmiteBonus(heldItem);

            dropChance += calculateCriticalBonus(attacker);

            dropChance *= getDifficultyMultiplier(attacker.world);

            dropChance = MathHelper.clamp(dropChance, 0.0f, 0.15f);
        }

        if (dropChance > 0.0f && attacker.getRNG().nextFloat() < dropChance)
        {
            dropHead(victim, attacker.world);

            Logger.write(0,
                    "[HeadDrop] " +
                            "Attacker=" + attacker.getName() +
                            " Weapon=" + (heldItem.isEmpty() ? "None" : heldItem.getItem().getRegistryName()) +
                            " Damage=" + String.format("%.1f", getAttackDamage(attacker)) +
                            " BaseChance=" + String.format("%.2f%%", getDamageBasedChance(getAttackDamage(attacker)) * 100) +
                            " SmiteBonus=" + String.format("%.2f%%", calculateSmiteBonus(heldItem) * 100) +
                            " CritBonus=" + String.format("%.2f%%", calculateCriticalBonus(attacker) * 100) +
                            " FinalChance=" + String.format("%.2f%%", dropChance * 100) +
                            " Victim=" + victim.getName()
            );
        }
    }

    private double getAttackDamage(EntityLivingBase attacker)
    {
        if (attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null)
        {
            return attacker
                    .getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
                    .getAttributeValue();
        }
        return 1.0D;
    }

    private float calculateBaseDropChance(EntityLivingBase attacker)
    {
        double attackDamage = getAttackDamage(attacker);

        float baseChance = getDamageBasedChance(attackDamage);

        ItemStack heldItem = attacker.getHeldItemMainhand();
        float durabilityFactor = 1.0f;

        if (!heldItem.isEmpty() && heldItem.isItemStackDamageable())
        {
            durabilityFactor =
                    1.0f - ((float) heldItem.getItemDamage() / heldItem.getMaxDamage());
            durabilityFactor = MathHelper.clamp(durabilityFactor, 0.5f, 1.0f); // Минимум 50% эффективности
            baseChance *= durabilityFactor;
        }

        return baseChance;
    }

    private float getDamageBasedChance(double attackDamage)
    {
        if (attackDamage < 5.0)
        {
            return 0.03f;
        }
        else if (attackDamage < 9.0)
        {
            return 0.04f;
        }
        else if (attackDamage < 15.0)
        {
            return 0.05f;
        }
        else if (attackDamage < 20.0)
        {
            return 0.055f;
        }
        else
        {
            return 0.06f;
        }
    }

    private float calculateSmiteBonus(ItemStack weapon)
    {
        if (weapon.isEmpty())
            return 0.0f;

        int smiteLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, weapon);

        return smiteLevel * 0.015f;
    }

    private float calculateCriticalBonus(EntityLivingBase attacker)
    {
        boolean isCritical =
                attacker.fallDistance > 0.0F &&
                        !attacker.onGround &&
                        !attacker.isInWater() &&
                        !attacker.isPotionActive(net.minecraft.init.MobEffects.BLINDNESS) &&
                        attacker.getRidingEntity() == null;

        if (isCritical)
        {
            return 0.02f;
        }
        return 0.0f;
    }

    private float getDifficultyMultiplier(World world)
    {
        switch (world.getDifficulty())
        {
            case PEACEFUL:
                return 0.0f;
            case EASY:
                return 0.75f;
            case NORMAL:
                return 1.0f;
            case HARD:
                return 1.25f;
            default:
                return 1.0f;
        }
    }

    private void dropHead(EntityLivingBase entityLivingBase, World world)
    {
        ItemStack itemStack = ItemStack.EMPTY;

        if (entityLivingBase instanceof EntitySkeleton)
        {
            itemStack = new ItemStack(Items.SKULL, 1, 0);
        }
        else if (entityLivingBase instanceof EntityZombie)
        {
            itemStack = new ItemStack(Items.SKULL, 1, 2);
        }
        else if (entityLivingBase instanceof EntityCreeper)
        {
            itemStack = new ItemStack(Items.SKULL, 1, 4);
        }

        if (!itemStack.isEmpty())
        {
            entityLivingBase.entityDropItem(itemStack, 0);
        }
    }
}