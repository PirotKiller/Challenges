package me.PirotKiller.challenges.Days;

import me.PirotKiller.challenges.Managers.Manager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;

public class Day5 {
    public void execute(ServerLevel overworld, BlockPos blockPos, ServerPlayer player){
        Manager.replaceCageBlocks(overworld, blockPos, Blocks.GLASS, Blocks.RED_STAINED_GLASS, 0, 0);
        player.sendSystemMessage(Component.literal("The cage is cracking...").withStyle(ChatFormatting.RED));
        Manager.sendTitleToPlayer(player,Component.literal("fdsfsdfsdf").withStyle(ChatFormatting.OBFUSCATED),Component.literal(""),10,70,20);

    }
}
