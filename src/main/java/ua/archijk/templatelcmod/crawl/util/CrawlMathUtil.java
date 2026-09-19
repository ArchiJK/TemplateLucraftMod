package ua.archijk.templatelcmod.crawl.util;

public final class CrawlMathUtil {

    private CrawlMathUtil() {
    }

    public static float clamp01(float value) {
        if (Float.isNaN(value) || Float.isInfinite(value)) {
            throw new IllegalArgumentException("Interpolation value must be finite");
        }
        if (value < 0.0F) {
            return 0.0F;
        }
        if (value > 1.0F) {
            return 1.0F;
        }
        return value;
    }

    public static float lerp(float start, float end, float alpha) {
        return start + (end - start) * clamp01(alpha);
    }
}
