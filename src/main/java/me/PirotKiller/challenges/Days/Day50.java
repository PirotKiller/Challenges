package me.PirotKiller.challenges.Days;

import me.PirotKiller.challenges.Managers.Manager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;

import java.util.List;

public class Day50 {
    public void execute(ServerLevel overworld, List<BlockPos> pos, ServerPlayer player){
        overworld.setBlock(pos.get(0).below(), Blocks.STONE.defaultBlockState(), 3);
        overworld.setBlock(pos.get(1).below(), Blocks.STONE.defaultBlockState(), 3);
        overworld.setBlock(pos.get(0), Blocks.POLISHED_BLACKSTONE_BUTTON.rotate(Blocks.POLISHED_BLACKSTONE_BUTTON.defaultBlockState(),overworld,pos.get(0).north(), Rotation.CLOCKWISE_90), 3);
        overworld.setBlock(pos.get(1), Blocks.POLISHED_BLACKSTONE_BUTTON.defaultBlockState(), 3);
        player.sendSystemMessage(Component.literal("A new button has appeared... 'Press this before Day 70.'").withStyle(ChatFormatting.GRAY));
        Manager.playSound(overworld, player.getOnPos(), SoundEvents.PLAYER_LEVELUP, 2.0f, 1.0f);
    }
}
