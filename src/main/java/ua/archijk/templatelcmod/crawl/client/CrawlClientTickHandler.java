package ua.archijk.templatelcmod.crawl.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ua.archijk.templatelcmod.crawl.capability.ICrawlData;
import ua.archijk.templatelcmod.crawl.util.CrawlCapabilityUtil;

public class CrawlClientTickHandler {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || Minecraft.getMinecraft().world == null) {
            return;
        }

        for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
            ICrawlData data = CrawlCapabilityUtil.get(player);
            if (data == null) {
                continue;
            }
            CrawlClientStateApplier.apply(player, data);
        }
    }
}
