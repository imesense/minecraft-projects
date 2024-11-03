package org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive;

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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnDropHeadMob
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnDropHeadMob()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    /**
     *
     * @param livingDeathEvent
     */
    @SubscribeEvent
    public synchronized void onEntityDeath(LivingDeathEvent livingDeathEvent)
    {
        if (livingDeathEvent.getSource().getTrueSource() instanceof EntityLivingBase)
        {
            Entity entity = livingDeathEvent.getEntity();
            EntityLivingBase entityLivingBase = (EntityLivingBase) livingDeathEvent.getSource().getTrueSource();

            if (entity instanceof EntitySkeleton ||
                    entity instanceof EntityZombie ||
                    entity instanceof EntityCreeper)
            {
                float dropChance = calculateDropChance(entityLivingBase);

                if (entityLivingBase.getRNG().nextFloat() < dropChance)
                {
                    dropHead((EntityLivingBase) entity, entityLivingBase.world);
                }
            }
        }
    }

    /**
     *
     * @param entityLivingBase
     * @return
     */
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

    /**
     *
     * @param entityLivingBase
     * @param world
     */
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
