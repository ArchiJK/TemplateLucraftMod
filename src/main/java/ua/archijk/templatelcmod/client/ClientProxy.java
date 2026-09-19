package ua.archijk.templatelcmod.client;

import net.minecraftforge.common.MinecraftForge;
import ua.archijk.templatelcmod.common.CommonProxy;
import ua.archijk.templatelcmod.crawl.client.CrawlCameraHandler;
import ua.archijk.templatelcmod.crawl.client.CrawlClientKeyHandler;
import ua.archijk.templatelcmod.crawl.client.CrawlClientTickHandler;
import ua.archijk.templatelcmod.crawl.client.CrawlModelInstaller;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
        CrawlClientKeyHandler.register();
        MinecraftForge.EVENT_BUS.register(new CrawlClientKeyHandler());
        MinecraftForge.EVENT_BUS.register(new CrawlClientTickHandler());
        MinecraftForge.EVENT_BUS.register(new CrawlCameraHandler());
    }

    @Override
    public void init() {
        super.init();
        CrawlModelInstaller.install();
    }
}
