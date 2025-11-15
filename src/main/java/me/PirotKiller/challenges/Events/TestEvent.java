package me.PirotKiller.challenges.Events;

import me.PirotKiller.challenges.Challenges;
import me.PirotKiller.challenges.Counter.Days;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = Challenges.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TestEvent {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event){

        Component message = Component.literal("This is a message from my mod!"+ new Days().getDay(event.getPlayer()));
        event.getPlayer().sendSystemMessage(message);
    }

    @SubscribeEvent
    public static void onPressStartButton(PlayerInteractEvent.RightClickBlock event){
        Level world = event.getLevel();
        @NotNull BlockPos blockPos = event.getPos();
        if (blockPos == event.getEntity().getOnPos())
            return;

        Block clickedBlock = world.getBlockState(blockPos).getBlock();
        BlockPos clickedPosition = event.getPos();
        if (clickedBlock instanceof ButtonBlock){
            Player player = event.getEntity();
            BlockPos cageStartPosition = clickedPosition.above();
            // Send a message!
            player.sendSystemMessage(Component.literal("You clicked a button!"));
            createCage(world, cageStartPosition, 6);

            // And your day check
            long currentDay = world.getGameTime() / 24000L;
            player.sendSystemMessage(Component.literal("It is day " + currentDay));

        }
    }
    public static void createCage(Level world, BlockPos startPos, int size) {

        // Define the block states we'll use
        BlockState wallBlockState = Blocks.IRON_BARS.defaultBlockState();
        BlockState floorCeilingBlockState = Blocks.GLASS.defaultBlockState();

        // Loop through all 3 dimensions
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {

                    // Calculate the real-world position
                    BlockPos currentPos = startPos.offset(x, y, z);

                    // --- Logic to determine which block to place ---

                    // Check if it's on the floor (y == 0) or ceiling (y == size - 1)
                    if (y == 0 || y == (size - 1)) {
                        // Place glass for floor and ceiling
                        world.setBlock(currentPos, floorCeilingBlockState, 3);
                    }
                    // Check if it's on any of the vertical walls
                    else if (x == 0 || x == (size - 1) || // X-faces (West/East)
                            z == 0 || z == (size - 1))   // Z-faces (North/South)
                    {
                        // Place iron bars for the vertical walls
                        world.setBlock(currentPos, wallBlockState, 3);
                    }
                    // Else, it's inside the hollow part, do nothing (leave as air or existing block)
                }
            }
        }
    }

    @SubscribeEvent
    public static void changeColor(ViewportEvent.ComputeFogColor event){
        event.setRed(0.9f);
        event.setGreen(0.1f);
        event.setBlue(0.1f);
    }

}
