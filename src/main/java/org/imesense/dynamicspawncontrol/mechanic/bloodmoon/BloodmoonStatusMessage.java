package org.imesense.dynamicspawncontrol.mechanic.bloodmoon;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BloodmoonStatusMessage implements IMessage {
    public boolean bloodmoonActive;

    public BloodmoonStatusMessage(boolean bloodMoon) {
        this.bloodmoonActive = bloodMoon;
    }

    public BloodmoonStatusMessage() {
    }

    public void fromBytes(ByteBuf buf) {
        this.bloodmoonActive = buf.readBoolean();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.bloodmoonActive);
    }

    public BloodmoonStatusMessage setStatus(boolean active) {
        this.bloodmoonActive = active;
        return this;
    }
}
