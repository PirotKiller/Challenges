package me.PirotKiller.challenges.Counter;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Days {
    private Level world;
    public long getDay(Player player){
        world = player.level();
        return world.getGameTime()/24000L;
    }
}
