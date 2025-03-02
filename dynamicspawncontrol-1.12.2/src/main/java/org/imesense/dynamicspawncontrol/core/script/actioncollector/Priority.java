package org.imesense.dynamicspawncontrol.core.script.actioncollector;

import org.imesense.dynamicspawncontrol.core.script.storage.datadescription.ProfilePriority;

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

    public ProfilePriority.Data getConfigByPriority(List<ProfilePriority.Data> listProfilePriorityData, Random random)
    {
        int randomPriorityValue =
                random.nextInt(listProfilePriorityData.stream().mapToInt(config -> config.priority).sum()),
                cumulativePrioritySum = 0;

        for (ProfilePriority.Data randomData : listProfilePriorityData)
        {
            cumulativePrioritySum += randomData.priority;

            if (randomPriorityValue < cumulativePrioritySum)
            {
                return randomData;
            }
        }

        return listProfilePriorityData.get(listProfilePriorityData.size() - 1);
    }
}
