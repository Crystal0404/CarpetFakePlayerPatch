package com.github.crystal0404.mods.carpetfakeplayerpatch;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CarpetFakePlayerPatch implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Carpet Fake Player Patch");

    @Override
    public void onInitialize() {
        String carpet_version = FabricLoader.getInstance()
                .getModContainer("carpet")
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();

        LOGGER.info("Added the fake player patch for Carpet(version: {})", carpet_version);
        LOGGER.info("Regarding the patch content, please check https://github.com/gnembon/fabric-carpet/pull/2114");
    }
}
