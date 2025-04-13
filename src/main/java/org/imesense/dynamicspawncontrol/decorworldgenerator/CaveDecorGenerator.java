package org.imesense.dynamicspawncontrol.decorworldgenerator;

import net.minecraft.block.BlockSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.Random;

@InitLog
public final class CaveDecorGenerator implements IWorldGenerator
{
    public CaveDecorGenerator()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
                         IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider.getDimension() == 0)
        {
            generateDecorations(world, random, chunkX * 16, chunkZ * 16);
        }
    }

    private void generateDecorations(World world, Random random, int x, int z)
    {
        for (int i = 0; i < 16; i++)
        {
            int randX = x + random.nextInt(16);
            int randZ = z + random.nextInt(16);
            int randY = random.nextInt(40);
            randY = 40 - randY;

            BlockPos pos = new BlockPos(randX, randY, randZ);
            BlockPos groundPos = pos.down();

            if (world.isAirBlock(pos) && random.nextFloat() < 0.2f)
            {
                if (hasAnyNeighbor(world, pos))
                {
                    placeWeb(world, pos);
                    continue;
                }
            }

            if (world.isAirBlock(pos) && world.getBlockState(groundPos).isSideSolid(world, groundPos, EnumFacing.UP))
            {
                if (random.nextFloat() < 0.15f)
                {
                    placeMobHead(world, pos, random);
                }
            }
        }
    }

    private boolean hasAnyNeighbor(World world, BlockPos blockPos)
    {
        for (EnumFacing side : EnumFacing.values())
        {
            if (!world.isAirBlock(blockPos.offset(side)))
            {
                return true;
            }
        }

        return false;
    }

    private void placeWeb(World world, BlockPos blockPos)
    {
        world.setBlockState(blockPos, Blocks.WEB.getDefaultState(), 2);
    }

    private void placeMobHead(World world, BlockPos blockPos, Random random)
    {
        ItemStack skull = getRandomMobHead(random, blockPos.getY());

        world.setBlockState(blockPos, Blocks.SKULL.getDefaultState()
                .withProperty(BlockSkull.FACING, EnumFacing.UP), 2);

        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (tileEntity instanceof TileEntitySkull)
        {
            TileEntitySkull skullTile = (TileEntitySkull) tileEntity;
            skullTile.setType(skull.getMetadata());

            int rotation = random.nextInt(16);
            skullTile.setSkullRotation(rotation);
        }
    }

    private ItemStack getRandomMobHead(Random random, int y)
    {
        if (y > 35)
        {
            int type = random.nextInt(5);

            switch (type)
            {
                case 0: return new ItemStack(Items.SKULL, 1, 0); //-' Skeleton
                case 1: return new ItemStack(Items.SKULL, 1, 2); //-' Zombie
                case 2: return new ItemStack(Items.SKULL, 1, 4); //-' Creeper
                case 3: return new ItemStack(Items.SKULL, 1, 3); //-' Player
                default: return new ItemStack(Items.SKULL, 1, 1); //-' Wither Skeleton
            }
        }
        else
        {
            int type = random.nextInt(3);

            switch (type)
            {
                case 0: return new ItemStack(Items.SKULL, 1, 0); //-' Skeleton
                case 1: return new ItemStack(Items.SKULL, 1, 1); //-' Wither Skeleton
                default: return new ItemStack(Items.SKULL, 1, 3); //-' Player
            }
        }
    }
}