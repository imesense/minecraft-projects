package org.imesense.dynamicspawncontrol.core.script.actioncollector;

import org.imesense.dynamicspawncontrol.core.script.storage.StoringScriptData;

import java.util.List;
import java.util.Random;

public final class Priority
{
    private static volatile Priority _INSTANCE;

    public static Priority getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (Priority.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new Priority();
                }
            }
        }

        return _INSTANCE;
    }

    public StoringScriptData.RandomData getConfigByPriority(List<StoringScriptData.RandomData> index, Random random)
    {
        int randomPriorityValue =
                random.nextInt(index.stream().mapToInt(config -> config.Priority).sum()),
                cumulativePrioritySum = 0;

        for (StoringScriptData.RandomData randomData : index)
        {
            cumulativePrioritySum += randomData.Priority;

            if (randomPriorityValue < cumulativePrioritySum)
            {
                return randomData;
            }
        }

        return index.get(index.size() - 1);
    }
}
