package ua.archijk.templatelcmod.client;

import net.minecraftforge.common.MinecraftForge;
import ua.archijk.templatelcmod.common.CommonProxy;
import ua.archijk.templatelcmod.crawl.client.CrawlCameraHandler;
import ua.archijk.templatelcmod.crawl.client.CrawlClientEvents;
import ua.archijk.templatelcmod.crawl.client.CrawlClientKeyHandler;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
        CrawlClientKeyHandler.register();
        MinecraftForge.EVENT_BUS.register(new CrawlClientKeyHandler());
        MinecraftForge.EVENT_BUS.register(new CrawlClientEvents());
        MinecraftForge.EVENT_BUS.register(new CrawlCameraHandler());
    }
}
