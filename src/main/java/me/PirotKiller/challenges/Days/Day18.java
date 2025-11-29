package me.PirotKiller.challenges.Days;

import me.PirotKiller.challenges.Managers.Manager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;

public class Day18 {
    public static void execute(ServerLevel overworld, BlockPos blockPos, ServerPlayer player){
        Manager.sendTitleToPlayer(player, Component.literal("IT'S FALLING!").withStyle(ChatFormatting.DARK_RED), Component.literal("RUN!"), 10, 40, 20);
        player.sendSystemMessage(Component.literal("The cage floor has vanished!").withStyle(ChatFormatting.YELLOW));

        Manager.replaceCageBlocks(overworld, blockPos, Blocks.RED_STAINED_GLASS, Blocks.AIR, 0, 0); // Replaces only layer Y=0
        Manager.spawnParticles(overworld, ParticleTypes.EXPLOSION_EMITTER, blockPos, 10, 3.0);

        Manager.playSound(overworld, player.getOnPos(), SoundEvents.WARDEN_SONIC_BOOM, 2.0f, 1.0f);
        Manager.playSound(overworld, blockPos, SoundEvents.NETHERITE_BLOCK_BREAK, 2.0f, 0.5f);
    }
}
