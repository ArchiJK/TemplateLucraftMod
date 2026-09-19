package ua.archijk.templatelcmod.crawl.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import ua.archijk.templatelcmod.crawl.capability.ICrawlData;
import ua.archijk.templatelcmod.crawl.util.CrawlConstants;

public final class CrawlClientStateApplier {

    private CrawlClientStateApplier() {
    }

    public static void apply(EntityPlayer player, ICrawlData data) {
        float targetHeight = data.isCrawling() ? CrawlConstants.CRAWL_HEIGHT : CrawlConstants.PLAYER_HEIGHT;
        if (Math.abs(player.height - targetHeight) > 0.001F) {
            AxisAlignedBB box = player.getEntityBoundingBox();
            player.height = targetHeight;
            player.setEntityBoundingBox(new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, box.minY + targetHeight, box.maxZ));
        }

        if (data.isCrawling()) {
            player.fallDistance = 0.0F;
        }
        player.setNoGravity(data.isCrawling() && data.getSurface() != EnumFacing.DOWN);
    }
}
