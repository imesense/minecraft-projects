package org.imesense.dynamicspawncontrol.bloodmoonmanager;


import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/* loaded from: Bloodmoon_MC1.12.2_1.5.3.jar:lumien/bloodmoon/network/handler/HandleBloodmoonStatus.class */
public class HandleBloodmoonStatus implements IMessageHandler<MessageBloodmoonStatus, IMessage> {
    public IMessage onMessage(final MessageBloodmoonStatus message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() { // from class: lumien.bloodmoon.network.handler.HandleBloodmoonStatus.1
            @Override // java.lang.Runnable
            public void run() {
                ClientBloodmoonHandler.INSTANCE.setBloodmoon(message.bloodmoonActive);
            }
        });
        return null;
    }
}
