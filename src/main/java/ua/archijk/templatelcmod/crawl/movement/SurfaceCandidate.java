package ua.archijk.templatelcmod.crawl.movement;

import net.minecraft.util.EnumFacing;

public class SurfaceCandidate {

    private final EnumFacing surface;

    public SurfaceCandidate(EnumFacing surface) {
        this.surface = surface;
    }

    public EnumFacing getSurface() {
        return this.surface;
    }
}
