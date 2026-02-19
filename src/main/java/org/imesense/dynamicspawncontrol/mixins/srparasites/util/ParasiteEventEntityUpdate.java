package org.imesense.dynamicspawncontrol.mixins.srparasites.util;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import org.imesense.dynamicspawncontrol.core.mixinconfig.srparasites.SRParasitesBlacklistData;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ParasiteEventEntity.class, remap = false)
public abstract class ParasiteEventEntityUpdate
{
    @Inject(
            method = "checkName",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void $checkName(String potentialElement, String[] blacklist, boolean isWhitelist, CallbackInfoReturnable<Boolean> cir)
    {
        EarlyLogBuffer.log(Log.DEBUG, "ParasiteEventEntityUpdate: checkName called");
        EarlyLogBuffer.log(Log.DEBUG, "  potentialElement: " + potentialElement);
        EarlyLogBuffer.log(Log.DEBUG, "  isWhitelist: " + isWhitelist);
        EarlyLogBuffer.log(Log.DEBUG, "  original blacklist size: " + (blacklist != null ? blacklist.length : 0));

        if (potentialElement == null)
        {
            EarlyLogBuffer.log(Log.DEBUG, "  potentialElement is NULL, returning");
            return;
        }

        String[] dscBlacklist = SRParasitesBlacklistData.getActiveBlacklist();
        EarlyLogBuffer.log(Log.DEBUG, "  DSC blacklist size: " + (dscBlacklist != null ? dscBlacklist.length : 0));

        if (dscBlacklist != null)
        {
            for (String blacklistedEntity : dscBlacklist)
            {
                EarlyLogBuffer.log(Log.DEBUG, "    Checking against: " + blacklistedEntity);

                if (potentialElement.equals(blacklistedEntity) || potentialElement.contains(blacklistedEntity))
                {
                    EarlyLogBuffer.log(Log.DEBUG, "  MATCH FOUND! Setting return to: " + (!isWhitelist));

                    cir.setReturnValue(!isWhitelist);
                    cir.cancel();

                    return;
                }
            }
        }

        EarlyLogBuffer.log(Log.DEBUG, "  No matches found, continuing to original method");
    }
}