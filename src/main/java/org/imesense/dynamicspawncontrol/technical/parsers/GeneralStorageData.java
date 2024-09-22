package org.imesense.dynamicspawncontrol.technical.parsers;

import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.util.List;

/**
 *
 */
public class GeneralStorageData
{
    /**
     *
     */
    private static GeneralStorageData instance;

    /**
     *
     * @return
     */
    public static GeneralStorageData getInstance()
    {
        if (instance == null)
        {
            throw new IllegalStateException("Instance not initialized. Call the constructor first.");
        }

        return instance;
    }

    public GeneralStorageData(String nameClass)
    {
        Log.writeDataToLogFile(0, String.format("Create object [%s]", nameClass));
        instance = this;
    }

    public List<String> _entitiesProhibitedOutdoors;

    public List<String> getEntitiesProhibitedOutdoors()
    {
        return _entitiesProhibitedOutdoors;
    }

    public List<EquipmentConfig> _equipmentConfigs;

    public List<EquipmentConfig> getEquipmentConfigs()
    {
        return _equipmentConfigs;
    }

    public static class EquipmentConfig
    {
        public int _priority;

        public List<String> _heldItems;

        public List<String> _helmets;

        public List<String> _chestPlates;

        public List<String> _leggings;

        public List<String> _boots;
    }
}
