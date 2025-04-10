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
/* loaded from: input.jar:cad97/spawnercraft/items/ItemMobSpirit.class */
public class ItemMobSpirit extends ItemMobSoul {
    public ItemMobSpirit() {
        func_77655_b("mob_spirit");
        setRegistryName(SpawnerCraft.MOD_ID, "mob_spirit");
    }

    @Nonnull
    public EnumActionResult func_180614_a(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.func_184586_b(hand);
        ResourceLocation id = ItemMonsterPlacer.func_190908_h(stack);
        if (id == null || !EntityList.field_75627_a.containsKey(id)) {
            return EnumActionResult.FAIL;
        }
        if (world.field_72995_K) {
            return EnumActionResult.SUCCESS;
        }
        if (!player.func_175151_a(pos.func_177972_a(facing), facing, stack)) {
            return EnumActionResult.FAIL;
        }
        IBlockState state = world.func_180495_p(pos);
        Block block = state.func_177230_c();
        if (block == SpawnerCraftBlocks.MOB_CAGE) {
            world.func_175656_a(pos, Blocks.field_150474_ac.func_176223_P());
            TileEntityMobSpawner spawner = world.func_175625_s(pos);
            Objects.requireNonNull(spawner);
            MobSpawnerBaseLogic logic = spawner.func_145881_a();
            logic.func_190894_a(ItemMonsterPlacer.func_190908_h(stack));
            spawner.func_70296_d();
            world.func_184138_a(pos, state, state, 3);
            if (!player.field_71075_bZ.field_75098_d) {
                stack.func_190918_g(1);
            }
            return EnumActionResult.SUCCESS;
        } else if (block == Blocks.field_150474_ac) {
            return EnumActionResult.FAIL;
        } else {
            BlockPos pos2 = pos.func_177972_a(facing);
            double d0 = getYOffset(world, pos2);
            Entity entity = ItemMonsterPlacer.func_77840_a(world, ItemMonsterPlacer.func_190908_h(stack), pos2.func_177958_n() + 0.5d, pos2.func_177956_o() + d0, pos2.func_177952_p() + 0.5d);
            if (entity != null) {
                if ((entity instanceof EntityLivingBase) && stack.func_82837_s()) {
                    entity.func_96094_a(stack.func_82833_r());
                }
                ItemMonsterPlacer.func_185079_a(world, player, stack, entity);
                if (!player.field_71075_bZ.field_75098_d) {
                    stack.func_190918_g(1);
                }
            }
            return EnumActionResult.SUCCESS;
        }
    }

    private double getYOffset(World world, BlockPos pos) {
        AxisAlignedBB aabb = new AxisAlignedBB(pos).func_72321_a(0.0d, -1.0d, 0.0d);
        List<AxisAlignedBB> list = world.func_184144_a((Entity) null, aabb);
        if (list.isEmpty()) {
            return 0.0d;
        }
        double d0 = aabb.field_72338_b;
        for (AxisAlignedBB axisalignedbb1 : list) {
            d0 = Math.max(axisalignedbb1.field_72337_e, d0);
        }
        return d0 - pos.func_177956_o();
    }
}

