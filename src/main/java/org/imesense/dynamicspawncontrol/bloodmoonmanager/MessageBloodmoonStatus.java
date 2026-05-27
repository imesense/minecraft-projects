package org.imesense.dynamicspawncontrol.bloodmoonmanager;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageBloodmoonStatus implements IMessage {
    public boolean bloodmoonActive;

    public MessageBloodmoonStatus(boolean bloodMoon) {
        this.bloodmoonActive = bloodMoon;
    }

    public MessageBloodmoonStatus() {
    }

    public void fromBytes(ByteBuf buf) {
        this.bloodmoonActive = buf.readBoolean();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.bloodmoonActive);
    }

    public MessageBloodmoonStatus setStatus(boolean active) {
        this.bloodmoonActive = active;
        return this;
    }
}
