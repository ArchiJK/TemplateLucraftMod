package ua.archijk.templatelcmod.crawl.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import ua.archijk.templatelcmod.crawl.capability.ICrawlData;
import ua.archijk.templatelcmod.crawl.network.CrawlNetworkHandler;
import ua.archijk.templatelcmod.crawl.network.PacketToggleCrawl;
import ua.archijk.templatelcmod.crawl.util.CrawlCapabilityUtil;

public class CrawlClientKeyHandler {

    private static final KeyBinding TOGGLE_CRAWL = new KeyBinding(
            "key.templatelucraftmod.toggle_crawl",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            Keyboard.KEY_G,
            "key.categories.templatelucraftmod"
    );

    public static void register() {
        ClientRegistry.registerKeyBinding(TOGGLE_CRAWL);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (net.minecraft.client.Minecraft.getMinecraft().player == null) {
            return;
        }
        while (TOGGLE_CRAWL.isPressed()) {
            ICrawlData data = CrawlCapabilityUtil.get(net.minecraft.client.Minecraft.getMinecraft().player);
            boolean desired = data == null || !data.isCrawling();
            CrawlNetworkHandler.CHANNEL.sendToServer(new PacketToggleCrawl(desired));
        }
    }
}
