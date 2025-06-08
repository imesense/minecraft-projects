package org.imesense.dynamicspawncontrol.mixins.WirelessIndustry;

import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.wirelesstools.general.WorldLoadUnloadHandler;
import ru.wirelesstools.wnet.LinkedEnergySaveData;

@Mixin(WorldLoadUnloadHandler.class)
public abstract class WorldLoadUnloadHandlerMixin
{
    @Shadow(remap = false)
    private LinkedEnergySaveData data;

    @Inject(
            method = "onWorldUnload(Lnet/minecraftforge/event/world/WorldEvent$Unload;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void injectUnload(WorldEvent.Unload event, CallbackInfo callbackInfo)
    {
        if (!event.getWorld().isRemote)
        {
            MapStorage storage = event.getWorld().getMapStorage();
            this.data = (LinkedEnergySaveData) storage.getOrLoadData(LinkedEnergySaveData.class, "WI_savedata");

            if (this.data == null)
            {
                this.data = new LinkedEnergySaveData();
                storage.setData("WI_savedata", this.data);
            }

            this.data.markDirty();
        }

        callbackInfo.cancel();
    }
}
