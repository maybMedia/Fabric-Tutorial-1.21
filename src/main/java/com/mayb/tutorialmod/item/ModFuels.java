package com.mayb.tutorialmod.item;

import com.mayb.tutorialmod.TutorialMod;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;

import java.util.Map;

public class ModFuels {

    // A Map for future-proofing: Key is the item, Value is the burn time (ticks)
    private static final Map<Item, Integer> FUEL_MAP = Map.of(
            ModItems.STARLIGHT_ASHES, 1200
            // Add more fuels here as needed
    );

    public static void registerModFuels() {
        TutorialMod.LOGGER.info("Registering Mod Items for " + TutorialMod.MOD_ID);

        FuelRegistry registry = FuelRegistry.INSTANCE;

        // Register fuels using the map
        FUEL_MAP.forEach(registry::add);
    }
}
