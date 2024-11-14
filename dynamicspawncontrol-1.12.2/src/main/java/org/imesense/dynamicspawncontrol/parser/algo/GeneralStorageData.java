package org.imesense.dynamicspawncontrol.parser.algo;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;

/**
 *
 */
public final class GeneralStorageData
{
    /**
     *
     */
    public static GeneralStorageData Instance;

    /**
     *
     */
    public GeneralStorageData()
    {
		CodeGeneric.printInitClassToLog(this.getClass());

        Instance = this;
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

        /**
         *
         */
        public boolean HasShield;
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
