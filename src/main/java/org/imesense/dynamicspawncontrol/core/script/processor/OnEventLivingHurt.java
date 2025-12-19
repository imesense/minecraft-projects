package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
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

        Log.write(0, "[ZombieDamage] "
                + "Original: " + originalDamage
                + " -> Reduced: " + reducedDamage);
    }
}
