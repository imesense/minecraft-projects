package org.imesense.dynamicspawncontrol.technical.customlibrary.thing;

import net.minecraft.entity.Entity;
//import org.imesense.dynamicspawncontrol.technical.config.spiderattackweb.MatchingConfig;

import java.util.ArrayList;

public class EntityThing extends EntityThingBase implements IThing
{
   public EntityThing(Entity entity)
   {
      super(entity);
   }

   //public ArrayList<String> getNameKeys() {
     // return MatchingConfig.getNameKeys(this.owner);
   //}
}
