package ua.archijk.templatelcmod.crawl.client;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.archijk.templatelcmod.crawl.capability.ICrawlData;
import ua.archijk.templatelcmod.crawl.movement.CrawlPoseState;
import ua.archijk.templatelcmod.crawl.util.CrawlCapabilityUtil;
import ua.archijk.templatelcmod.crawl.util.CrawlConstants;
import ua.archijk.templatelcmod.crawl.util.CrawlMathUtil;
import ua.archijk.templatelcmod.crawl.util.CrawlOrientationUtil;

public class CrawlClientEvents {

    private boolean matrixPushed;

    @SubscribeEvent
    public void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        ICrawlData data = CrawlCapabilityUtil.get(player);
        if (data == null || (!data.isCrawling() && data.getTransitionTicks() <= 0)) {
            return;
        }

        float blend = 1.0F;
        if (data.getTransitionTicks() > 0) {
            blend = CrawlMathUtil.clamp01((CrawlConstants.TRANSITION_TICKS - data.getTransitionTicks() + event.getPartialRenderTick()) / (float) CrawlConstants.TRANSITION_TICKS);
        }

        float fromPitch = CrawlOrientationUtil.getPitchForSurface(data.getPreviousSurface());
        float toPitch = CrawlOrientationUtil.getPitchForSurface(data.getSurface());
        float fromRoll = CrawlOrientationUtil.getRollForSurface(data.getPreviousSurface());
        float toRoll = CrawlOrientationUtil.getRollForSurface(data.getSurface());

        // Blend previous->current surface orientation to avoid one-tick snapping between normals.
        float pitch = CrawlMathUtil.lerp(fromPitch, toPitch, blend);
        float roll = CrawlMathUtil.lerp(fromRoll, toRoll, blend);

        GlStateManager.pushMatrix();
        GlStateManager.translate(event.getX(), event.getY() + player.height * 0.5F, event.getZ());
        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(roll, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(-event.getX(), -(event.getY() + player.height * 0.5F), -event.getZ());
        this.matrixPushed = true;

        applyPoseAnimation((ModelBiped) event.getRenderer().getMainModel(), player, data.getPoseState(), event.getPartialRenderTick());
    }

    @SubscribeEvent
    public void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        if (this.matrixPushed) {
            GlStateManager.popMatrix();
            this.matrixPushed = false;
        }
    }

    private static void applyPoseAnimation(ModelBiped model, EntityPlayer player, CrawlPoseState poseState, float partialTicks) {
        if (poseState == CrawlPoseState.STANDING) {
            return;
        }

        float walk = player.limbSwing - player.limbSwingAmount * (1.0F - partialTicks);
        float swing = (float) Math.sin(walk * 0.8F) * 0.35F;

        model.bipedBody.rotateAngleX = (float) Math.PI / 2.0F;
        model.bipedHead.rotateAngleX = -((float) Math.PI / 2.0F);
        model.bipedLeftArm.rotateAngleX = swing;
        model.bipedRightArm.rotateAngleX = -swing;
        model.bipedLeftLeg.rotateAngleX = -swing;
        model.bipedRightLeg.rotateAngleX = swing;

        if (poseState == CrawlPoseState.CLIMBING) {
            model.bipedBody.rotateAngleX = (float) Math.PI / 2.2F;
            model.bipedLeftArm.rotateAngleX = 1.2F;
            model.bipedRightArm.rotateAngleX = -1.2F;
        } else if (poseState == CrawlPoseState.LOWERING) {
            model.bipedBody.rotateAngleX = (float) Math.PI / 2.6F;
        } else if (poseState == CrawlPoseState.RAISING) {
            model.bipedBody.rotateAngleX = (float) Math.PI / 3.0F;
        }
    }
}
