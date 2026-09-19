package ua.archijk.templatelcmod.crawl.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import ua.archijk.templatelcmod.crawl.movement.CrawlPoseState;

public class PacketCrawlSync implements IMessage {

    private int entityId;
    private boolean crawling;
    private int pose;
    private int surface;
    private int previousSurface;
    private int transitionTicks;

    public PacketCrawlSync() {
    }

    public PacketCrawlSync(int entityId, boolean crawling, CrawlPoseState pose, EnumFacing surface, EnumFacing previousSurface, int transitionTicks) {
        this.entityId = entityId;
        this.crawling = crawling;
        this.pose = pose.ordinal();
        this.surface = surface.getIndex();
        this.previousSurface = previousSurface.getIndex();
        this.transitionTicks = transitionTicks;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.crawling = buf.readBoolean();
        this.pose = buf.readInt();
        this.surface = buf.readInt();
        this.previousSurface = buf.readInt();
        this.transitionTicks = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeBoolean(this.crawling);
        buf.writeInt(this.pose);
        buf.writeInt(this.surface);
        buf.writeInt(this.previousSurface);
        buf.writeInt(this.transitionTicks);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public boolean isCrawling() {
        return this.crawling;
    }

    public CrawlPoseState getPose() {
        CrawlPoseState[] values = CrawlPoseState.values();
        if (this.pose < 0 || this.pose >= values.length) {
            return CrawlPoseState.STANDING;
        }
        return values[this.pose];
    }

    public EnumFacing getSurface() {
        EnumFacing facing = EnumFacing.byIndex(this.surface);
        return facing == null ? EnumFacing.DOWN : facing;
    }

    public EnumFacing getPreviousSurface() {
        EnumFacing facing = EnumFacing.byIndex(this.previousSurface);
        return facing == null ? EnumFacing.DOWN : facing;
    }

    public int getTransitionTicks() {
        return Math.max(0, this.transitionTicks);
    }
}
