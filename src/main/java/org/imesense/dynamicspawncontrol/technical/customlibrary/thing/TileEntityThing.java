package org.imesense.dynamicspawncontrol.technical.customlibrary.thing;

import java.util.ArrayList;
import net.minecraft.tileentity.TileEntity;
//import org.imesense.dynamicspawncontrol.technical.config.spiderattackweb.MatchingConfig;

public final class TileEntityThing extends TileEntityThingBase implements IThing
{
   public TileEntityThing(TileEntity ownerIn) {
      super(ownerIn);
   }

  // public ArrayList<String> getNameKeys() {
  //    return MatchingConfig.getNameKeys(this.owner);
   //}
}
