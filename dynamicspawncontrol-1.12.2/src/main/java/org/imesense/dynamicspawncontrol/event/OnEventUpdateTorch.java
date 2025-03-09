package org.imesense.dynamicspawncontrol.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventUpdateTorch
{
    private static boolean instanceExists = false;

    public OnEventUpdateTorch()
    {
		CodeGeneric.printInitClassToLog(this.getClass());
		
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    @SubscribeEvent
    public void onHit_0(LivingHurtEvent livingHurtEvent)
    {
        if ((livingHurtEvent.getSource().getDamageType().equalsIgnoreCase("mob") ||
                livingHurtEvent.getSource().getDamageType().equalsIgnoreCase("player")) && livingHurtEvent.getSource().getTrueSource() != null)
        {
            Entity entity = livingHurtEvent.getEntity();
            Entity entityGetTrueSource = livingHurtEvent.getSource().getTrueSource();

            if (entityGetTrueSource instanceof EntityZombie)
            {
                EntityZombie entityZombie = (EntityZombie)entityGetTrueSource;

                if (Block.getBlockFromItem(entityZombie.getHeldItemMainhand().getItem()) instanceof BlockTorch)
                {
                    entity.setFire(5);
                }
            }
            else if (entityGetTrueSource instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityPlayerMP = (EntityPlayerMP)entityGetTrueSource;

                if (Block.getBlockFromItem(entityPlayerMP.getHeldItemMainhand().getItem()) instanceof BlockTorch)
                {
                    entity.setFire(5);
                }
            }
            else if (entityGetTrueSource instanceof EntitySkeleton)
            {
                EntitySkeleton entitySkeleton = (EntitySkeleton)entityGetTrueSource;

                if (Block.getBlockFromItem(entitySkeleton.getHeldItemMainhand().getItem()) instanceof BlockTorch)
                {
                    entity.setFire(5);
                }
            }
        }
    }

    @SubscribeEvent
    public void onBreak_1(BlockEvent.BreakEvent breakEvent)
    {
        EntityPlayerMP player = (EntityPlayerMP) breakEvent.getPlayer();

        if (Block.getBlockFromItem(player.getHeldItemMainhand().getItem()) instanceof BlockTorch)
        {
            BlockPos blockPos = breakEvent.getPos();
            Block block = breakEvent.getState().getBlock();

            if (block == Blocks.TNT)
            {
                breakEvent.setCanceled(true);

                Explosion ex = new Explosion(player.world,
                        player, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 100.0F, true, true);

                breakEvent.getState().getBlock().onBlockExploded(player.world, blockPos, ex);

                if (!player.capabilities.isCreativeMode)
                {
                    player.getHeldItemMainhand().setCount(player.getHeldItemMainhand().getCount() - 1);
                    EntityItem itm = new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(Items.STICK));
                    player.world.spawnEntity(itm);
                }
            }
        }
    }
}
