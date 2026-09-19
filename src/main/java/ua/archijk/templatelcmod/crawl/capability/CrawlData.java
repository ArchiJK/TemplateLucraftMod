package ua.archijk.templatelcmod.crawl.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import ua.archijk.templatelcmod.crawl.movement.CrawlPoseState;

public class CrawlData implements ICrawlData {

    private boolean crawling;
    private CrawlPoseState poseState = CrawlPoseState.STANDING;
    private EnumFacing surface = EnumFacing.DOWN;
    private EnumFacing previousSurface = EnumFacing.DOWN;
    private int transitionTicks;

    @Override
    public boolean isCrawling() {
        return this.crawling;
    }

    @Override
    public void setCrawling(boolean crawling) {
        this.crawling = crawling;
    }

    @Override
    public CrawlPoseState getPoseState() {
        return this.poseState;
    }

    @Override
    public void setPoseState(CrawlPoseState poseState) {
        this.poseState = poseState == null ? CrawlPoseState.STANDING : poseState;
    }

    @Override
    public EnumFacing getSurface() {
        return this.surface;
    }

    @Override
    public void setSurface(EnumFacing surface) {
        this.surface = surface == null ? EnumFacing.DOWN : surface;
    }

    @Override
    public EnumFacing getPreviousSurface() {
        return this.previousSurface;
    }

    @Override
    public void setPreviousSurface(EnumFacing surface) {
        this.previousSurface = surface == null ? EnumFacing.DOWN : surface;
    }

    @Override
    public int getTransitionTicks() {
        return this.transitionTicks;
    }

    @Override
    public void setTransitionTicks(int ticks) {
        this.transitionTicks = Math.max(0, ticks);
    }

    @Override
    public void copyFrom(ICrawlData other) {
        if (other == null) {
            return;
        }
        this.crawling = other.isCrawling();
        this.poseState = other.getPoseState();
        this.surface = other.getSurface();
        this.previousSurface = other.getPreviousSurface();
        this.transitionTicks = other.getTransitionTicks();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("Crawling", this.crawling);
        tag.setString("Pose", this.poseState.name());
        tag.setInteger("Surface", this.surface.getIndex());
        tag.setInteger("PreviousSurface", this.previousSurface.getIndex());
        tag.setInteger("TransitionTicks", this.transitionTicks);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.crawling = nbt.getBoolean("Crawling");
        this.poseState = parsePose(nbt.getString("Pose"));
        this.surface = EnumFacing.byIndex(nbt.getInteger("Surface"));
        this.previousSurface = EnumFacing.byIndex(nbt.getInteger("PreviousSurface"));
        this.transitionTicks = Math.max(0, nbt.getInteger("TransitionTicks"));
        if (this.surface == null) {
            this.surface = EnumFacing.DOWN;
        }
        if (this.previousSurface == null) {
            this.previousSurface = this.surface;
        }
    }

    private static CrawlPoseState parsePose(String pose) {
        try {
            return CrawlPoseState.valueOf(pose);
        } catch (IllegalArgumentException ignored) {
            return CrawlPoseState.STANDING;
        }
    }
}
