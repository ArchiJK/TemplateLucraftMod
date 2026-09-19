package ua.archijk.templatelcmod.crawl.util;

import net.minecraft.entity.Entity;
import ua.archijk.templatelcmod.crawl.capability.CrawlCapability;
import ua.archijk.templatelcmod.crawl.capability.ICrawlData;

import javax.annotation.Nullable;

public final class CrawlCapabilityUtil {

    private CrawlCapabilityUtil() {
    }

    @Nullable
    public static ICrawlData get(Entity entity) {
        if (entity == null || !entity.hasCapability(CrawlCapability.CRAWL_DATA, null)) {
            return null;
        }
        return entity.getCapability(CrawlCapability.CRAWL_DATA, null);
    }
}
