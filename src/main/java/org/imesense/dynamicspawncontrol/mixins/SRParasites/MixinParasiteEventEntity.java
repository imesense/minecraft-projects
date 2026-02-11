package org.imesense.dynamicspawncontrol.mixins.SRParasites;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import org.imesense.dynamicspawncontrol.core.mixinconfig.MixinConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ParasiteEventEntity.class, remap = false)
public class MixinParasiteEventEntity
{
    static
    {
        try
        {
            MixinConfigManager.loadConfig();
        }
        catch (Exception exception)
        {
            System.err.println("Failed to load mixin config: " + exception.getMessage());
        }
    }

    @Inject(
            method = "checkName",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void $checkName(String potentialElement, String[] blacklist, boolean isWhitelist, CallbackInfoReturnable<Boolean> cir
    )
    {
        if (!MixinConfigManager.isParasitesMixinEnabled())
        {
            return;
        }

        if (potentialElement == null)
        {
            return;
        }

        List<String> additionalBlacklist = MixinConfigManager.getParasitesBlacklist();

        for (String blacklistedEntity : additionalBlacklist)
        {
            if (potentialElement.contains(blacklistedEntity))
            {
                if (!isWhitelist)
                {
                    cir.setReturnValue(true);
                    cir.cancel();
                }

                return;
            }
        }
    }
}