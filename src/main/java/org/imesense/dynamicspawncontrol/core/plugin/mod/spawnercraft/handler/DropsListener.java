package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.handler;

import net.minecraft.block.BlockMobSpawner;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init.SpawnerCraftBlocks;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init.SpawnerCraftItems;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items.ItemMobSoul;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
public final class DropsListener
{
    private static volatile DropsListener _INSTANCE;

    public static DropsListener getInstance()
    {
        return CodeGeneric.getInstance(DropsListener.class);
    }

    public DropsListener()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handleMobDrops(LivingDropsEvent event)
    {
        EntityPlayer getTrueSource = (EntityPlayer) event.getSource().getTrueSource();

        if (getTrueSource instanceof EntityPlayer)
        {
            ItemStack heldItem = getTrueSource.getHeldItemMainhand();
            if (heldItem.getItem() == SpawnerCraftItems.MOB_ROD || !ConfigHandler.dropsRequireFishing)
            {
                dropFor(event.getEntity());
            }
        }
    }

    private void dropFor(Entity entity)
    {
        ResourceLocation entityResource = EntityList.getKey(entity);

        if (entityResource == null)
        {
            return;
        }

        String entityString = entityResource.toString();

        if (ConfigHandler.eggMapping.containsKey(entityString))
        {
            entityString = ConfigHandler.eggMapping.get(entityString);
            entityResource = new ResourceLocation(entityString);
        }

        ItemStack stack = new ItemStack(SpawnerCraftItems.MOB_ESSENCE);

        if (EntityList.ENTITY_EGGS.containsKey(entityResource) && (ConfigHandler.mobEssenceToggleList.contains(entityString) ^
                ConfigHandler.isListBlacklist))
        {
            ItemMobSoul.applyEntityIdToItemStack(stack, entityResource);
            entity.entityDropItem(stack, 0.0f);
        }
    }

    public void handleBlockDrops(BlockEvent.HarvestDropsEvent event)
    {
        if ((event.getState().getBlock() instanceof BlockMobSpawner) && event.getHarvester() != null &&
                EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, event.getHarvester().getHeldItemMainhand()) >=
                        ConfigHandler.spawnerDropSilkLevel)
        {
            event.getDrops().add(new ItemStack(SpawnerCraftBlocks.MOB_CAGE));
        }
    }
}
