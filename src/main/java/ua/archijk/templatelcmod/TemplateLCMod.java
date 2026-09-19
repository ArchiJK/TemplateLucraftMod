package ua.archijk.templatelcmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import ua.archijk.templatelcmod.common.CommonProxy;
import ua.archijk.templatelcmod.crawl.capability.CrawlCapability;
import ua.archijk.templatelcmod.crawl.config.CrawlConfig;
import ua.archijk.templatelcmod.crawl.network.CrawlNetworkHandler;


@Mod(
        modid = TemplateLCMod.MODID,
        version = TemplateLCMod.VERSION,
        name = TemplateLCMod.NAME,
        dependencies = TemplateLCMod.DEPENDENCIES
)
public class TemplateLCMod {
    public static final String MODID = "templatelucraftmod";
    public static final String NAME = "Template LC Mod";
    public static final String VERSION = "1.0";
    public static final String DEPENDENCIES = "required-after:lucraftcore@[1.12.2-2.5.16,)";

    @SidedProxy(clientSide = "ua.archijk.templatelcmod.client.ClientProxy", serverSide = "ua.archijk.templatelcmod.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.init();
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        // NO-OP
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CrawlConfig.init(event.getSuggestedConfigurationFile());
        CrawlCapability.register();
        CrawlNetworkHandler.register();
        proxy.preInit();
    }



}
