package org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data;

import net.minecraft.util.ResourceLocation;

import java.util.List;

public final class EntityDescription
{
    public EntityDescription()
    {

    }

    public static final class Data
    {
        public String name;
        public String profile;
        public Boolean isArcher;
        public List<ResourceLocation> entityTypes;
        public String description;
    }
}
