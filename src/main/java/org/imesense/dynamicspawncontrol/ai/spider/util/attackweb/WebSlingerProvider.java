package org.imesense.dynamicspawncontrol.ai.spider.util.attackweb;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.SimpleCapabilityProvider;
import org.imesense.dynamicspawncontrol.technical.customlibrary.thing.IThingBase;

import javax.annotation.Nullable;

/**
 *
 */
public final class WebSlingerProvider extends SimpleCapabilityProvider<IWebSlinger>
{
    /**
     *
     */
    private final IThingBase OWNER;

    /**
     *
     */
    private final int TASK_PRIORITY;

    /**
     *
     * @param capability
     * @param facing
     * @param ownerIn
     * @param taskPriorityIn
     */
    public WebSlingerProvider(Capability<IWebSlinger> capability, @Nullable EnumFacing facing, IThingBase ownerIn, int taskPriorityIn)
    {
        super(capability, facing, capability != null ? capability.getDefaultInstance() : null);

        this.OWNER = ownerIn;
        this.TASK_PRIORITY = taskPriorityIn;
    }

    /**
     *
     * @return
     */
    public IWebSlinger getInstance()
    {
        final IWebSlinger CAP = super.getInstance();
        CAP.checkInit(this.OWNER, this.TASK_PRIORITY);

        return CAP;
    }
}
