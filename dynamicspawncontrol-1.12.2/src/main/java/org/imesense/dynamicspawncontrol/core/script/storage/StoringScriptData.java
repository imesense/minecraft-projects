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
        this.entityDescriptionsList = new ArrayList<>();
        this.potionList = new ArrayList<>();;
        this.randomDataList = new ArrayList<>();
        this.dataSupportList = new ArrayList<>();;
    }

    public static class EntityDescription
    {
        public String name;
        public String profile;
        public Boolean isArcher;
        public String entityType;
        public String description;
    }

    public static class RandomData
    {
        public Integer Priority;
    }

    public static class Equipment
    {
        public List<ItemData> HeldItems;
        public List<ItemData> Helmets;
        public List<ItemData> ChestPlates;
        public List<ItemData> Leggings;
        public List<ItemData> Boots;

        public Boolean HasShield;
        public Boolean seeSky;
        public String commandNbt;
        public Integer maxHeight;
        public Integer minHeight;

        public List<AbstractPotionEffect.Data> potions;
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
    public List<RandomData> randomDataList;
    public List<EntityDescription> entityDescriptionsList;
    public List<AbstractPotionEffect.Data> potionList;

    public List<DataSupport> dataSupportList;
}
