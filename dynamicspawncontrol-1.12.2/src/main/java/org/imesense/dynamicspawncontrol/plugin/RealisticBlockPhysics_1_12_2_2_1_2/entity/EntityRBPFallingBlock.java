package org.imesense.dynamicspawncontrol.plugin.RealisticBlockPhysics_1_12_2_2_1_2.entity;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
//import xbigellx.rbp.RealisticBlockPhysics;
//import xbigellx.rbp.eventhandler.RBPFallingBlockEvent;
//import xbigellx.rbp.physics.BlockDefinition;
//import xbigellx.rbp.util.PhysicsUtil;

public class EntityRBPFallingBlock extends Entity implements IEntityAdditionalSpawnData {
    protected static final DataParameter<BlockPos> ORIGIN;
    private IBlockState fallTile;
    private NBTTagCompound tileEntityData;
    private int fallTime;
    private boolean setBlock = true;
    private double prevMotionY = 0.0D;
    private BlockDefinition physicsDef;
    private int entityDamageMax = 40;
    private double entityDamage;

    public EntityRBPFallingBlock(World worldIn) {
        super(worldIn);
    }

    public EntityRBPFallingBlock(World worldIn, double x, double y, double z, IBlockState blockState) {
        super(worldIn);
        this.physicsDef = RealisticBlockPhysics.getBlockDefinition(worldIn, blockState);
        if (this.physicsDef == null) {
            this.setDead();
        }

        this.entityDamage = this.physicsDef.getEntityDamage();
        this.fallTile = blockState;
        this.setSize(0.98F, 0.98F);
        this.setPosition(x, y + (double)((1.0F - this.height) / 2.0F), z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.setOrigin(new BlockPos(this));
        TileEntity tileEntity = worldIn.getTileEntity(this.getPosition());
        if (tileEntity != null) {
            this.tileEntityData = tileEntity.writeToNBT(new NBTTagCompound());
            if (blockState.getBlock() instanceof BlockContainer) {
                if (tileEntity instanceof IInventory) {
                    ((IInventory)tileEntity).clear();
                } else {
                    this.tileEntityData = null;
                }
            }
        }

    }

    public void onUpdate() {
        if (this.fallTile.getMaterial() == Material.AIR) {
            this.setDead();
        } else if (this.physicsDef == null && !this.world.isRemote) {
            this.setDead();
        } else {
            Block block = this.fallTile.getBlock();
            this.prevMotionY = this.motionY;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            BlockPos pos;
            if (this.fallTime++ == 0) {
                pos = new BlockPos(this);
                if (this.world.getBlockState(pos).getBlock() == block) {
                    this.world.setBlockToAir(pos);
                } else if (!this.world.isRemote) {
                    this.setDead();
                    return;
                }
            }

            if (!this.hasNoGravity()) {
                this.motionY -= 0.03999999910593033D;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            if (!this.world.isRemote) {
                pos = new BlockPos(this);
                if (!this.onGround) {
                    if (this.fallTime > 100 && !this.world.isRemote && (pos.getY() < 1 || pos.getY() > 256) || this.fallTime > 600) {
                        this.setDead();
                    }
                } else {
                    IBlockState blockState = this.world.getBlockState(pos);
                    this.motionX *= 0.699999988079071D;
                    this.motionY *= 0.699999988079071D;
                    this.motionZ *= -0.5D;
                    if (blockState.getBlock() != Blocks.PISTON_EXTENSION) {
                        if (this.setBlock) {
                            BlockPos placePos = !PhysicsUtil.canBlockPassThrough(this.world, pos) ? pos.up() : pos;
                            boolean shouldBreak = Math.abs(this.prevMotionY) >= this.physicsDef.getBreakVelocityMinOnLanded() && Math.random() < this.physicsDef.getBreakChanceOnLanded();
                            boolean shouldPlace;
                            if (!shouldBreak && RealisticBlockPhysics.getConfiguration().getProperty("main", "FallingBlockRelocation").getBoolean()) {
                                shouldPlace = true;

                                while(shouldPlace) {
                                    shouldPlace = false;
                                    if (!PhysicsUtil.canBlockPassThrough(this.world, placePos)) {
                                        placePos = placePos.add(0, 1, 0);
                                        shouldPlace = true;
                                    }
                                }
                            }

                            this.setPosition((double)placePos.getX(), (double)placePos.getY(), (double)placePos.getZ());
                            if (!(this.world.getBlockState(placePos).getBlock() instanceof BlockLiquid) && !(this.world.getBlockState(placePos).getBlock() instanceof BlockDynamicLiquid)) {
                                this.world.destroyBlock(placePos, PhysicsUtil.randomCrushedBlockDropItem());
                            }

                            shouldPlace = this.world.mayPlace(block, placePos, true, EnumFacing.UP, (Entity)null);
                            TileEntity tileEntity;
                            if (shouldPlace) {
                                if (RealisticBlockPhysics.getConfiguration().getProperty("main", "FallingBlockSounds").getBoolean()) {
                                    this.world.playSound((EntityPlayer)null, this.getPosition(), this.getBlock().getBlock().getSoundType(this.getBlock(), this.world, this.getPosition(), (Entity)null).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                                }

                                if (!shouldBreak) {
                                    BlockPos slideResult = PhysicsUtil.getRandomBlockSlidePos(this.world, this.getPosition(), this.physicsDef.getSlideChanceOnLanded());
                                    if (slideResult != null) {
                                        this.setPosition((double)slideResult.getX() + 0.5D, (double)slideResult.getY(), (double)slideResult.getZ() + 0.5D);
                                        MinecraftForge.EVENT_BUS.post(new RBPFallingBlockEvent.Slid(this, this.getPosition()));
                                        return;
                                    }
                                }

                                this.setDead();
                                this.world.setBlockState(placePos, this.fallTile);
                                if (block instanceof BlockFalling) {
                                    ((BlockFalling)block).onEndFalling(this.world, pos, this.fallTile, blockState);
                                }

                                if (this.tileEntityData != null && block.hasTileEntity(this.fallTile)) {
                                    tileEntity = this.world.getTileEntity(placePos);
                                    if (tileEntity != null) {
                                        NBTTagCompound nbttagcompound = tileEntity.writeToNBT(new NBTTagCompound());
                                        Iterator var9 = this.tileEntityData.getKeySet().iterator();

                                        while(var9.hasNext()) {
                                            String key = (String)var9.next();
                                            NBTBase nbtbase = this.tileEntityData.getTag(key);
                                            if (!"x".equals(key) && !"y".equals(key) && !"z".equals(key)) {
                                                nbttagcompound.setTag(key, nbtbase.copy());
                                            }
                                        }

                                        tileEntity.readFromNBT(nbttagcompound);
                                        tileEntity.markDirty();
                                    }
                                }

                                MinecraftForge.EVENT_BUS.post(new RBPFallingBlockEvent.Landed(this));
                                if (shouldBreak) {
                                    this.world.destroyBlock(this.getPosition(), PhysicsUtil.randomFallingBlockDropItem());
                                } else {
                                    boolean blocksCollide = PhysicsUtil.doBlocksCollide(this.world, this.getPosition(), this.getBlock(), this.getPosition().down(), this.world.getBlockState(this.getPosition().down()));
                                    if (!blocksCollide && this.getBlock().getCollisionBoundingBox(this.world, this.getPosition()) != null) {
                                        this.world.destroyBlock(this.getPosition(), PhysicsUtil.randomFallingBlockDropItem());
                                    }
                                }
                            } else {
                                this.setDead();
                                this.world.playEvent(2001, pos, Block.getStateId(this.getBlock()));
                                block.dropBlockAsItem(this.world, pos, this.getBlock(), 0);
                                if (this.tileEntityData != null && block.hasTileEntity(this.fallTile) && block instanceof ITileEntityProvider) {
                                    tileEntity = ((ITileEntityProvider)block).createNewTileEntity(this.world, 0);
                                    tileEntity.readFromNBT(this.tileEntityData);
                                    if (tileEntity instanceof IInventory) {
                                        InventoryHelper.dropInventoryItems(this.world, pos, (IInventory)tileEntity);
                                    }
                                }
                            }
                        } else {
                            this.setDead();
                        }
                    }
                }
            }

            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;
        }
    }

    public void fall(float distance, float damageMultiplier) {
        Block block = this.fallTile.getBlock();
        if (this.entityDamage > 0.0D) {
            int i = MathHelper.ceil(distance - 1.0F);
            if (i > 0) {
                List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
                boolean flag = block == Blocks.ANVIL;
                DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
                Iterator var8 = list.iterator();

                while(var8.hasNext()) {
                    Entity entity = (Entity)var8.next();
                    if (entity instanceof EntityLivingBase) {
                        entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor((double)((float)i) * this.entityDamage), this.entityDamageMax));
                    }
                }

                if (flag && (double)this.rand.nextFloat() < 0.05000000074505806D + (double)i * 0.05D) {
                    int j = (Integer)this.fallTile.getValue(BlockAnvil.DAMAGE);
                    ++j;
                    if (j > 2) {
                        this.setBlock = false;
                    } else {
                        this.fallTile = this.fallTile.withProperty(BlockAnvil.DAMAGE, j);
                    }
                }
            }
        }

    }

    public void writeSpawnData(ByteBuf buffer) {
        NBTTagCompound compound = new NBTTagCompound();
        this.writeEntityToNBT(compound);
        ByteBufUtils.writeTag(buffer, compound);
    }

    public void readSpawnData(ByteBuf additionalData) {
        NBTTagCompound compound = ByteBufUtils.readTag(additionalData);
        this.readEntityFromNBT(compound);
    }

    public void addEntityCrashInfo(CrashReportCategory category) {
        super.addEntityCrashInfo(category);
        if (this.fallTile != null) {
            Block block = this.fallTile.getBlock();
            category.addCrashSection("Immitating block ID", Block.getIdFromBlock(block));
            category.addCrashSection("Immitating block data", block.getMetaFromState(this.fallTile));
        }

    }

    protected void writeEntityToNBT(NBTTagCompound compound) {
        Block block = this.fallTile != null ? this.fallTile.getBlock() : Blocks.AIR;
        ResourceLocation resourcelocation = (ResourceLocation)Block.REGISTRY.getNameForObject(block);
        compound.setString("Block", resourcelocation == null ? "" : resourcelocation.toString());
        compound.setByte("Data", (byte)block.getMetaFromState(this.fallTile));
        compound.setInteger("Time", this.fallTime);
        compound.setDouble("EntityDamage", this.entityDamage);
        compound.setInteger("EntityDamageMax", this.entityDamageMax);
        if (this.tileEntityData != null) {
            compound.setTag("TileEntityData", this.tileEntityData);
        }

    }

    protected void readEntityFromNBT(NBTTagCompound compound) {
        int i = compound.getByte("Data") & 255;
        if (compound.hasKey("Block", 8)) {
            this.fallTile = Block.getBlockFromName(compound.getString("Block")).getStateFromMeta(i);
        } else if (compound.hasKey("TileID", 99)) {
            this.fallTile = Block.getBlockById(compound.getInteger("TileID")).getStateFromMeta(i);
        } else {
            this.fallTile = Block.getBlockById(compound.getByte("Tile") & 255).getStateFromMeta(i);
        }

        this.fallTime = compound.getInteger("Time");
        Block block = this.fallTile.getBlock();
        if (compound.hasKey("HurtEntities", 99)) {
            this.entityDamage = compound.getDouble("EntityDamage");
            this.entityDamageMax = compound.getInteger("EntityDamageMax");
        }

        if (compound.hasKey("TileEntityData", 10)) {
            this.tileEntityData = compound.getCompoundTag("TileEntityData");
        }

        if (block == null || block.getDefaultState().getMaterial() == Material.AIR) {
            this.fallTile = Blocks.SAND.getDefaultState();
        }

    }

    protected void entityInit() {
        this.preventEntitySpawning = true;
        this.dataManager.register(ORIGIN, BlockPos.ORIGIN);
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }

    public boolean canBeAttackedWithItem() {
        return false;
    }

    public boolean ignoreItemEntityData() {
        return true;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public World getWorldObj() {
        return this.world;
    }

    @Nullable
    public IBlockState getBlock() {
        return this.fallTile;
    }

    public void setOrigin(BlockPos pos) {
        this.dataManager.set(ORIGIN, pos);
    }

    @SideOnly(Side.CLIENT)
    public BlockPos getOrigin() {
        return (BlockPos)this.dataManager.get(ORIGIN);
    }

    static {
        ORIGIN = EntityDataManager.createKey(EntityFallingBlock.class, DataSerializers.BLOCK_POS);
    }
}