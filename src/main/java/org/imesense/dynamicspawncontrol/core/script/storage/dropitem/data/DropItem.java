package org.imesense.dynamicspawncontrol.core.script.storage.dropitem.data;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

public final class DropItem
{
    public static final class Data
    {
        public ResourceLocation entity;
        public List<ItemDrop> drops;

        public static final class ItemDrop
        {
            public ResourceLocation item;
            public Integer minAmount;
            public Integer maxAmount;
            public Float chance;
            public Event.Result result = Event.Result.DEFAULT;
        }
    }
}
