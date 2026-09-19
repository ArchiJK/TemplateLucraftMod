package ua.archijk.templatelcmod.crawl.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ua.archijk.templatelcmod.crawl.capability.ICrawlData;
import ua.archijk.templatelcmod.crawl.util.CrawlCapabilityUtil;
import ua.archijk.templatelcmod.crawl.movement.CrawlMovementController;

public class PacketToggleCrawlHandler implements IMessageHandler<PacketToggleCrawl, IMessage> {

    @Override
    public IMessage onMessage(PacketToggleCrawl message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        player.getServerWorld().addScheduledTask(() -> handle(player, message));
        return null;
    }

    private static void handle(EntityPlayerMP player, PacketToggleCrawl message) {
        ICrawlData data = CrawlCapabilityUtil.get(player);
        if (data == null) {
            return;
        }

        boolean currentlyCrawling = data.isCrawling();
        boolean target = message.getDesiredState();
        if (currentlyCrawling == target) {
            return;
        }

        if (target) {
            if (!CrawlMovementController.tryEnable(player, data)) {
                player.sendStatusMessage(new TextComponentTranslation("crawlmod.message.cannot_enable"), true);
            } else {
                player.sendStatusMessage(new TextComponentTranslation("crawlmod.message.enabled"), true);
            }
        } else {
            if (CrawlMovementController.disable(player, data)) {
                player.sendStatusMessage(new TextComponentTranslation("crawlmod.message.disabled"), true);
            } else {
                player.sendStatusMessage(new TextComponentTranslation("crawlmod.message.cannot_disable"), true);
            }
        }
    }
}
