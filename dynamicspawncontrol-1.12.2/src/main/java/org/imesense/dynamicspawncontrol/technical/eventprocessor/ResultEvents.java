package org.imesense.dynamicspawncontrol.technical.eventprocessor;

import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWord;
import org.imesense.dynamicspawncontrol.technical.customlibrary.SingleKeyWord;

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
        if (map.has(MultipleKeyWord.CommonKeyWorlds.ACTION_RESULT))
        {
            this.finalResult = map.get(MultipleKeyWord.CommonKeyWorlds.ACTION_RESULT);
        }
        if (SingleKeyWord.EVENT_RESULTS.SUPER.equals(this.finalResult))
        {
            this.result = Event.Result.DEFAULT;
        }
        else if (SingleKeyWord.EVENT_RESULTS.TRUE.equals(this.finalResult))
        {
            this.result = Event.Result.ALLOW;
        }
        else if (SingleKeyWord.EVENT_RESULTS.FALSE.equals(this.finalResult))
        {
            this.result = Event.Result.DENY;
        }

        return this.result;
    }
}
