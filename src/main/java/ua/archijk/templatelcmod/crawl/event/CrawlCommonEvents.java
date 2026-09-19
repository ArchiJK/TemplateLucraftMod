package ua.archijk.templatelcmod.crawl.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ua.archijk.templatelcmod.TemplateLCMod;
import ua.archijk.templatelcmod.crawl.capability.CrawlCapabilityProvider;
import ua.archijk.templatelcmod.crawl.capability.ICrawlData;
import ua.archijk.templatelcmod.crawl.movement.CrawlMovementController;
import ua.archijk.templatelcmod.crawl.network.CrawlNetworkHandler;
import ua.archijk.templatelcmod.crawl.util.CrawlCapabilityUtil;

public class CrawlCommonEvents {

    private static final ResourceLocation CRAWL_CAP = new ResourceLocation(TemplateLCMod.MODID, "crawl_data");

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(CRAWL_CAP, new CrawlCapabilityProvider());
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        ICrawlData original = CrawlCapabilityUtil.get(event.getOriginal());
        ICrawlData clone = CrawlCapabilityUtil.get(event.getEntityPlayer());
        if (original != null && clone != null) {
            clone.copyFrom(original);
        }
    }

    @SubscribeEvent
    public void onLogin(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            CrawlNetworkHandler.sync(event.player);
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerRespawnEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            CrawlNetworkHandler.sync(event.player);
        }
    }

    @SubscribeEvent
    public void onDimensionChange(PlayerChangedDimensionEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            CrawlNetworkHandler.sync(event.player);
        }
    }

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getEntityPlayer() instanceof EntityPlayerMP)) {
            return;
        }
        CrawlNetworkHandler.syncTo(event.getTarget(), (EntityPlayerMP) event.getEntityPlayer());
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient() || event.phase != TickEvent.Phase.END || !(event.player instanceof EntityPlayerMP)) {
            return;
        }

        ICrawlData data = CrawlCapabilityUtil.get(event.player);
        if (data == null) {
            return;
        }
        CrawlMovementController.tick((EntityPlayerMP) event.player, data);
    }
}
