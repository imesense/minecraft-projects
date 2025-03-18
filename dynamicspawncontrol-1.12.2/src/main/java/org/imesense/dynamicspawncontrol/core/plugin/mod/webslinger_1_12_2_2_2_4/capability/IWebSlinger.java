package org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.capability;

import org.imesense.dynamicspawncontrol.core.plugin.mod.wumpleutil_1_12_2_2_12_9.util.adapter.IThingBase;

@FunctionalInterface
public interface IWebSlinger
{
    void checkInit(IThingBase owner, int priority);
}
