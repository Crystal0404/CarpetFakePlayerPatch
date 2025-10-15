package com.github.crystal0404.mods.carpetfakeplayerpatch.mixin;

import carpet.patches.EntityPlayerMPFake;
import com.github.crystal0404.mods.carpetfakeplayerpatch.CarpetFakePlayerPatch;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EntityPlayerMPFake.class)
public abstract class EntityPlayerMPFakeMixin {
    @Inject(
            method = "lambda$createFake$2",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;" +
                            "placeNewPlayer(Lnet/minecraft/network/Connection;" +
                            "Lnet/minecraft/server/level/ServerPlayer;" +
                            "Lnet/minecraft/server/network/CommonListenerCookie;)V",
                    shift = At.Shift.AFTER
            )
    )
    private static void createFakeMixin(
            String name,
            GameProfile gameprofile,
            MinecraftServer server,
            ServerLevel worldIn,
            Vec3 pos,
            double yaw,
            double pitch,
            GameType gamemode,
            ResourceKey<Level> dimensionId,
            boolean flying,
            GameProfile p,
            Throwable t,
            CallbackInfo ci,
            @Local(ordinal = 0) EntityPlayerMPFake instance
    ) {
        loadPlayerData(instance);
        instance.stopRiding();
    }

    @Inject(
            method = "createShadow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;" +
                            "placeNewPlayer(Lnet/minecraft/network/Connection;" +
                            "Lnet/minecraft/server/level/ServerPlayer;" +
                            "Lnet/minecraft/server/network/CommonListenerCookie;)V",
                    shift = At.Shift.AFTER
            )
    )
    private static void createShadowMixin(
            MinecraftServer server,
            ServerPlayer player,
            CallbackInfoReturnable<EntityPlayerMPFake> cir,
            @Local(ordinal = 0) EntityPlayerMPFake playerShadow
    ) {
        loadPlayerData(playerShadow);
    }

    @Unique
    private static void loadPlayerData(EntityPlayerMPFake player) {
        try (
                ProblemReporter.ScopedCollector collector = new ProblemReporter.ScopedCollector(
                        player.problemPath(),
                        CarpetFakePlayerPatch.LOGGER
                )
        ) {
            Optional<ValueInput> optional = player
                    .level()
                    .getServer()
                    .getPlayerList()
                    .loadPlayerData(player.nameAndId())
                    .map(it -> TagValueInput.create(collector, player.registryAccess(), it));
            optional.ifPresent(valueInput -> {
                player.load(valueInput);
                player.loadAndSpawnEnderPearls(valueInput);
                player.loadAndSpawnParentVehicle(valueInput);
            });
        }
    }
}
