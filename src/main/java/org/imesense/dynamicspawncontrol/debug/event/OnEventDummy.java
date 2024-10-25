package org.imesense.dynamicspawncontrol.debug.event;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import scala.util.Random;

import java.util.HashSet;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventDummy
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnEventDummy()
    {
		CodeGenericUtil.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
            EntityLivingBase killer = (EntityLivingBase) event.getSource().getTrueSource();
            Entity entity = event.getEntity();

            if (entity instanceof EntitySkeleton || entity instanceof EntityZombie || entity instanceof EntityCreeper) {
                float dropChance = calculateDropChance(killer);

                if (killer.getRNG().nextFloat() < dropChance) {
                    dropHead((EntityLivingBase) entity, killer.world);
                }
            }
        }
    }

    private float calculateDropChance(EntityLivingBase killer) {
        ItemStack heldItem = killer.getHeldItemMainhand();
        float baseChance = 0.0f;

        if (heldItem.getItem() == Items.WOODEN_SWORD) {
            baseChance = 0.05f;
        } else if (heldItem.getItem() == Items.STONE_SWORD) {
            baseChance = 0.10f;
        } else if (heldItem.getItem() == Items.IRON_SWORD) {
            baseChance = 0.15f;
        } else if (heldItem.getItem() == Items.GOLDEN_SWORD) {
            baseChance = 0.20f;
        } else if (heldItem.getItem() == Items.DIAMOND_SWORD) {
            baseChance = 0.30f;
        }

        int smiteLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, heldItem);
        baseChance += smiteLevel * 0.10f;

        return MathHelper.clamp(baseChance, 0.0f, 1.0f);
    }

    private void dropHead(EntityLivingBase entity, World world) {
        ItemStack headItem = ItemStack.EMPTY;

        if (entity instanceof EntitySkeleton) {
            headItem = new ItemStack(Items.SKULL, 1, 0);
        } else if (entity instanceof EntityZombie) {
            headItem = new ItemStack(Items.SKULL, 1, 2);
        } else if (entity instanceof EntityCreeper) {
            headItem = new ItemStack(Items.SKULL, 1, 4);
        }
        
        if (!headItem.isEmpty()) {
            entity.entityDropItem(headItem, 0);
        }
    }
}
