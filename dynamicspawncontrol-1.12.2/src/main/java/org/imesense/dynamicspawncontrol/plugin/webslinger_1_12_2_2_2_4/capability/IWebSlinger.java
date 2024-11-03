package org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.capability;

import org.imesense.dynamicspawncontrol.plugin.wumpleutil_1_12_2_2_12_9.util.adapter.IThingBase;

/**
 *
 */
@FunctionalInterface
public interface IWebSlinger
{
    /**
     *
     * @param owner
     * @param priority
     */
    void checkInit(IThingBase owner, int priority);
}
