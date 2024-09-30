package org.imesense.dynamicspawncontrol.gameplay.events;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.gameplay.commands.cmdServerSingleScriptsReload;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnUpdateTorchLogic
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnUpdateTorchLogic()
    {
		CodeGenericUtils.printInitClassToLog(this.getClass());
		
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onHit(LivingHurtEvent event)
    {
        if ((event.getSource().getDamageType().equalsIgnoreCase("mob") ||
                event.getSource().getDamageType().equalsIgnoreCase("player")) && event.getSource().getTrueSource() != null)
        {
            Entity entity = event.getEntity();
            Entity entityGetTrueSource = event.getSource().getTrueSource();

            if (entityGetTrueSource instanceof EntityZombie)
            {
                EntityZombie zombie = (EntityZombie)entityGetTrueSource;

                if (Block.getBlockFromItem(zombie.getHeldItemMainhand().getItem()) instanceof BlockTorch)
                {
                    entity.setFire(5);
                }
            }
            else if (entityGetTrueSource instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer)entityGetTrueSource;

                if (Block.getBlockFromItem(player.getHeldItemMainhand().getItem()) instanceof BlockTorch)
                {
                    entity.setFire(5);
                }
            }
            else if (entityGetTrueSource instanceof EntitySkeleton)
            {
                EntitySkeleton skeleton = (EntitySkeleton)entityGetTrueSource;

                if (Block.getBlockFromItem(skeleton.getHeldItemMainhand().getItem()) instanceof BlockTorch)
                {
                    entity.setFire(5);
                }
            }
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onBreak(BlockEvent.BreakEvent event)
    {
        EntityPlayer player = event.getPlayer();

        if (Block.getBlockFromItem(player.getHeldItemMainhand().getItem()) instanceof BlockTorch)
        {
            BlockPos blockPos = event.getPos();
            Block block = event.getState().getBlock();

            if (block == Blocks.TNT)
            {
                event.setCanceled(true);
                Explosion ex = new Explosion(player.world, player, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 100.0F, true, true);
                event.getState().getBlock().onBlockExploded(player.world, blockPos, ex);

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
