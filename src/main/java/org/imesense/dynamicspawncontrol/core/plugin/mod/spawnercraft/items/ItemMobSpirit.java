package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init.SpawnerCraftBlocks;

/* loaded from: input.jar:cad97/spawnercraft/items/ItemMobSpirit.class */
public class ItemMobSpirit extends ItemMobSoul {
    public ItemMobSpirit() {
        setUnlocalizedName("mob_spirit");
        setRegistryName(SpawnerCraft.MOD_ID, "mob_spirit");
    }

    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        ResourceLocation id = ItemMonsterPlacer.getNamedIdFrom(stack);
        if (id == null || !EntityList.ENTITY_EGGS.containsKey(id)) {
            return EnumActionResult.FAIL;
        }
        if (world.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return EnumActionResult.FAIL;
        }
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == SpawnerCraftBlocks.MOB_CAGE) {
            world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState());
            TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getTileEntity(pos);
            Objects.requireNonNull(spawner);
            MobSpawnerBaseLogic logic = spawner.getSpawnerBaseLogic();
            logic.setEntityId(ItemMonsterPlacer.getNamedIdFrom(stack));
            spawner.markDirty();
            world.notifyBlockUpdate(pos, state, state, 3);
            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
            return EnumActionResult.SUCCESS;
        } else if (block == Blocks.MOB_SPAWNER) {
            return EnumActionResult.FAIL;
        } else {
            BlockPos pos2 = pos.offset(facing);
            double d0 = getYOffset(world, pos2);
            Entity entity = ItemMonsterPlacer.spawnCreature(world,
                    ItemMonsterPlacer.getNamedIdFrom(stack),
                    pos2.getX() + 0.5d, pos2.getY() + d0, pos2.getZ() + 0.5d);
            if (entity != null) {
                if ((entity instanceof EntityLivingBase) && stack.hasDisplayName()) {
                    entity.setCustomNameTag(stack.getDisplayName());
                }
                ItemMonsterPlacer.applyItemEntityDataToEntity(world, player, stack, entity);
                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }
            }
            return EnumActionResult.SUCCESS;
        }
    }

    private double getYOffset(World world, BlockPos pos) {
        AxisAlignedBB aabb = new AxisAlignedBB(pos).expand(0.0d, -1.0d, 0.0d);
        List<AxisAlignedBB> list = world.getCollisionBoxes((Entity) null, aabb);
        if (list.isEmpty()) {
            return 0.0d;
        }
        double d0 = aabb.minY;
        for (AxisAlignedBB axisalignedbb1 : list) {
            d0 = Math.max(axisalignedbb1.maxY, d0);
        }
        return d0 - pos.getY();
    }
}

