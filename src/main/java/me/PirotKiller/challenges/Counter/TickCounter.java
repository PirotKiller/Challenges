package me.PirotKiller.challenges.Counter;

import me.PirotKiller.challenges.Challenges;
import me.PirotKiller.challenges.Days.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Challenges.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TickCounter {
    // 1. Give your overlay a unique ID
    private static final String PLAYER_COORDS_HUD_ID =
            Challenges.MOD_ID + ":player_coords_hud";
    static Long day;
    static int ticks;
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        // Only run at the end of a tick
        if (event.phase == TickEvent.Phase.END) {
            MinecraftServer server = event.getServer();
            ServerLevel overworld = server.overworld();
            ticks = overworld.getServer().getTickCount();
            day = overworld.getGameTime()/24000L;
//            ticks = overworld.getGameTime();

            if (day == 1L) {
                new Day1().execute();
            } else if (day == 5L) {
                new Day5().execute();
            }else if (day == 18L) {
                new Day18().execute();
            }else if (day == 21L) {
                new Day21().execute();
            }else if (day == 29L) {
                new Day29().execute();
            }else if (day == 46L) {
                new Day46().execute();
            }else if (day == 50L) {
                new Day50().execute();
            }else if (day == 82L) {
                new Day82().execute();
            }else if (day == 90L) {
                new Day90().execute();
            }else if (day == 95L) {
                new Day95().execute();
            }

        }
    }
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        if (mc.player == null) {
            return;
        }
        String textToShow = "Day: " + day + "/n Ticks: "+ticks ;
        float scale = 2.0f;
        guiGraphics.pose().pushPose();


        guiGraphics.pose().scale(scale, scale, 1.0f);
        int x = 10; // pixels from left
        int y = 10; // pixels from top
        int color = 0xFFFFFF;

        guiGraphics.drawString(font, textToShow, (int)(x / scale),(int)(y / scale), color, false);
        guiGraphics.pose().popPose();
    }


}
