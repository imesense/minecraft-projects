package org.imesense.dynamicspawncontrol.core.script.storage;

import com.google.gson.JsonObject;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public final class StoringScriptData
{
    private static volatile StoringScriptData _INSTANCE;

    public static StoringScriptData getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (StoringScriptData.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new StoringScriptData();
                }
            }
        }

        return _INSTANCE;
    }

    public StoringScriptData()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.equipmentList = new ArrayList<>();;
        this.potionList = new ArrayList<>();;
        this.dataSupportList = new ArrayList<>();;
    }

    public static class Equipment
    {
        public String profile;
        public String description;

        public String entityType;
        public Integer Priority;

        public List<ItemData> HeldItems;
        public List<ItemData> Helmets;
        public List<ItemData> ChestPlates;
        public List<ItemData> Leggings;
        public List<ItemData> Boots;

        public Boolean HasShield;
        public Boolean isArcher;
        public Boolean seeSky;
        public String commandNbt;
        public Integer maxHeight;
        public Integer minHeight;

        public List<AbstractPotionEffect.Data> potions;
        public String name;
    }

    public static class DataSupport
    {
        public Boolean seeSky;
        public String entityType;
        public List<AbstractPotionEffect.Data> potions;
    }

    public static class ItemData
    {
        public String item;
        public JsonObject nbt;
    }

    public List<Equipment> equipmentList;
    public List<AbstractPotionEffect.Data> potionList;

    public List<DataSupport> dataSupportList;
}
