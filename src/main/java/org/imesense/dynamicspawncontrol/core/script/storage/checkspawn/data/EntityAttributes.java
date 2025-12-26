package org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data;

import java.util.List;

public final class EntityAttributes
{
    public EntityAttributes()
    {

    }

    public static final class Data
    {
        public String commandNbt;
        public List<PotionEffect.Data> potion;

        public Double potionChance;
        public Double commandNbtChance;
    }
}
