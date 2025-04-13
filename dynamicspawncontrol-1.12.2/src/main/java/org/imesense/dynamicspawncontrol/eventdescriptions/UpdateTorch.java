package org.imesense.dynamicspawncontrol.eventdescriptions;

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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
public final class UpdateTorch
{
    private static volatile UpdateTorch _INSTANCE;

    public static UpdateTorch getInstance()
    {
        return CodeGeneric.getInstance(UpdateTorch.class);
    }

    public UpdateTorch()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handleEntityHit(LivingHurtEvent event)
    {
        if ((event.getSource().getDamageType().equalsIgnoreCase("mob") ||
                event.getSource().getDamageType().equalsIgnoreCase("player")) && event.getSource().getTrueSource() != null)
        {
            Entity entity = event.getEntity();
            Entity attacker = event.getSource().getTrueSource();

            if (attacker instanceof EntityZombie)
            {
                handleZombieAttack((EntityZombie) attacker, entity);
            }
            else if (attacker instanceof EntityPlayerMP)
            {
                handlePlayerAttack((EntityPlayerMP) attacker, entity);
            }
            else if (attacker instanceof EntitySkeleton)
            {
                handleSkeletonAttack((EntitySkeleton) attacker, entity);
            }
        }
    }

    public void handleZombieAttack(EntityZombie entityZombie, Entity target)
    {
        if (Block.getBlockFromItem(entityZombie.getHeldItemMainhand().getItem()) instanceof BlockTorch)
        {
            target.setFire(5);
        }
    }

    public void handlePlayerAttack(EntityPlayerMP entityPlayerMP, Entity target)
    {
        if (Block.getBlockFromItem(entityPlayerMP.getHeldItemMainhand().getItem()) instanceof BlockTorch)
        {
            target.setFire(5);
        }
    }

    public void handleSkeletonAttack(EntitySkeleton entitySkeleton, Entity target)
    {
        if (Block.getBlockFromItem(entitySkeleton.getHeldItemMainhand().getItem()) instanceof BlockTorch)
        {
            target.setFire(5);
        }
    }

    public void handleBlockBreak(BlockEvent.BreakEvent event)
    {
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.getPlayer();

        if (Block.getBlockFromItem(entityPlayerMP.getHeldItemMainhand().getItem()) instanceof BlockTorch)
        {
            BlockPos blockPos = event.getPos();
            Block block = event.getState().getBlock();

            if (block == Blocks.TNT)
            {
                event.setCanceled(true);

                Explosion explosion = new Explosion(entityPlayerMP.world,
                        entityPlayerMP, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 100.0F, true, true);

                event.getState().getBlock().onBlockExploded(entityPlayerMP.world, blockPos, explosion);

                if (!entityPlayerMP.capabilities.isCreativeMode)
                {
                    entityPlayerMP.getHeldItemMainhand().setCount(entityPlayerMP.getHeldItemMainhand().getCount() - 1);

                    EntityItem stickItem = new EntityItem(entityPlayerMP.world, entityPlayerMP.posX,
                            entityPlayerMP.posY, entityPlayerMP.posZ, new ItemStack(Items.STICK));

                    entityPlayerMP.world.spawnEntity(stickItem);
                }
            }
        }
    }
}
