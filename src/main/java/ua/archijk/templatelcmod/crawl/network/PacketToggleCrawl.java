package ua.archijk.templatelcmod.crawl.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketToggleCrawl implements IMessage {

    private boolean desiredState;

    public PacketToggleCrawl() {
    }

    public PacketToggleCrawl(boolean desiredState) {
        this.desiredState = desiredState;
    }

    public boolean getDesiredState() {
        return this.desiredState;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.desiredState = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.desiredState);
    }
}
