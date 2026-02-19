package org.imesense.dynamicspawncontrol.mixins.srparasites.handler;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import org.imesense.dynamicspawncontrol.core.mixinconfig.srparasites.SRParasitesBlacklistData;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SRPEventHandlerBus.class, remap = false)
public abstract class SRPEventHandlerBusUpdate
{
    @Inject(method = "writeCOTHTag", at = @At("HEAD"), remap = false)
    private void $writeCOTHTag(EntityLivingBase in, String mobname, NBTTagCompound tags, CallbackInfo ci)
    {
        EarlyLogBuffer.log(Log.DEBUG, "SRPEventHandlerBusUpdate: writeCOTHTag called for mob: " + mobname);

        if (in != null)
        {
            ResourceLocation entityKey = EntityList.getKey(in);
            EarlyLogBuffer.log(Log.DEBUG, "  Entity class: " + in.getClass().getName());

            if (entityKey != null)
            {
                String entityName = entityKey.toString();
                EarlyLogBuffer.log(Log.DEBUG, "  Entity registry name: " + entityName);

                String[] blacklist = SRParasitesBlacklistData.getActiveBlacklist();
                EarlyLogBuffer.log(Log.DEBUG, "  Blacklist size: " + (blacklist != null ? blacklist.length : 0));

                if (blacklist != null)
                {
                    for (String blacklistedEntity : blacklist)
                    {
                        EarlyLogBuffer.log(Log.DEBUG, "    Checking against: " + blacklistedEntity);

                        if (entityName.equals(blacklistedEntity) || entityName.contains(blacklistedEntity))
                        {
                            EarlyLogBuffer.log(Log.DEBUG, "  MATCH FOUND! Setting immunity to 0 for: " + entityName);
                            tags.setInteger("srpcothimmunity", 0);

                            break;
                        }
                    }
                }
            }
            else
            {
                EarlyLogBuffer.log(Log.DEBUG, "  Entity registry name is NULL");
            }
        }
        else
        {
            EarlyLogBuffer.log(Log.DEBUG, "  Entity is NULL");
        }
    }
}