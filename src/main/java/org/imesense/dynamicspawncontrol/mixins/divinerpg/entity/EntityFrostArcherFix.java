package org.imesense.dynamicspawncontrol.mixins.divinerpg.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import lombok.NonNull;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import divinerpg.enums.ArrowType;
import divinerpg.objects.entities.entity.EntityDivineMob;
import divinerpg.objects.entities.entity.iceika.EntityFrostArcher;
import divinerpg.objects.entities.entity.projectiles.EntityDivineArrow;
import divinerpg.registry.LootTableRegistry;

@Mixin(EntityFrostArcher.class)
@SuppressWarnings("UnusedMixin")
public abstract class EntityFrostArcherFix extends EntityDivineMob implements IRangedAttackMob
{
    @Unique
    private static final String ATTACKER_UUID_TAG = "AttackerUUID";

    @Unique
    private UUID $$attackerUUID;

    @Unique
    private EntityPlayer $$persistentAttacker;

    public EntityFrostArcherFix(World worldIn)
    {
        super(worldIn);
        this.setSize(0.6F, 2.0F);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
    }

    public float getEyeHeight()
    {
        return 1.725F;
    }

    @Override
    protected void initEntityAI()
    {
        // Базовые задачи
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackRanged(this, 0.27F, 2, 10.0F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 32.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));

        // Задача на получение урона - запоминаем обидчика
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false)
        {
            @Override
            public void startExecuting()
            {
                super.startExecuting();

                EntityLivingBase attacker = this.taskOwner.getRevengeTarget();

                if (attacker instanceof EntityPlayer)
                {
                    ((EntityFrostArcherFix) this.taskOwner).$$setPersistentAttacker((EntityPlayer) attacker);
                }
            }
        });

        // Задача поиска цели - сначала проверяем запомненного игрока
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 0, true, false, null)
        {
            @Override
            public boolean shouldExecute()
            {
                // Если есть запомненный игрок и он жив - атакуем его
                if ($$persistentAttacker != null && $$persistentAttacker.isEntityAlive())
                {
                    double distance = this.taskOwner.getDistanceSq($$persistentAttacker);

                    if (distance <= this.getTargetDistance() * this.getTargetDistance())
                    {
                        this.targetEntity = $$persistentAttacker;
                        return true;
                    }
                }

                // Иначе ищем ближайшего игрока
                return super.shouldExecute();
            }

            @Override
            protected double getTargetDistance()
            {
                return 20.0D; // Радиус поиска цели
            }
        });
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        // Восстанавливаем запомненного игрока из UUID при загрузке
        if (!this.world.isRemote && this.$$persistentAttacker == null && this.$$attackerUUID != null)
        {
            for (EntityPlayer player : this.world.playerEntities)
            {
                if (player.getUniqueID().equals(this.$$attackerUUID) && player.isEntityAlive())
                {
                    this.$$persistentAttacker = player;
                    this.setRevengeTarget(player);
                    this.setAttackTarget(player);
                    break;
                }
            }
        }

        // Очищаем запомненного игрока если он умер
        if (this.$$persistentAttacker != null && !this.$$persistentAttacker.isEntityAlive())
        {
            $$clearPersistentAttacker();
        }

        // Очищаем если игрок слишком далеко
        if (this.$$persistentAttacker != null && this.getAttackTarget() == null)
        {
            double distance = this.getDistanceSq(this.$$persistentAttacker);
            if (distance > 1024.0D) // 32 блока
            {
                $$clearPersistentAttacker();
            }
        }
    }

    @Override
    public void attackEntityWithRangedAttack(@NonNull EntityLivingBase target, float f)
    {
        // Атакуем только игроков
        if (!(target instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer targetPlayer = (EntityPlayer) target;

        // Если есть запомненный игрок, атакуем только его
        if ($$persistentAttacker != null && targetPlayer != $$persistentAttacker)
        {
            return;
        }

        // Если нет запомненного игрока, но этот игрок нас ударил - запоминаем его
        if ($$persistentAttacker == null && this.getRevengeTarget() == targetPlayer)
        {
            $$setPersistentAttacker(targetPlayer);
        }

        this.world.spawnEntity(new EntityDivineArrow(
                this.world,
                ArrowType.FROST_ARCHER_ARROW,
                this,
                targetPlayer,
                1.6F,
                12.0F
        ));
    }

    @Override
    public boolean attackEntityFrom(@NonNull DamageSource source, float amount)
    {
        // Запоминаем игрока который нанес урон
        if (source.getTrueSource() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) source.getTrueSource();

            if (!player.isCreative())
            {
                $$setPersistentAttacker(player);
            }
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void setAttackTarget(@Nullable EntityLivingBase target)
    {
        // Разрешаем атаковать только игроков
        if (target == null || target instanceof EntityPlayer)
        {
            super.setAttackTarget(target);
        }
    }

    @Override
    public void setRevengeTarget(@Nullable EntityLivingBase target)
    {
        // Разрешаем мстить только игрокам
        if (target == null || target instanceof EntityPlayer)
        {
            super.setRevengeTarget(target);
        }
    }

    @Override
    public void writeEntityToNBT(@NonNull NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (this.$$attackerUUID != null)
        {
            compound.setString(ATTACKER_UUID_TAG, this.$$attackerUUID.toString());
        }
    }

    @Override
    public void readEntityFromNBT(@NonNull NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey(ATTACKER_UUID_TAG))
        {
            this.$$attackerUUID = UUID.fromString(compound.getString(ATTACKER_UUID_TAG));
        }
    }

    @Unique
    public void $$setPersistentAttacker(EntityPlayer attacker)
    {
        if (attacker != null)
        {
            this.$$persistentAttacker = attacker;
            this.$$attackerUUID = attacker.getUniqueID();
            this.setRevengeTarget(attacker);
            this.setAttackTarget(attacker);
        }
    }

    @Unique
    private void $$clearPersistentAttacker()
    {
        this.$$persistentAttacker = null;
        this.$$attackerUUID = null;
        this.setRevengeTarget(null);
        this.setAttackTarget(null);
    }

    @NonNull
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEFINED;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(@NonNull DamageSource source)
    {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    protected ResourceLocation getLootTable()
    {
        return LootTableRegistry.ENTITIES_FROST_ARCHER;
    }

    // Used for `IRangedAttackMob`
    public void setSwingingArms(boolean swingingArms)
    {
    }
}
