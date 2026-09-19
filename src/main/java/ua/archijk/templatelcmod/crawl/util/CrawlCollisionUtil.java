package ua.archijk.templatelcmod.crawl.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

public final class CrawlCollisionUtil {

    private CrawlCollisionUtil() {
    }

    public static AxisAlignedBB buildPlayerBox(EntityPlayer player, double x, double y, double z, float height) {
        float halfWidth = player.width * 0.5F;
        return new AxisAlignedBB(x - halfWidth, y, z - halfWidth, x + halfWidth, y + height, z + halfWidth);
    }

    public static boolean canOccupy(Entity entity, AxisAlignedBB box) {
        return entity.world.getCollisionBoxes(entity, box).isEmpty();
    }

    public static boolean canSweep(Entity entity, AxisAlignedBB start, AxisAlignedBB end, int steps) {
        for (int i = 1; i <= steps; i++) {
            double progress = (double) i / (double) steps;
            AxisAlignedBB current = start.offset(
                    (end.minX - start.minX) * progress,
                    (end.minY - start.minY) * progress,
                    (end.minZ - start.minZ) * progress
            );
            if (!canOccupy(entity, current)) {
                return false;
            }
        }
        return true;
    }
}
