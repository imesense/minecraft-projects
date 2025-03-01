package org.imesense.dynamicspawncontrol.core.script.storage;

import com.google.gson.JsonObject;
import lombok.Getter;
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
        this.DataSupports = new ArrayList<>();
    }

    public static class Equipment
    {
        public String profile;
        public String description;

        public String entityType;
        public int Priority;

        public List<ItemData> HeldItems;
        public List<ItemData> Helmets;
        public List<ItemData> ChestPlates;
        public List<ItemData> Leggings;
        public List<ItemData> Boots;
        public boolean HasShield;
        public boolean isArcher;
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

    @Getter
    public List<Equipment> EquipmentConfigs;

    @Getter
    public List<AbstractPotionEffect.Data> Potions;

    @Getter
    public List<DataSupport> DataSupports;
}
