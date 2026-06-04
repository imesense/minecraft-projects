package org.imesense.dynamicspawncontrol.mechanic.bloodmoon;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;

public final class BloodMoonSpawner {
    private static final int MOB_COUNT_DIV = (int) Math.pow(17.0d, 2.0d);
    private final Set<ChunkPos> eligibleChunksForSpawning = Sets.newHashSet();

    public int findChunksForSpawning(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate) throws IllegalAccessException, NoSuchMethodException, InstantiationException, SecurityException, IllegalArgumentException, InvocationTargetException {
        PlayerChunkMapEntry playermanager$playerinstance;
        if (!spawnHostileMobs && !spawnPeacefulMobs) {
            return 0;
        }
        this.eligibleChunksForSpawning.clear();
        int i = 0;
        for (EntityPlayer entityplayer : worldServerIn.playerEntities) {
            if (!entityplayer.isSpectator()) {
                int j = MathHelper.floor(entityplayer.posX / 16.0d);
                int k = MathHelper.floor(entityplayer.posZ / 16.0d);
                int i1 = -8;
                while (i1 <= 8) {
                    int j1 = -8;
                    while (j1 <= 8) {
                        boolean flag = i1 == (-8) || i1 == 8 || j1 == (-8) || j1 == 8;
                        ChunkPos chunkpos = new ChunkPos(i1 + j, j1 + k);
                        if (!this.eligibleChunksForSpawning.contains(chunkpos)) {
                            i++;
                            if (!flag && worldServerIn.getWorldBorder().contains(chunkpos) && (playermanager$playerinstance = worldServerIn.getPlayerChunkMap().getEntry(chunkpos.x, chunkpos.z)) != null && playermanager$playerinstance.isSentToPlayers()) {
                                this.eligibleChunksForSpawning.add(chunkpos);
                            }
                        }
                        j1++;
                    }
                    i1++;
                }
            }
        }
        int j4 = 0;
        BlockPos blockpos1 = worldServerIn.getSpawnPoint();
        for (EnumCreatureType enumcreaturetype : EnumCreatureType.values()) {
            if ((!enumcreaturetype.getPeacefulCreature() || spawnPeacefulMobs) && ((enumcreaturetype.getPeacefulCreature() || spawnHostileMobs) && (!enumcreaturetype.getAnimal() || spawnOnSetTickRate))) {
                int k4 = worldServerIn.countEntities(enumcreaturetype, true);
                int spawnLimit = (enumcreaturetype.getMaxNumberOfCreature() * i) / MOB_COUNT_DIV;
                if (k4 <= spawnLimit * BloodMoonConfig.getInstance(BloodMoonConfig.class).getSpawning().getSpawnLimitMultiplier()) {
                    ArrayList<ChunkPos> shuffled = Lists.newArrayList(this.eligibleChunksForSpawning);
                    Collections.shuffle(shuffled);
                    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                    Iterator<ChunkPos> it = shuffled.iterator();
                    while (it.hasNext()) {
                        ChunkPos chunkcoordintpair1 = it.next();
                        BlockPos blockpos = getRandomChunkPosition(worldServerIn, chunkcoordintpair1.x, chunkcoordintpair1.z);
                        int k1 = blockpos.getX();
                        int l1 = blockpos.getY();
                        int i2 = blockpos.getZ();
                        IBlockState iblockstate = worldServerIn.getBlockState(blockpos);
                        if (!iblockstate.isNormalCube()) {
                            int j2 = 0;
                            for (int k2 = 0; k2 < 3; k2++) {
                                int l2 = k1;
                                int i3 = l1;
                                int j3 = i2;
                                Biome.SpawnListEntry biomegenbase$spawnlistentry = null;
                                IEntityLivingData ientitylivingdata = null;
                                int l3 = MathHelper.ceil(Math.random() * 4.0d);
                                for (int i4 = 0; i4 < l3; i4++) {
                                    l2 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6);
                                    i3 += worldServerIn.rand.nextInt(1) - worldServerIn.rand.nextInt(1);
                                    j3 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6);
                                    blockpos$mutableblockpos.setPos(l2, i3, j3);
                                    float f = l2 + 0.5f;
                                    float f1 = j3 + 0.5f;
                                    if (worldServerIn.canBlockSeeSky(blockpos$mutableblockpos) && !worldServerIn.isAnyPlayerWithinRangeAt(f, i3, f1, BloodMoonConfig.getInstance(BloodMoonConfig.class).getSpawning().getSpawnRange()) && blockpos1.distanceSq(f, i3, f1) >= BloodMoonConfig.getInstance(BloodMoonConfig.class).getSpawning().getWorldSpawnDistance() * BloodMoonConfig.getInstance(BloodMoonConfig.class).getSpawning().getWorldSpawnDistance()) {
                                        if (biomegenbase$spawnlistentry == null) {
                                            biomegenbase$spawnlistentry = worldServerIn.getSpawnListEntryForTypeAt(enumcreaturetype, blockpos$mutableblockpos);
                                            if (biomegenbase$spawnlistentry == null || !BloodMoonSpawnValidator.canSpawn(biomegenbase$spawnlistentry.entityClass)) {
                                                break;
                                            }
                                        }
                                        if (worldServerIn.canCreatureTypeSpawnHere(enumcreaturetype, biomegenbase$spawnlistentry, blockpos$mutableblockpos) && canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(biomegenbase$spawnlistentry.entityClass), worldServerIn, blockpos$mutableblockpos)) {
                                            try {
                                                EntityLiving entityliving = (EntityLiving) biomegenbase$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldServerIn);
                                                entityliving.setLocationAndAngles(f, i3, f1, worldServerIn.rand.nextFloat() * 360.0f, 0.0f);
                                                Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(entityliving, worldServerIn, f, i3, f1, false);
                                                if (canSpawn == Event.Result.ALLOW || (canSpawn == Event.Result.DEFAULT && entityliving.getCanSpawnHere() && entityliving.isNotColliding())) {
                                                    if (!ForgeEventFactory.doSpecialSpawn(entityliving, worldServerIn, f, l3, f1)) {
                                                        ientitylivingdata = entityliving.onInitialSpawn(worldServerIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                                                    }
                                                    if (entityliving.isNotColliding()) {
                                                        j2++;
                                                        entityliving.getEntityData().setBoolean("bloodmoonSpawned", true);
                                                        worldServerIn.spawnEntity(entityliving);
                                                    } else {
                                                        entityliving.setDead();
                                                    }
                                                    if (i2 >= ForgeEventFactory.getMaxSpawnPackSize(entityliving)) {
                                                        break;
                                                    }
                                                }
                                                j4 += j2;
                                            } catch (Exception exception) {
                                                exception.printStackTrace();
                                                return j4;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        return j4;
    }

    protected static BlockPos getRandomChunkPosition(World worldIn, int x, int z) {
        Chunk chunk = worldIn.getChunkFromChunkCoords(x, z);
        int i = (x * 16) + worldIn.rand.nextInt(16);
        int j = (z * 16) + worldIn.rand.nextInt(16);
        int k = MathHelper.roundUp(chunk.getHeight(new BlockPos(i, 0, j)) + 1, 16);
        int l = worldIn.rand.nextInt(k > 0 ? k : (chunk.getTopFilledSegment() + 16) - 1);
        return new BlockPos(i, l, j);
    }

    public static boolean isValidEmptySpawnBlock(IBlockState p_185331_0_) {
        return (p_185331_0_.isBlockNormalCube() || p_185331_0_.canProvidePower() || p_185331_0_.getMaterial().isLiquid() || BlockRailBase.isRailBlock(p_185331_0_)) ? false : true;
    }

    public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType spawnPlacementTypeIn, World worldIn, BlockPos pos) {
        if (!worldIn.getWorldBorder().contains(pos)) {
            return false;
        }
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (spawnPlacementTypeIn == EntityLiving.SpawnPlacementType.IN_WATER) {
            return iblockstate.getMaterial().isLiquid() && worldIn.getBlockState(pos.down()).getMaterial().isLiquid() && !worldIn.getBlockState(pos.up()).isNormalCube();
        }
        BlockPos blockpos = pos.down();
        IBlockState state = worldIn.getBlockState(blockpos);
        if (!state.getBlock().canCreatureSpawn(state, worldIn, blockpos, spawnPlacementTypeIn)) {
            return false;
        }
        Block block = worldIn.getBlockState(blockpos).getBlock();
        boolean flag = (block == Blocks.BEDROCK || block == Blocks.BARRIER) ? false : true;
        return flag && isValidEmptySpawnBlock(iblockstate) && isValidEmptySpawnBlock(worldIn.getBlockState(pos.up()));
    }

    public static void performWorldGenSpawning(World worldIn, Biome biomeIn, int p_77191_2_, int p_77191_3_, int p_77191_4_, int p_77191_5_, Random randomIn) {
        int iNextInt;
        List<Biome.SpawnListEntry> list = biomeIn.getSpawnableList(EnumCreatureType.CREATURE);
        if (!list.isEmpty()) {
            while (randomIn.nextFloat() < biomeIn.getSpawningChance()) {
                Biome.SpawnListEntry biomegenbase$spawnlistentry = WeightedRandom.getRandomItem(worldIn.rand, list);
                int i = biomegenbase$spawnlistentry.minGroupCount + randomIn.nextInt((1 + biomegenbase$spawnlistentry.maxGroupCount) - biomegenbase$spawnlistentry.minGroupCount);
                IEntityLivingData ientitylivingdata = null;
                int j = p_77191_2_ + randomIn.nextInt(p_77191_4_);
                int k = p_77191_3_ + randomIn.nextInt(p_77191_5_);
                for (int j1 = 0; j1 < i; j1++) {
                    boolean flag = false;
                    for (int k1 = 0; !flag && k1 < 4; k1++) {
                        BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k));
                        if (canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos)) {
                            try {
                                EntityLiving entityliving = (EntityLiving) biomegenbase$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldIn);
                                entityliving.setLocationAndAngles(j + 0.5f, blockpos.getY(), k + 0.5f, randomIn.nextFloat() * 360.0f, 0.0f);
                                worldIn.spawnEntity(entityliving);
                                ientitylivingdata = entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                                flag = true;
                                j += randomIn.nextInt(5) - randomIn.nextInt(5);
                                iNextInt = k + (randomIn.nextInt(5) - randomIn.nextInt(5));
                                while (true) {
                                    k = iNextInt;
                                    if (j >= p_77191_2_ || j >= p_77191_2_ + p_77191_4_ || k < p_77191_3_ || k >= p_77191_3_ + p_77191_4_) {
                                        j = (j + randomIn.nextInt(5)) - randomIn.nextInt(5);
                                        iNextInt = (k + randomIn.nextInt(5)) - randomIn.nextInt(5);
                                    }
                                }
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        } else {
                            j += randomIn.nextInt(5) - randomIn.nextInt(5);
                            iNextInt = k + (randomIn.nextInt(5) - randomIn.nextInt(5));
                            while (true) {
                                k = iNextInt;
                                if (j >= p_77191_2_) {
                                }
                                j = (j + randomIn.nextInt(5)) - randomIn.nextInt(5);
                                iNextInt = (k + randomIn.nextInt(5)) - randomIn.nextInt(5);
                            }
                        }
                    }
                }
            }
        }
    }
}
