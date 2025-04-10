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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/* loaded from: input.jar:cad97/spawnercraft/handler/DropsListener.class */
public class DropsListener {
    public static final DropsListener instance = new DropsListener();

    private DropsListener() {
    }

    @SubscribeEvent
    public void onMobDrops(LivingDropsEvent event) {
        EntityPlayer func_76346_g = event.getSource().func_76346_g();
        if (func_76346_g instanceof EntityPlayer) {
            ItemStack heldItem = func_76346_g.func_184614_ca();
            if (heldItem.func_77973_b() == SpawnerCraftItems.MOB_ROD || !ConfigHandler.dropsRequireFishing) {
                dropFor(event.getEntity());
            }
        }
    }

    private void dropFor(Entity entity) {
        ResourceLocation entityResource = EntityList.func_191301_a(entity);
        if (entityResource == null) {
            return;
        }
        String entityString = entityResource.toString();
        if (ConfigHandler.eggMapping.containsKey(entityString)) {
            entityString = ConfigHandler.eggMapping.get(entityString);
            entityResource = new ResourceLocation(entityString);
        }
        ItemStack stack = new ItemStack(SpawnerCraftItems.MOB_ESSENCE);
        if (EntityList.field_75627_a.containsKey(entityResource) && (ConfigHandler.mobEssenceToggleList.contains(entityString) ^ ConfigHandler.isListBlacklist)) {
            ItemMobSoul.applyEntityIdToItemStack(stack, entityResource);
            entity.func_70099_a(stack, 0.0f);
        }
    }

    @SubscribeEvent
    public void onBlockDrops(BlockEvent.HarvestDropsEvent event) {
        if ((event.getState().func_177230_c() instanceof BlockMobSpawner) && event.getHarvester() != null && EnchantmentHelper.func_77506_a(Enchantments.field_185306_r, event.getHarvester().func_184614_ca()) >= ConfigHandler.spawnerDropSilkLevel) {
            event.getDrops().add(new ItemStack(SpawnerCraftBlocks.MOB_CAGE));
        }
    }
}
