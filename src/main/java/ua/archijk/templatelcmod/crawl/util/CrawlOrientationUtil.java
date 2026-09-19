package ua.archijk.templatelcmod.crawl.util;

import net.minecraft.util.EnumFacing;

public final class CrawlOrientationUtil {

    private CrawlOrientationUtil() {
    }

    public static float getPitchForSurface(EnumFacing surface) {
        switch (surface) {
            case UP:
                return 180.0F;
            case NORTH:
                return 90.0F;
            case SOUTH:
                return -90.0F;
            default:
                return 0.0F;
        }
    }

    public static float getRollForSurface(EnumFacing surface) {
        switch (surface) {
            case EAST:
                return 90.0F;
            case WEST:
                return -90.0F;
            default:
                return 0.0F;
        }
    }
}
