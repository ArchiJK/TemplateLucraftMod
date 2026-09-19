package ua.archijk.templatelcmod.crawl.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class CrawlCapability {

    @CapabilityInject(ICrawlData.class)
    public static final Capability<ICrawlData> CRAWL_DATA = null;

    private CrawlCapability() {
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(ICrawlData.class, new Capability.IStorage<ICrawlData>() {
            @Override
            public NBTBase writeNBT(Capability<ICrawlData> capability, ICrawlData instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<ICrawlData> capability, ICrawlData instance, EnumFacing side, NBTBase nbt) {
                instance.deserializeNBT((net.minecraft.nbt.NBTTagCompound) nbt);
            }
        }, CrawlData::new);
    }
}
