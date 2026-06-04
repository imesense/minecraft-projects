package org.imesense.dynamicspawncontrol.mechanic.bloodmoon;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BloodmoonStatusHandler implements IMessageHandler<BloodmoonStatusMessage, IMessage> {
    public IMessage onMessage(final BloodmoonStatusMessage message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                BloodMoonClientHandler.INSTANCE.setBloodmoon(message.bloodmoonActive);
            }
        });
        return null;
    }
}
