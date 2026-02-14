package org.imesense.dynamicspawncontrol.mixins.srparasites;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import org.imesense.dynamicspawncontrol.core.mixinconfig.srparasites.SRParasitesBlacklistData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SRPEventHandlerBus.class, remap = false)
public abstract class MixinSRPEventHandlerBus
{
    @Inject(method = "writeCOTHTag", at = @At("HEAD"), remap = false)
    private void $writeCOTHTag(EntityLivingBase in, String mobname, NBTTagCompound tags, CallbackInfo ci)
    {
        if (in != null)
        {
            ResourceLocation entityKey = EntityList.getKey(in);

            if (entityKey != null)
            {
                String entityName = entityKey.toString();

                for (String blacklistedEntity : SRParasitesBlacklistData.getActiveBlacklist())
                {
                    if (entityName.equals(blacklistedEntity) || entityName.contains(blacklistedEntity))
                    {
                        tags.setInteger("srpcothimmunity", 0);
                        break;
                    }
                }
            }
        }
    }
}