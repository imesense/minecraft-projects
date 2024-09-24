package org.imesense.dynamicspawncontrol.technical.parsers;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.parsers.beta.ParserSingleZombieSummonAID;

import java.util.List;

/**
 *
 */
public final class GeneralStorageData
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

    /**
     *
     */
    public GeneralStorageData()
    {
        instance = this;

        CodeGenericUtils.printInitClassToLog(GeneralStorageData.class);
    }

    /**
     *
     */
    public List<String> EntitiesProhibitedOutdoors;

    /**
     *
     * @return
     */
    public List<String> getEntitiesProhibitedOutdoors()
    {
        return this.EntitiesProhibitedOutdoors;
    }

    /**
     *
     */
    public static class Equipment
    {
        /**
         *
         */
        public int Priority;

        /**
         *
         */
        public List<String> HeldItems;

        /**
         *
         */
        public List<String> Helmets;

        /**
         *
         */
        public List<String> ChestPlates;

        /**
         *
         */
        public List<String> Leggings;

        /**
         *
         */
        public List<String> Boots;
    }

    /**
     *
     */
    public List<Equipment> EquipmentConfigs;

    /**
     *
     * @return
     */
    public List<Equipment> getEquipmentConfigs()
    {
        return this.EquipmentConfigs;
    }
}
