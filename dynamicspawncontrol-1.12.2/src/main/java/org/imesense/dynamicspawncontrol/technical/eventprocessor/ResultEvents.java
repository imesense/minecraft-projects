package org.imesense.dynamicspawncontrol.technical.eventprocessor;

import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords;
import org.imesense.dynamicspawncontrol.technical.customlibrary.SingleKeyWords;

/**
 *
 */
public final class ResultEvents
{
    /**
     *
     */
    private Object finalResult;

    /**
     *
     */
    private Event.Result result;

    /**
     *
     * @param map
     * @return
     */
    public Event.Result getResult(AttributeMap<?> map)
    {
        if (map.has(MultipleKeyWords.CommonKeyWorlds.ACTION_RESULT))
        {
            this.finalResult = map.get(MultipleKeyWords.CommonKeyWorlds.ACTION_RESULT);
        }
        if (SingleKeyWords.EVENT_RESULTS.SUPER.equals(this.finalResult))
        {
            this.result = Event.Result.DEFAULT;
        }
        else if (SingleKeyWords.EVENT_RESULTS.TRUE.equals(this.finalResult))
        {
            this.result = Event.Result.ALLOW;
        }
        else if (SingleKeyWords.EVENT_RESULTS.FALSE.equals(this.finalResult))
        {
            this.result = Event.Result.DENY;
        }

        return this.result;
    }
}
