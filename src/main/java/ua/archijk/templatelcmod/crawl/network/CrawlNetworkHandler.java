package ua.archijk.templatelcmod.crawl.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import ua.archijk.templatelcmod.TemplateLCMod;
import ua.archijk.templatelcmod.crawl.capability.ICrawlData;
import ua.archijk.templatelcmod.crawl.util.CrawlCapabilityUtil;

public final class CrawlNetworkHandler {

    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(TemplateLCMod.MODID + "_crawl");

    private CrawlNetworkHandler() {
    }

    public static void register() {
        CHANNEL.registerMessage(PacketToggleCrawlHandler.class, PacketToggleCrawl.class, 0, Side.SERVER);
        CHANNEL.registerMessage(PacketCrawlSyncHandler.class, PacketCrawlSync.class, 1, Side.CLIENT);
    }

    public static void sync(Entity entity) {
        ICrawlData data = CrawlCapabilityUtil.get(entity);
        if (data == null) {
            return;
        }

        PacketCrawlSync packet = new PacketCrawlSync(entity.getEntityId(), data.isCrawling(), data.getPoseState(), data.getSurface(), data.getPreviousSurface(), data.getTransitionTicks());
        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            CHANNEL.sendTo(packet, player);
            CHANNEL.sendToAllTracking(packet, player);
        }
    }

    public static void syncTo(Entity entity, EntityPlayerMP receiver) {
        ICrawlData data = CrawlCapabilityUtil.get(entity);
        if (data == null) {
            return;
        }
        PacketCrawlSync packet = new PacketCrawlSync(entity.getEntityId(), data.isCrawling(), data.getPoseState(), data.getSurface(), data.getPreviousSurface(), data.getTransitionTicks());
        CHANNEL.sendTo(packet, receiver);
    }
}
