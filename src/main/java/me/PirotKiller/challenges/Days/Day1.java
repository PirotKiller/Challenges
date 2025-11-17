package me.PirotKiller.challenges.Days;

import me.PirotKiller.challenges.Managers.Manager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class Day1 {

    private Manager manager = new Manager();

    public void execute(ServerLevel world, ServerPlayer player){
        int cageSize = 6;
        BlockPos cageStartPosition = new BlockPos(-2864,150,855);
        BlockPos spawnPos = cageStartPosition.offset(cageSize / 2,1,cageSize / 2);
//        createCage(world, cageStartPosition, cageSize);
//        spawnModdedMob(world,spawnPos, Manager.MOB_IDS.get(2));
        Manager.sendTitleToPlayer(player,"LOOK UP!","Challenge: Build a farm before Day 20",10, 70, 20);
        player.sendSystemMessage(Component.literal("We will drop him anytime..."));
        player.sendSystemMessage(Component.literal("Challenge: Build a farm before Day 20"));
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

    public static void spawnModdedMob(ServerLevel world, BlockPos pos, String mobs) {
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobs));
        entityType.spawn(world,pos, MobSpawnType.MOB_SUMMONED);
    }
}
