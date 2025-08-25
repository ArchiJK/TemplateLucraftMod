package ua.archijk.templatelcmod.superpowers;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityEntry;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.archijk.templatelcmod.superpowers.abilities.*;

@EventBusSubscriber(
        modid = "templatelucraftmod"
)
public class Abilities {
    public Abilities() {
    }

    @SubscribeEvent
    public static void onRegisterAbilities(RegistryEvent.Register<AbilityEntry> e) {

        e.getRegistry().register(new AbilityEntry(AbilityTogglePowerTwo.class, new ResourceLocation("templatelcmod", "abilitytogglepower_two")));

        e.getRegistry().register(new AbilityEntry(AbilityVampirism.class, new ResourceLocation("templatelcmod", "abilityvampirism")));

    }

    @SubscribeEvent
    public static void onRegisterConditions(RegistryEvent.Register<AbilityCondition.ConditionEntry> e) {
    }
}
