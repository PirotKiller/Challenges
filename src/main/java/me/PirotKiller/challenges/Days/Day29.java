package me.PirotKiller.challenges.Days;

import me.PirotKiller.challenges.Managers.Manager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;

public class Day29 {
    public static void execute(ServerLevel overworld, BlockPos pos, ServerPlayer player){

        Manager.sendTitleToPlayer(player, Component.literal("☽ BLOOD MOON ☽").withStyle(ChatFormatting.RED),
                Component.literal("The hordes are coming!").withStyle(ChatFormatting.AQUA), 10, 70, 20);
        Manager.spawnMob(overworld, pos.east(5), Manager.MOB_IDS.get(2), 20);
        Manager.spawnMob(overworld, pos.east(5), Manager.MOB_IDS.get(3), 10);
        Manager.playSound(overworld, player.getOnPos().east(6), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 2.0f, 0.5f);
    }


}
