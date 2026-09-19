package ua.archijk.templatelcmod.common;

import net.minecraftforge.common.MinecraftForge;
import ua.archijk.templatelcmod.crawl.event.CrawlCommonEvents;

public class CommonProxy {

    public void preInit() {
        MinecraftForge.EVENT_BUS.register(new CrawlCommonEvents());
    }

    public void init() {
        // NO-OP
    }
}
