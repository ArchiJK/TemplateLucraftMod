package ua.archijk.templatelcmod.superpowers.powers;

import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.events.AbilityKeyEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.archijk.templatelcmod.TemplateLCMod;

@Mod.EventBusSubscriber(modid = TemplateLCMod.MODID)
public class AHSuperpowers {

    public static SuperpowerAlimer ALIMER = new SuperpowerAlimer();

    @SubscribeEvent
    public static void onRegisterSuperpowers(RegistryEvent.Register<Superpower> e) {
        e.getRegistry().register(ALIMER);
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent e) {
        if (e.getSource() != null && e.getSource().getTrueSource() != null && e.getSource().getTrueSource() instanceof EntityLivingBase) {
            SuperpowerAlimer.addXP((EntityLivingBase) e.getSource().getTrueSource(), SuperpowerAlimer.XP_AMOUNT_KILL);
        }
    }

    @SubscribeEvent
    public static void onKill(AbilityKeyEvent e) {
        if (e.pressed) {
                SuperpowerAlimer.addXP(e.ability.getEntity(), SuperpowerAlimer.XP_AMOUNT_VAMPIRISM);
        }
    }

}
