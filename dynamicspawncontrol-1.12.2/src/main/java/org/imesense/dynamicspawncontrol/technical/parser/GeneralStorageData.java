package org.imesense.dynamicspawncontrol.technical.parser;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

import java.util.List;

/**
 *
 */
public final class GeneralStorageData
{
    /**
     *
     */
    public static GeneralStorageData instance;

    /**
     *
     */
    public GeneralStorageData()
    {
		CodeGenericUtil.printInitClassToLog(this.getClass());
		
        instance = this;
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
