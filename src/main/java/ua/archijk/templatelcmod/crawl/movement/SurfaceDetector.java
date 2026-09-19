package ua.archijk.templatelcmod.crawl.movement;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ua.archijk.templatelcmod.crawl.config.CrawlConfig;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class SurfaceDetector {

    private SurfaceDetector() {
    }

    @Nullable
    public static SurfaceCandidate findSurface(EntityPlayer player, EnumFacing preferred) {
        List<EnumFacing> order = buildSearchOrder(preferred);
        for (EnumFacing side : order) {
            if (!isSideAllowed(side)) {
                continue;
            }
            if (hasAttachableSurface(player.world, player.getEntityBoundingBox(), side)) {
                return new SurfaceCandidate(side);
            }
        }
        return null;
    }

    private static List<EnumFacing> buildSearchOrder(EnumFacing preferred) {
        List<EnumFacing> order = new ArrayList<>();
        if (preferred != null) {
            order.add(preferred);
        }
        for (EnumFacing facing : EnumFacing.values()) {
            if (!order.contains(facing)) {
                order.add(facing);
            }
        }
        return order;
    }

    private static boolean isSideAllowed(EnumFacing side) {
        if (side == EnumFacing.UP) {
            return CrawlConfig.allowCeilingCrawl;
        }
        if (side != EnumFacing.DOWN && side.getAxis().isHorizontal()) {
            return CrawlConfig.allowWallCrawl;
        }
        return true;
    }

    private static boolean hasAttachableSurface(World world, AxisAlignedBB box, EnumFacing side) {
        AxisAlignedBB probe = box.grow(0.03D).offset(side.getXOffset() * 0.12D, side.getYOffset() * 0.12D, side.getZOffset() * 0.12D);
        BlockPos min = new BlockPos(probe.minX, probe.minY, probe.minZ);
        BlockPos max = new BlockPos(probe.maxX, probe.maxY, probe.maxZ);

        for (BlockPos pos : BlockPos.getAllInBoxMutable(min, max)) {
            IBlockState state = world.getBlockState(pos);
            if (!canAttachTo(world, pos.toImmutable(), state, side)) {
                continue;
            }
            AxisAlignedBB collision = state.getCollisionBoundingBox(world, pos);
            if (collision == null) {
                continue;
            }
            AxisAlignedBB worldCollision = collision.offset(pos);
            if (worldCollision.intersects(probe)) {
                return true;
            }
        }
        return false;
    }

    private static boolean canAttachTo(World world, BlockPos pos, IBlockState state, EnumFacing side) {
        if (state.getMaterial() == Material.AIR || state.getMaterial().isLiquid()) {
            return false;
        }
        if (CrawlConfig.isBlocked(state)) {
            return false;
        }
        if (state.getCollisionBoundingBox(world, pos) == null) {
            return false;
        }
        return state.isSideSolid(world, pos, side.getOpposite());
    }
}
