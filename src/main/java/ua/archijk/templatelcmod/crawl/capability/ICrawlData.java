package ua.archijk.templatelcmod.crawl.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import ua.archijk.templatelcmod.crawl.movement.CrawlPoseState;

public interface ICrawlData {

    boolean isCrawling();

    void setCrawling(boolean crawling);

    CrawlPoseState getPoseState();

    void setPoseState(CrawlPoseState poseState);

    EnumFacing getSurface();

    void setSurface(EnumFacing surface);

    EnumFacing getPreviousSurface();

    void setPreviousSurface(EnumFacing surface);

    int getTransitionTicks();

    void setTransitionTicks(int ticks);

    void copyFrom(ICrawlData other);

    NBTTagCompound serializeNBT();

    void deserializeNBT(NBTTagCompound nbt);
}
