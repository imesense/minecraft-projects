package org.imesense.dynamicspawncontrol.mixins.minecraftforge;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NetworkRegistry.class)
public abstract class NetworkRegistryFix
{
    @Redirect(
            method = {
                    "newChannel(Ljava/lang/String;[Lio/netty/channel/ChannelHandler;)Ljava/util/EnumMap;",
                    "newChannel(Lnet/minecraftforge/fml/common/ModContainer;Ljava/lang/String;[Lio/netty/channel/ChannelHandler;)Ljava/util/EnumMap;"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/fml/relauncher/Side;values()[Lnet/minecraftforge/fml/relauncher/Side;"
            ),
            remap = false
    )
    private Side[] removeBukkit()
    {
        return new Side[] {
                Side.CLIENT,
                Side.SERVER
        };
    }
}
