package ua.archijk.templatelcmod.crawl.client;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import ua.archijk.templatelcmod.crawl.capability.ICrawlData;
import ua.archijk.templatelcmod.crawl.movement.CrawlPoseState;
import ua.archijk.templatelcmod.crawl.util.CrawlCapabilityUtil;
import ua.archijk.templatelcmod.crawl.util.CrawlConstants;
import ua.archijk.templatelcmod.crawl.util.CrawlMathUtil;

public class CrawlModelPlayer extends ModelPlayer {

    public CrawlModelPlayer(float modelSize, boolean smallArms) {
        super(modelSize, smallArms);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        if (!(entityIn instanceof EntityPlayer)) {
            return;
        }

        ICrawlData data = CrawlCapabilityUtil.get(entityIn);
        if (data == null || (!data.isCrawling() && data.getTransitionTicks() <= 0)) {
            return;
        }

        float progress = data.getTransitionTicks() > 0
                ? CrawlMathUtil.clamp01((CrawlConstants.TRANSITION_TICKS - data.getTransitionTicks()) / (float) CrawlConstants.TRANSITION_TICKS)
                : 1.0F;

        float walkSwing = (float) Math.sin(limbSwing * 0.9F) * 0.6F * limbSwingAmount;
        float crawlBody = CrawlMathUtil.lerp(0.0F, 1.45F, progress);
        float crawlHead = CrawlMathUtil.lerp(this.bipedHead.rotateAngleX, -1.25F, progress);

        if (data.getPoseState() == CrawlPoseState.RAISING) {
            crawlBody = CrawlMathUtil.lerp(1.2F, 0.0F, progress);
            crawlHead = CrawlMathUtil.lerp(-1.0F, this.bipedHead.rotateAngleX, progress);
        } else if (data.getPoseState() == CrawlPoseState.CLIMBING) {
            walkSwing = (float) Math.sin(ageInTicks * 0.9F) * 0.8F;
        }

        this.bipedBody.rotateAngleX = crawlBody;
        this.bipedHead.rotateAngleX = crawlHead;
        this.bipedHeadwear.rotateAngleX = crawlHead;

        this.bipedRightArm.rotateAngleX = -walkSwing;
        this.bipedLeftArm.rotateAngleX = walkSwing;
        this.bipedRightLeg.rotateAngleX = walkSwing;
        this.bipedLeftLeg.rotateAngleX = -walkSwing;

        this.bipedRightArm.rotateAngleY = 0.0F;
        this.bipedLeftArm.rotateAngleY = 0.0F;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;
    }
}
