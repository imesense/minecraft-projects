package org.imesense.dynamicspawncontrol.entity.zombie;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class ZombieBaseEntity extends EntityZombie
{
    public enum ZombieType
    {
        NORMAL,
        FERAL,
        EXPLOSION
    }

    protected ZombieType zombieType = ZombieType.NORMAL;

    public ZombieBaseEntity(World world)
    {
        super(world);
    }

    public void setZombieType(ZombieType type)
    {
        this.zombieType = type;
        applyTypeAttributes();
    }

    public abstract ZombieType getZombieType();

    protected void applyTypeAttributes()
    {
        switch (zombieType)
        {
            case FERAL:
                applyFeralStats();
                break;

            case EXPLOSION:
                applyExplosionStats();
                break;

            case NORMAL:
                default:
                break;
        }
    }

    protected void applyFeralStats()
    {
        double health = 20.0D + this.rand.nextInt(21);

        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH)
                .setBaseValue(health);

        this.setHealth((float) health);

        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
                .setBaseValue(0.33D);

        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
                .setBaseValue(7.5D);
    }

    protected void applyExplosionStats()
    {
        double health = 20.0D + this.rand.nextInt(15);

        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH)
                .setBaseValue(health);

        this.setHealth((float) health);

        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
                .setBaseValue(0.25D);

        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
                .setBaseValue(4.5D);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setString("DSCZombieType", this.zombieType.name());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("DSCZombieType"))
        {
            this.zombieType = ZombieType.valueOf(compound.getString("DSCZombieType"));
            applyTypeAttributes();
        }
    }
}
