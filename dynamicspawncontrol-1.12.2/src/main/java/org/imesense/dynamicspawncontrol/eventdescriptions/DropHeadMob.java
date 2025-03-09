package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class DropHeadMob
{
    private static volatile DropHeadMob _INSTANCE;

    public static DropHeadMob getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (DropHeadMob.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new DropHeadMob();
                }
            }
        }

        return _INSTANCE;
    }

    public DropHeadMob()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    public void handleEntityDeath(LivingDeathEvent event)
    {
        if (event.getSource().getTrueSource() instanceof EntityLivingBase)
        {
            Entity entity = event.getEntity();
            EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();

            if (entity instanceof EntitySkeleton ||
                    entity instanceof EntityZombie ||
                    entity instanceof EntityCreeper)
            {
                float dropChance = calculateDropChance(attacker);

                if (attacker.getRNG().nextFloat() < dropChance)
                {
                    dropHead((EntityLivingBase) entity, attacker.world);
                }
            }
        }
    }

    private float calculateDropChance(EntityLivingBase entityLivingBase)
    {
        float baseChance = 0.0f;

        ItemStack heldItem = entityLivingBase.getHeldItemMainhand();

        if (heldItem.getItem() == Items.WOODEN_SWORD)
        {
            baseChance = 0.05f;
        }
        else if (heldItem.getItem() == Items.STONE_SWORD)
        {
            baseChance = 0.10f;
        }
        else if (heldItem.getItem() == Items.IRON_SWORD)
        {
            baseChance = 0.15f;
        }
        else if (heldItem.getItem() == Items.GOLDEN_SWORD)
        {
            baseChance = 0.20f;
        }
        else if (heldItem.getItem() == Items.DIAMOND_SWORD)
        {
            baseChance = 0.30f;
        }

        int smiteLevel = EnchantmentHelper.
                getEnchantmentLevel(Enchantments.SMITE, heldItem);

        baseChance += smiteLevel * 0.10f;

        return MathHelper.clamp(baseChance, 0.0f, 1.0f);
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
