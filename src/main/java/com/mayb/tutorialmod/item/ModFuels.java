package com.mayb.tutorialmod.item;

import com.mayb.tutorialmod.TutorialMod;
import net.fabricmc.fabric.api.registry.FuelRegistry;

public class ModFuels {

    public static void RegisterModFuels() {
        TutorialMod.LOGGER.info("Registering Mod Items for " + TutorialMod.MOD_ID);

        FuelRegistry.INSTANCE.add(ModItems.STARLIGHT_ASHES, 1200);
    }
}
