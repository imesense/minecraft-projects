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

    public StoringScriptData.Equipment getConfigByPriority(List<StoringScriptData.Equipment> equipmentList, Random random)
    {
        int totalPriority = equipmentList.stream().mapToInt(config -> config.Priority).sum();
        int randomValue = random.nextInt(totalPriority);

        int cumulativePriority = 0;

        for (StoringScriptData.Equipment config : equipmentList)
        {
            cumulativePriority += config.Priority;

            if (randomValue < cumulativePriority)
            {
                return config;
            }
        }

        return equipmentList.get(equipmentList.size() - 1);
    }
}
