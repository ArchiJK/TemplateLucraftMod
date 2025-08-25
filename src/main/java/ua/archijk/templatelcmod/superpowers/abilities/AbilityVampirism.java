package ua.archijk.templatelcmod.superpowers.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityToggle;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.common.MinecraftForge;
import ua.archijk.templatelcmod.AHIconHelper;

public class AbilityVampirism extends AbilityToggle {

    private static final float HEALING_PERCENTAGE = 0.1f;

    public AbilityVampirism(EntityLivingBase entity) {
        super(entity);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void updateTick() {
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        // player
        if (this.isEnabled() && event.getSource().getTrueSource() == this.entity) {
            float damage = event.getAmount();
            float healingAmount = damage * HEALING_PERCENTAGE;

            if (entity instanceof EntityPlayer) {
                entity.heal(healingAmount);
            } else {
                entity.setHealth(Math.min(entity.getMaxHealth(), entity.getHealth() + healingAmount));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawIcon(Minecraft mc, Gui gui, int x, int y) {
        AHIconHelper.drawIcon(mc, gui, x, y, 0, 12);
    }
}