package ua.archijk.templatelcmod.crawl.config;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class CrawlConfig {

    private static Configuration configuration;
    private static final Set<ResourceLocation> BLOCKED_BLOCKS = new HashSet<>();

    public static boolean allowWallCrawl = true;
    public static boolean allowCeilingCrawl = true;
    public static double crawlSpeed = 0.08D;
    public static int maxClimbHeight = 1;
    public static String[] blockedBlocks = new String[]{"minecraft:air", "minecraft:water", "minecraft:flowing_water", "minecraft:lava", "minecraft:flowing_lava"};

    private CrawlConfig() {
    }

    public static void init(File configFile) {
        configuration = new Configuration(configFile);
        sync();
    }

    public static void sync() {
        if (configuration == null) {
            return;
        }

        String category = "crawl";
        allowWallCrawl = configuration.getBoolean("allowWallCrawl", category, allowWallCrawl, "Allow crawling on vertical surfaces.");
        allowCeilingCrawl = configuration.getBoolean("allowCeilingCrawl", category, allowCeilingCrawl, "Allow crawling on ceilings.");
        crawlSpeed = configuration.getFloat("crawlSpeed", category, (float) crawlSpeed, 0.01F, 0.5F, "Horizontal crawling speed multiplier.");
        maxClimbHeight = configuration.getInt("maxClimbHeight", category, maxClimbHeight, 0, 1, "Maximum auto climb height in blocks.");
        blockedBlocks = configuration.getStringList("blockedBlocks", category, blockedBlocks,
                "Registry names of blocks that cannot be used as crawl surfaces.");

        BLOCKED_BLOCKS.clear();
        for (String value : blockedBlocks) {
            ResourceLocation id = ResourceLocation.tryCreate(value.trim());
            if (id != null) {
                BLOCKED_BLOCKS.add(id);
            }
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static boolean isBlocked(IBlockState state) {
        Block block = state.getBlock();
        ResourceLocation registryName = block.getRegistryName();
        return registryName != null && BLOCKED_BLOCKS.contains(registryName);
    }

    public static Set<ResourceLocation> getBlockedBlocks() {
        return Collections.unmodifiableSet(BLOCKED_BLOCKS);
    }

}
