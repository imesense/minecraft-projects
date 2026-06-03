package org.imesense.dynamicspawncontrol.mechanic.bloodmoon;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandleBloodmoonStatus implements IMessageHandler<MessageBloodmoonStatus, IMessage> {
    public IMessage onMessage(final MessageBloodmoonStatus message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                ClientBloodmoonHandler.INSTANCE.setBloodmoon(message.bloodmoonActive);
            }
        });
        return null;
    }
}
