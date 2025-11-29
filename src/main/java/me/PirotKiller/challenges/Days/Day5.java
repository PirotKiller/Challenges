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

public class Day5 {
    public static void execute(ServerLevel overworld, BlockPos blockPos, ServerPlayer player){
        Manager.replaceCageBlocks(overworld, blockPos, Blocks.GLASS, Blocks.RED_STAINED_GLASS, 0, 0);
        player.sendSystemMessage(Component.literal("The cage is cracking...").withStyle(ChatFormatting.RED));
        Manager.playSound(overworld, player.getOnPos(), SoundEvents.GLASS_BREAK, 2.0f, 1.0f);

        Manager.spawnParticles(overworld, ParticleTypes.CRIT, blockPos.offset(3, 0, 3), 20, 4.0);
        Manager.sendTitleToPlayer(player,Component.literal("fdsfsdfsdf").withStyle(ChatFormatting.OBFUSCATED),Component.literal(""),10,70,20);

    }
}
