package ua.archijk.templatelcmod.crawl.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Map;

public final class CrawlModelInstaller {

    private static boolean installed;

    private CrawlModelInstaller() {
    }

    public static void install() {
        if (installed || Minecraft.getMinecraft().getRenderManager() == null) {
            return;
        }

        Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
        for (Map.Entry<String, RenderPlayer> entry : skinMap.entrySet()) {
            boolean slim = "slim".equals(entry.getKey());
            ObfuscationReflectionHelper.setPrivateValue(RenderLivingBase.class, entry.getValue(), new CrawlModelPlayer(0.0F, slim), "mainModel", "field_77045_g");
        }

        installed = true;
    }
}
