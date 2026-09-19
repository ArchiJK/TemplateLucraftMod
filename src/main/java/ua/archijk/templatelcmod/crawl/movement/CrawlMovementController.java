package ua.archijk.templatelcmod.crawl.movement;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import ua.archijk.templatelcmod.crawl.capability.ICrawlData;
import ua.archijk.templatelcmod.crawl.config.CrawlConfig;
import ua.archijk.templatelcmod.crawl.network.CrawlNetworkHandler;
import ua.archijk.templatelcmod.crawl.util.CrawlCollisionUtil;
import ua.archijk.templatelcmod.crawl.util.CrawlConstants;

public final class CrawlMovementController {

    private CrawlMovementController() {
    }

    public static boolean tryEnable(EntityPlayerMP player, ICrawlData data) {
        SurfaceCandidate candidate = SurfaceDetector.findSurface(player, data.getSurface());
        if (candidate == null) {
            return false;
        }

        AxisAlignedBB crawlBox = CrawlCollisionUtil.buildPlayerBox(player, player.posX, player.posY, player.posZ, CrawlConstants.CRAWL_HEIGHT);
        if (!CrawlCollisionUtil.canOccupy(player, crawlBox)) {
            return false;
        }

        data.setCrawling(true);
        data.setPoseState(CrawlPoseState.LOWERING);
        data.setPreviousSurface(data.getSurface());
        data.setSurface(candidate.getSurface());
        data.setTransitionTicks(CrawlConstants.TRANSITION_TICKS);
        applyPlayerHeight(player, CrawlConstants.CRAWL_HEIGHT);
        CrawlNetworkHandler.sync(player);
        return true;
    }

    public static boolean disable(EntityPlayerMP player, ICrawlData data) {
        AxisAlignedBB standingBox = CrawlCollisionUtil.buildPlayerBox(player, player.posX, player.posY, player.posZ, CrawlConstants.PLAYER_HEIGHT);
        if (!CrawlCollisionUtil.canOccupy(player, standingBox)) {
            return false;
        }

        data.setCrawling(false);
        data.setPoseState(CrawlPoseState.RAISING);
        data.setTransitionTicks(CrawlConstants.TRANSITION_TICKS);
        data.setPreviousSurface(data.getSurface());
        data.setSurface(EnumFacing.DOWN);
        applyPlayerHeight(player, CrawlConstants.PLAYER_HEIGHT);
        player.setNoGravity(false);
        CrawlNetworkHandler.sync(player);
        return true;
    }

    public static void tick(EntityPlayerMP player, ICrawlData data) {
        if (!data.isCrawling()) {
            applyPlayerHeight(player, CrawlConstants.PLAYER_HEIGHT);
            if (data.getTransitionTicks() > 0) {
                data.setTransitionTicks(data.getTransitionTicks() - 1);
            } else {
                data.setPoseState(CrawlPoseState.STANDING);
            }
            return;
        }

        applyPlayerHeight(player, CrawlConstants.CRAWL_HEIGHT);
        SurfaceCandidate candidate = SurfaceDetector.findSurface(player, data.getSurface());
        if (candidate == null) {
            disable(player, data);
            return;
        }

        EnumFacing previous = data.getSurface();
        EnumFacing current = candidate.getSurface();
        if (previous != current) {
            data.setPreviousSurface(previous);
            data.setSurface(current);
            data.setPoseState(CrawlPoseState.TRANSITION);
            data.setTransitionTicks(CrawlConstants.TRANSITION_TICKS);
            CrawlNetworkHandler.sync(player);
        }

        applySurfaceAdhesion(player, data.getSurface());
        applySpeed(player);
        tryAutoClimb(player, data);
        tryAutoLower(player, data);

        if (data.getTransitionTicks() > 0) {
            data.setTransitionTicks(data.getTransitionTicks() - 1);
        } else if (data.getPoseState() != CrawlPoseState.CLIMBING && data.getPoseState() != CrawlPoseState.LOWERING) {
            data.setPoseState(CrawlPoseState.CRAWLING);
        }
    }

    private static void applySurfaceAdhesion(EntityPlayerMP player, EnumFacing surface) {
        player.fallDistance = 0.0F;
        if (surface == EnumFacing.DOWN) {
            player.setNoGravity(false);
            return;
        }
        player.setNoGravity(true);
        double adhesion = 0.08D;
        player.motionX += adhesion * surface.getFrontOffsetX();
        player.motionY += adhesion * surface.getFrontOffsetY();
        player.motionZ += adhesion * surface.getFrontOffsetZ();
    }

    private static void applySpeed(EntityPlayerMP player) {
        if (player.moveForward == 0.0F && player.moveStrafing == 0.0F) {
            return;
        }
        player.motionX *= CrawlConfig.crawlSpeed / 0.1D;
        player.motionZ *= CrawlConfig.crawlSpeed / 0.1D;
    }

    private static void tryAutoClimb(EntityPlayerMP player, ICrawlData data) {
        if (CrawlConfig.maxClimbHeight <= 0 || data.getSurface() != EnumFacing.DOWN || !player.collidedHorizontally) {
            return;
        }

        AxisAlignedBB current = player.getEntityBoundingBox();
        for (int step = 1; step <= CrawlConfig.maxClimbHeight; step++) {
            AxisAlignedBB target = current.offset(0.0D, step, 0.0D);
            if (!CrawlCollisionUtil.canOccupy(player, target)) {
                continue;
            }
            if (!CrawlCollisionUtil.canSweep(player, current, target, 4)) {
                continue;
            }
            player.move(MoverType.SELF, 0.0D, step, 0.0D);
            data.setPoseState(CrawlPoseState.CLIMBING);
            data.setTransitionTicks(CrawlConstants.TRANSITION_TICKS);
            CrawlNetworkHandler.sync(player);
            return;
        }
    }

    private static void tryAutoLower(EntityPlayerMP player, ICrawlData data) {
        if (data.getSurface() != EnumFacing.DOWN || player.collidedHorizontally || player.onGround) {
            return;
        }

        AxisAlignedBB current = player.getEntityBoundingBox();
        AxisAlignedBB target = current.offset(0.0D, -1.0D, 0.0D);
        if (!CrawlCollisionUtil.canOccupy(player, target)) {
            return;
        }
        if (!CrawlCollisionUtil.canSweep(player, current, target, 4)) {
            return;
        }

        player.move(MoverType.SELF, 0.0D, -1.0D, 0.0D);
        data.setPoseState(CrawlPoseState.LOWERING);
        data.setTransitionTicks(CrawlConstants.TRANSITION_TICKS);
        CrawlNetworkHandler.sync(player);
    }

    private static void applyPlayerHeight(EntityPlayerMP player, float height) {
        if (Math.abs(player.height - height) < 0.001F) {
            return;
        }
        AxisAlignedBB current = player.getEntityBoundingBox();
        player.height = height;
        AxisAlignedBB resized = new AxisAlignedBB(current.minX, current.minY, current.minZ, current.maxX, current.minY + height, current.maxZ);
        player.setEntityBoundingBox(resized);
    }
}
