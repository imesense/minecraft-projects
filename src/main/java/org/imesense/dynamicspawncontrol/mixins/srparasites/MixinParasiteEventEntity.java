package org.imesense.dynamicspawncontrol.mixins.srparasites;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import org.imesense.dynamicspawncontrol.core.mixinconfig.srparasites.SRParasitesBlacklistData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ParasiteEventEntity.class, remap = false)
public abstract class MixinParasiteEventEntity
{
    @Inject(
            method = "checkName",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void $checkName(String potentialElement, String[] blacklist, boolean isWhitelist, CallbackInfoReturnable<Boolean> cir)
    {
        if (potentialElement == null)
        {
            return;
        }

        String[] dscBlacklist = SRParasitesBlacklistData.getActiveBlacklist();

        for (String blacklistedEntity : dscBlacklist)
        {
            if (potentialElement.equals(blacklistedEntity) || potentialElement.contains(blacklistedEntity))
            {
                cir.setReturnValue(!isWhitelist);
                cir.cancel();

                return;
            }
        }
    }
}