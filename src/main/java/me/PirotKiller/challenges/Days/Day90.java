package me.PirotKiller.challenges.Days;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class Day90 {
    public void execute(ServerPlayer player){
        player.sendSystemMessage(Component.literal("Challenge: Defeat the Corrupt Champion."));

    }
}
