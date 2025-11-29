package me.PirotKiller.challenges.Days;

import me.PirotKiller.challenges.Managers.Manager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

import java.util.List;

public class Day50 {
    public static void execute(ServerLevel overworld, List<BlockPos> pos, ServerPlayer player){
        for (BlockPos buttonPos : pos) {
            BlockPos supportBlockPos = buttonPos.north();
            overworld.setBlock(supportBlockPos.south(1).east(1), Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState(), 3);
            overworld.setBlock(supportBlockPos.south(1).east(1).below(1), Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState(), 3);

            BlockState buttonState = Blocks.STONE_BUTTON.defaultBlockState()
                    .setValue(ButtonBlock.FACE, AttachFace.WALL)
                    .setValue(ButtonBlock.FACING, Direction.WEST);

            overworld.setBlock(buttonPos, buttonState, 3);

            Manager.spawnParticles(overworld, ParticleTypes.DRAGON_BREATH, buttonPos, 20, 0.5);
        }
        Manager.playSound(overworld, pos.get(0), SoundEvents.END_PORTAL_SPAWN, 1.0f, 0.5f);

        player.sendSystemMessage(Component.literal("A new button has appeared... 'Press this before Day 70.'")
                .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC));

        Manager.sendTitleToPlayer(player,
                Component.literal("?").withStyle(ChatFormatting.DARK_PURPLE),
                Component.literal("Something has appeared..."),
                10, 70, 20);
    }
}
