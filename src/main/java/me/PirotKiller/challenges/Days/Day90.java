package me.PirotKiller.challenges.Days;

import me.PirotKiller.challenges.Managers.Manager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class Day90 {
    public void execute(ServerPlayer player){
        Manager.sendTitleToPlayer(player,
                Component.literal("BOSS BATTLE").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD),
                Component.literal("Prepare yourself...").withStyle(ChatFormatting.GOLD),
                10, 70, 20);


        player.sendSystemMessage(Component.literal("Challenge: Defeat the Corrupt Champion.")
                .withStyle(ChatFormatting.RED));
    }
}
