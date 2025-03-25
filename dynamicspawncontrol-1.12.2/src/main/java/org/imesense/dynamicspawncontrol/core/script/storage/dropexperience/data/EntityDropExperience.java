package org.imesense.dynamicspawncontrol.core.script.storage.dropexperience.data;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public final class EntityDropExperience
{
    public static final class Data
    {
        public ResourceLocation entity;
        public Integer xp;
        public Float multi_xp = 1.0f;
        public Float adding_xp;
        public Boolean use_default_xp = false;
        public Long worldTimeIntervalMax;
        public Long worldTimeIntervalMin;
        public Event.Result result = Event.Result.DEFAULT;;

        public boolean isTimeValid(World world)
        {
            if (this.worldTimeIntervalMin == null || this.worldTimeIntervalMax == null)
            {
                return true;
            }

            long worldTime = world.getWorldTime() % 24000;

            return worldTime >= this.worldTimeIntervalMin && worldTime <= this.worldTimeIntervalMax;
        }

        public Boolean validate()
        {
            if (this.use_default_xp)
            {
                return this.multi_xp != null;
            }

            return true;
        }
    }
}
