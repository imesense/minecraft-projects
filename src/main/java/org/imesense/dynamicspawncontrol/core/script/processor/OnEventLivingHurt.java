package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
@TODO(value = "Rework this event. Event does not meet the design standards and rework event zombieHasShield", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class OnEventLivingHurt
{
    private static volatile OnEventLivingHurt _INSTANCE;

    public static OnEventLivingHurt getInstance()
    {
        return CodeGeneric.getInstance(OnEventLivingHurt.class);
    }

    public OnEventLivingHurt()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    @SubscribeEvent
    public void handleUpdateLivingHurtEvent(LivingHurtEvent event)
    {
        if (!(event.getEntity() instanceof EntityPlayerMP)) return;

        DamageSource source = event.getSource();
        if (!(source.getTrueSource() instanceof EntityZombie)) return;

        float originalDamage = event.getAmount();
        float reducedDamage = 0.45F;

        event.setAmount(reducedDamage);

        Logger.info("[ZombieDamage] "
                + "Original: " + originalDamage
                + " -> Reduced: " + reducedDamage);
    }

    /*
    @SubscribeEvent
    public void zombieHasShield(LivingHurtEvent event)
    {
        if (event.getEntityLiving() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) event.getEntityLiving();

            // Проверяем щит
            ItemStack shield = zombie.getHeldItemMainhand();
            boolean hasShield = !shield.isEmpty() && shield.getItem() instanceof ItemShield;

            if (!hasShield)
            {
                shield = zombie.getHeldItemOffhand();
                hasShield = !shield.isEmpty() && shield.getItem() instanceof ItemShield;
            }

            if (hasShield && !event.getSource().isUnblockable())
            {
                // 50% шанс блокировать
                if (zombie.getRNG().nextFloat() < 0.5F)
                {
                    // Уменьшаем урон на 50%
                    event.setAmount(event.getAmount() * 0.5F);

                    // Повреждаем щит
                    int damage = 1 + (int)event.getAmount();
                    shield.damageItem(damage, zombie);

                    // Звук
                    zombie.world.playSound(null, zombie.posX, zombie.posY, zombie.posZ,
                            SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.HOSTILE,
                            1.0F, 0.8F + zombie.getRNG().nextFloat() * 0.4F);

                    // Отбрасывание атакующего
                    Entity attacker = event.getSource().getImmediateSource();

                    if (attacker instanceof EntityLivingBase)
                    {
                        ((EntityLivingBase) attacker).knockBack(zombie, 0.5F,
                                MathHelper.sin(attacker.rotationYaw * 0.017453292F),
                                -MathHelper.cos(attacker.rotationYaw * 0.017453292F));
                    }
                }
            }
        }
    }
     */
}
