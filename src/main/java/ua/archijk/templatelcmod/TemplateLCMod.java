package ua.archijk.templatelcmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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

    @EventHandler
    public void load(FMLInitializationEvent event) {

    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        // NO-OP
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {



    }



}
