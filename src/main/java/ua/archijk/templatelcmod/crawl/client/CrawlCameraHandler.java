package ua.archijk.templatelcmod.crawl.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.archijk.templatelcmod.crawl.capability.ICrawlData;
import ua.archijk.templatelcmod.crawl.util.CrawlCapabilityUtil;
import ua.archijk.templatelcmod.crawl.util.CrawlConstants;
import ua.archijk.templatelcmod.crawl.util.CrawlMathUtil;
import ua.archijk.templatelcmod.crawl.util.CrawlOrientationUtil;
import net.minecraft.util.EnumFacing;

public class CrawlCameraHandler {

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if (Minecraft.getMinecraft().player == null || event.getEntity() != Minecraft.getMinecraft().player) {
            return;
        }

        ICrawlData data = CrawlCapabilityUtil.get(Minecraft.getMinecraft().player);
        if (data == null || !data.isCrawling()) {
            event.setRoll(0.0F);
            return;
        }

        if (data.getTransitionTicks() > 0) {
            event.setRoll(0.0F);
            return;
        }

        if (data.getSurface() == EnumFacing.DOWN) {
            event.setRoll(0.0F);
            return;
        }

        float partialTicks = (float) event.getRenderPartialTicks();
        float blend = data.getTransitionTicks() > 0
                ? CrawlMathUtil.clamp01((CrawlConstants.TRANSITION_TICKS - data.getTransitionTicks() + partialTicks) / (float) CrawlConstants.TRANSITION_TICKS)
                : 1.0F;

        float fromRoll = CrawlOrientationUtil.getRollForSurface(data.getPreviousSurface());
        float toRoll = CrawlOrientationUtil.getRollForSurface(data.getSurface());
        event.setRoll(CrawlMathUtil.lerp(fromRoll, toRoll, blend));
    }
}
