package me.PirotKiller.challenges.Managers;

import com.mojang.brigadier.arguments.LongArgumentType;
import me.PirotKiller.challenges.Challenges;
import me.PirotKiller.challenges.Days.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = Challenges.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Manager {
    private static boolean start = false;
    private static long ticksBeforeStart;
    private static Player tragetPlayer;

    private static long currentServerDay = 0;

    private static long lastServerDay = -1;

    public static final List<String> MOB_IDS = Arrays.asList(
            "mutantmonsters:mutant_zombie",
            "mutantmonsters:mutant_skeleton",
            "minecraft:zombie",
            "minecraft:skeleton",
            "the_tax_man:taxman",
            "graveyard:corrupted_pillager"
    );

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher()
                .register(Commands.literal("setday")
                        .then(Commands.argument("day", LongArgumentType.longArg())
                                .executes(context -> setCurrentServerDay(LongArgumentType.getLong(context,"day"))))
                );
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ServerLevel overworld = event.getServer().overworld();
            if (overworld == null) return;

            if (start)
                currentServerDay = (overworld.getGameTime() - ticksBeforeStart + 24000L) / 24000L;

            if (currentServerDay > lastServerDay) {
                lastServerDay = currentServerDay;

                System.out.println("### NEW DAY on SERVER: " + currentServerDay + " ###");

                if (currentServerDay == 1L) {
                    new Day1().execute(overworld,event.getServer().getPlayerList().getPlayers().get(0));
                } else if (currentServerDay == 5L) {
                    new Day5().execute();
                }else if (currentServerDay == 18L) {
                    new Day18().execute();
                }else if (currentServerDay == 21L) {
                    new Day21().execute();
                }else if (currentServerDay == 29L) {
                    new Day29().execute();
                }else if (currentServerDay == 46L) {
                    new Day46().execute();
                }else if (currentServerDay == 50L) {
                    new Day50().execute();
                }else if (currentServerDay == 82L) {
                    new Day82().execute();
                }else if (currentServerDay == 90L) {
                    new Day90().execute();
                }else if (currentServerDay == 95L) {
                    new Day95().execute();
                }
            }
        }
    }

    @SubscribeEvent
    public static void checkStartButtonPress(PlayerInteractEvent.RightClickBlock event){
        Level world = event.getLevel();
        ticksBeforeStart = world.getGameTime();
        @NotNull BlockPos blockPos = event.getPos();
        if (blockPos == event.getEntity().getOnPos())
            return;
        if (!blockPos.equals(new BlockPos(-2857, 105, 855))){
            return;
        }
        Block clickedBlock = world.getBlockState(blockPos).getBlock();
        if (clickedBlock instanceof ButtonBlock){
            tragetPlayer = event.getEntity();
            tragetPlayer.sendSystemMessage(Component.literal("You clicked a button!"));
            setStart(true);
        }
    }




    @SubscribeEvent
    public static void changeColor(ViewportEvent.ComputeFogColor event){
        if (currentServerDay == 22) {
            event.setRed(0.5f);
            event.setGreen(0.3f);
            event.setBlue(0.3f);
        } else if (currentServerDay == 29) {
            event.setRed(0.9f);
            event.setGreen(0.3f);
            event.setBlue(0.3f);
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
//        if (!start)
//            return;
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        ClientLevel clientLevel = mc.level;

        if (clientLevel == null) {
            return;
        }

        String textToShow = "Day: " + currentServerDay;

        float scale = 2.0f;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1.0f);
        int x = 10;
        int y = 10;
        int color = 0xFFFFFF;

        guiGraphics.drawString(font, textToShow, (int)(x / scale),(int)(y / scale), color, false);
        guiGraphics.pose().popPose();
    }


    public static boolean isStart() {
        return start;
    }

    public static void setStart(boolean start) {
        Manager.start = start;
    }

    public static long getCurrentServerDay() {
        return currentServerDay;
    }

    public static int setCurrentServerDay(long currentServerDay) {
        Manager.currentServerDay = currentServerDay;
        return 1;
    }

    public static void sendTitleToPlayer(ServerPlayer player, String titleText, String subtitleText, int fadeIn, int stay, int fadeOut) {

        // 1. Set the timings
        ClientboundSetTitlesAnimationPacket timingsPacket = new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut);
        player.connection.send(timingsPacket);

        // 2. Set the subtitle (if any)
        if (subtitleText != null) {
            Component subtitle = Component.literal(subtitleText);
            ClientboundSetSubtitleTextPacket subtitlePacket = new ClientboundSetSubtitleTextPacket(subtitle);
            player.connection.send(subtitlePacket);
        }

        // 3. Set the main title
        Component title = Component.literal(titleText);
        ClientboundSetTitleTextPacket titlePacket = new ClientboundSetTitleTextPacket(title);
        player.connection.send(titlePacket);
    }
}
