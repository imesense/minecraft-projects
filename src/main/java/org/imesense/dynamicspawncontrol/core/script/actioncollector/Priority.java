package org.imesense.dynamicspawncontrol.core.script.actioncollector;

import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data.ProfilePriority;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;
import java.util.Random;

public final class Priority
{
    private static volatile Priority _INSTANCE;

    public static Priority getInstance()
    {
        return CodeGeneric.getInstance(Priority.class);
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
