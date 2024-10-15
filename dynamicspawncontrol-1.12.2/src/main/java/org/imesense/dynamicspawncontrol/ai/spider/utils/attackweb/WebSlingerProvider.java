package org.imesense.dynamicspawncontrol.ai.spider.utils.attackweb;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import org.imesense.dynamicspawncontrol.technical.customlibrary.SimpleCapabilityProvider;
import org.imesense.dynamicspawncontrol.technical.customlibrary.thing.IThing;

import javax.annotation.Nullable;

public class WebSlingerProvider extends SimpleCapabilityProvider<IWebSlinger>
{
    IThing owner = null;
    int taskPriority = -1;

    public WebSlingerProvider(Capability<IWebSlinger> capability, @Nullable EnumFacing facing, IThing ownerIn, int taskPriorityIn)
    {
        super(capability, facing, capability != null ? (IWebSlinger)capability.getDefaultInstance() : null);
        this.owner = ownerIn;
        this.taskPriority = taskPriorityIn;
    }

    public WebSlingerProvider(Capability<IWebSlinger> capability, @Nullable EnumFacing facing, IWebSlinger instance, IThing ownerIn, int taskPriorityIn)
    {
        super(capability, facing, instance);
        this.owner = ownerIn;
        this.taskPriority = taskPriorityIn;
    }

    public final IWebSlinger getInstance()
    {
        IWebSlinger cap = (IWebSlinger)super.getInstance();
        cap.checkInit(this.owner, this.taskPriority);
        return cap;
    }
}
