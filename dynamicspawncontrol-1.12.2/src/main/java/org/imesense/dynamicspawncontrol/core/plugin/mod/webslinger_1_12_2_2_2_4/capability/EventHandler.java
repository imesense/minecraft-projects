package org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.config.DataWebSlinger;
import org.imesense.dynamicspawncontrol.core.plugin.mod.wumpleutil_1_12_2_2_12_9.util.container.SimpleCapabilityProvider;
import org.imesense.dynamicspawncontrol.core.plugin.mod.wumpleutil_1_12_2_2_12_9.util.adapter.EntityThingBase;
import org.imesense.dynamicspawncontrol.core.plugin.mod.wumpleutil_1_12_2_2_12_9.util.adapter.IThingBase;

public final class EventHandler
{
    private static volatile EventHandler _INSTANCE;

    public static EventHandler getInstance()
    {
        return CodeGeneric.getInstance(EventHandler.class);
    }

    public EventHandler()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    public void handleAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event)
    {
        Entity entity = event.getObject();

        int priority = getEntityPriority(entity);

        if (priority > 0)
        {
            event.addCapability(WebSlingerCapability.ID, new SimpleCapabilityProvider<IWebSlinger>
            (
                WebSlingerCapability.CAPABILITY,
                WebSlingerCapability.DEFAULT_FACING,
                WebSlingerCapability.CAPABILITY.getDefaultInstance())
            {
                private final int TASK_PRIORITY = priority;

                private final IThingBase OWNER = new EntityThingBase(entity);

                @Override
                public IWebSlinger getInstance()
                {
                    IWebSlinger iWebSlinger = super.getInstance();
                    iWebSlinger.checkInit(this.OWNER, this.TASK_PRIORITY);

                    return iWebSlinger;
                }
            });
        }
    }

    private static int getEntityPriority(Object object)
    {
        if (object instanceof EntityLivingBase)
        {
            ResourceLocation resourceLocation = EntityList.getKey((EntityLivingBase) object);

            if (resourceLocation != null)
            {
                int priority =
                        DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getEntityPriority(resourceLocation.toString());

                if (priority > 0)
                {
                    return priority;
                }
            }
        }

        return -1;
    }
}
