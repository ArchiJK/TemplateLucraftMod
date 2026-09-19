package ua.archijk.templatelcmod.crawl.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrawlCapabilityProvider implements ICapabilitySerializable<NBTTagCompound> {

    private final ICrawlData data = new CrawlData();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CrawlCapability.CRAWL_DATA;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CrawlCapability.CRAWL_DATA) {
            return CrawlCapability.CRAWL_DATA.cast(this.data);
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return this.data.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.data.deserializeNBT(nbt);
    }
}
