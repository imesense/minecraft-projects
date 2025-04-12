package org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data;

import java.util.List;

public final class EntityEquipment
{
    public EntityEquipment()
    {

    }

    public static final class Data
    {
        public List<ItemDescription.Data> heldItem;
        public List<ItemDescription.Data> helmet;
        public List<ItemDescription.Data> chestPlate;
        public List<ItemDescription.Data> legging;
        public List<ItemDescription.Data> boots;

        public Boolean hasShield;
    }
}
