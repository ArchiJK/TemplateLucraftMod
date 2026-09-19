package ua.archijk.templatelcmod.crawl.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ua.archijk.templatelcmod.crawl.capability.ICrawlData;
import ua.archijk.templatelcmod.crawl.client.CrawlClientStateApplier;
import ua.archijk.templatelcmod.crawl.util.CrawlCapabilityUtil;

public class PacketCrawlSyncHandler implements IMessageHandler<PacketCrawlSync, IMessage> {

    @Override
    public IMessage onMessage(PacketCrawlSync message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            if (Minecraft.getMinecraft().world == null) {
                return;
            }
            Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.getEntityId());
            if (entity == null) {
                return;
            }
            ICrawlData data = CrawlCapabilityUtil.get(entity);
            if (data == null) {
                return;
            }
            data.setCrawling(message.isCrawling());
            data.setPoseState(message.getPose());
            data.setPreviousSurface(message.getPreviousSurface());
            data.setSurface(message.getSurface());
            data.setTransitionTicks(message.getTransitionTicks());
            if (entity instanceof EntityPlayer) {
                CrawlClientStateApplier.apply((EntityPlayer) entity, data);
            }
        });
        return null;
    }
}
