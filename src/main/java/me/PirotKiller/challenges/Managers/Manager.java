package me.PirotKiller.challenges.Managers;

import com.mojang.brigadier.arguments.LongArgumentType;
import me.PirotKiller.challenges.Challenges;
import me.PirotKiller.challenges.Days.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = Challenges.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Manager {
    private static long ticksBeforeStart;
    private static Player tragetPlayer;
    private static long currentServerDay = 0;
    private static long lastServerDay = -1;
    private static long setDayTick = 0;
    private static long dayTicks = 0;


    public static final List<String> reward_items = List.of(
            "apexguns:r301",
            "apexguns:light_ammo");
    private static final BlockPos DAY_1_CAGE_POS = new BlockPos(-2864,130,855);
    private static final BlockPos START_BUTTON_POS = new BlockPos(-2857, 105, 855);
    private static final List<BlockPos> DAY_50_BUTTON_POS =List.of(
            new BlockPos(-2852, 106, 854),
            new BlockPos(-2852, 106, 856));

    public static final List<String> MOB_IDS = Arrays.asList(
            "mutantmonsters:mutant_zombie",
            "mutantmonsters:mutant_skeleton",
            "minecraft:zombie",
            "minecraft:skeleton",
            "the_tax_man:taxman",
            "graveyard:lich"
    );

//----------------------------------------------------------------------------------------------------
//                                            Flags
//----------------------------------------------------------------------------------------------------
    private static boolean start = false;
    private static int plantedSeedsCount = 0;
    private static boolean farmChallengeCompleted = false;
    private static boolean player_has_placed_trap_door = false;
    private static boolean player_has_placed_lever = false;
    private static boolean player_survived_first_blood_moon = false;
    private static boolean player_survived_second_blood_moon = false;
    private static boolean player_has_mystery_object = false;


//______________________________________________________________________________________________________________
//                                                      Timed Events flags
//______________________________________________________________________________________________________________

    private static boolean evening_29 = false;
    private static boolean evening_46 = false;
    private static boolean evening_71 = false;
    private static boolean evening_82 = false;

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher()
                .register(Commands.literal("setday")
                        .then(Commands.argument("day", LongArgumentType.longArg())
                                .executes(context -> setCurrentServerDay(LongArgumentType.getLong(context,"day")-1L)))
                );
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ServerLevel overworld = event.getServer().overworld();
            if (overworld == null) return;

            dayTicks = overworld.getDayTime();
            if (isStart()) {
                currentServerDay = (overworld.getGameTime() - ticksBeforeStart - setDayTick + 24000L) / 24000L;
                if (currentServerDay > 50)
                    playRandomRunningSound(overworld,event.getServer().getPlayerList().getPlayers().get(0));
            }

            if (currentServerDay > lastServerDay) {
                lastServerDay = currentServerDay;

                System.out.println("### NEW DAY on SERVER: " + currentServerDay + " ###");
//                todo: Play sound on player position -> done
                if (currentServerDay == 1L) {
                    new Day1().execute(overworld,DAY_1_CAGE_POS,event.getServer().getPlayerList().getPlayers().get(0));
                } else if (currentServerDay == 5L) {
                    new Day5().execute(overworld,DAY_1_CAGE_POS, event.getServer().getPlayerList().getPlayers().get(0));
                }else if (currentServerDay == 18L) {
                    new Day18().execute(overworld,DAY_1_CAGE_POS, event.getServer().getPlayerList().getPlayers().get(0));
                }else if (currentServerDay == 21L) {
                    new Day21().execute(overworld,START_BUTTON_POS, event.getServer().getPlayerList().getPlayers().get(0));
                }else if (currentServerDay == 50L) {
                    new Day50().execute(overworld,DAY_50_BUTTON_POS, event.getServer().getPlayerList().getPlayers().get(0));
                }else if (currentServerDay == 90L) {
                    new Day90().execute(event.getServer().getPlayerList().getPlayers().get(0));
                }else if (currentServerDay == 95L) {
                    new Day95().execute(overworld,START_BUTTON_POS, event.getServer().getPlayerList().getPlayers().get(0));
                }
            }
//______________________________________________________________________________________________________________
//                                                      Timed Events
//______________________________________________________________________________________________________________
            if (currentServerDay == 29 && !evening_29 && dayTicks == 13000){
                new Day29().execute(overworld,START_BUTTON_POS, event.getServer().getPlayerList().getPlayers().get(0));
                evening_29 = true;
            }if (currentServerDay == 46 && !evening_46 && dayTicks == 14000){
                new Day46().execute(overworld,START_BUTTON_POS, event.getServer().getPlayerList().getPlayers().get(0));
                evening_46 = true;
            }if (currentServerDay == 71 && !evening_71 && !isPlayer_has_mystery_object()){
                // remove block if button not pressed
//                tragetPlayer.sendSystemMessage(Component.literal("The button crumbles... you were too late."));
//                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                evening_71 = true;
            }if (currentServerDay == 82 && !evening_82 && dayTicks == 12000){
                new Day82().execute(overworld,START_BUTTON_POS, event.getServer().getPlayerList().getPlayers().get(0));
                evening_82 = true;
            }
        }
    }
//############################################################################################################
//                                   Challenges Events
//############################################################################################################


//****************************************************************************************************
//                    Start Check
//****************************************************************************************************

    @SubscribeEvent
    public static void checkStartButtonPress(PlayerInteractEvent.RightClickBlock event){
        Level world = event.getLevel();
        @NotNull BlockPos blockPos = event.getPos();
        Block clickedBlock = world.getBlockState(blockPos).getBlock();
        tragetPlayer = event.getEntity();
        if (blockPos == event.getEntity().getOnPos())
            return;
        if (blockPos.equals(START_BUTTON_POS)){
            if (clickedBlock instanceof ButtonBlock){
                if (start) {
                    ticksBeforeStart = world.getGameTime();
                    event.getEntity().sendSystemMessage(Component.literal("The challenge has already begun."));
                    return;
                }
                setStart(true);
            }
        }
//****************************************************************************************************
//                                          Challenge 4
//****************************************************************************************************
        else if (DAY_50_BUTTON_POS.contains(blockPos) && !isPlayer_has_mystery_object()) {
            if (currentServerDay > 49 && currentServerDay <= 70) {

                tragetPlayer.sendSystemMessage(Component.literal("You received a Mysterious Object!"));
                player_has_mystery_object = true;

                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                world.setBlock(blockPos.below(), Blocks.AIR.defaultBlockState(), 3);
            }
            // code shifted above
//            else {
//                tragetPlayer.sendSystemMessage(Component.literal("The button crumbles... you were too late."));
//                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
//            }
        }
    }

//****************************************************************************************************
//                                              Challenge 1
//****************************************************************************************************
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        // Server-side check
        if (event.getLevel().isClientSide()) return;

        // Only count if the challenge is active (Day 1 to Day 20)
        // and not already completed.
        if (currentServerDay >= 1 && currentServerDay <= 18 && !farmChallengeCompleted) {

            if (event.getPlacedBlock().getBlock() instanceof CropBlock ||
                    event.getPlacedBlock().getBlock() instanceof NetherWartBlock ||
                    event.getPlacedBlock().getBlock() instanceof CocoaBlock) {

                plantedSeedsCount++;

                if (event.getEntity() instanceof ServerPlayer player) {
//                    player.sendSystemMessage(Component.literal("Crops planted: " + plantedSeedsCount + "/30")
//                            .withStyle(ChatFormatting.GREEN));

                    // Check if target reached
                    if (plantedSeedsCount >= 30) {
                        Day1.completeFarmChallenge(player);
                    }
                }
            }
        }
//****************************************************************************************************
//                                          Challenge 2
//****************************************************************************************************
        else if (currentServerDay > 20 && currentServerDay <= 29) {
            if (player_has_placed_lever && player_has_placed_trap_door)
                return;
            if (event.getPlacedBlock().getBlock() instanceof TrapDoorBlock) {
                player_has_placed_trap_door = true;
            }
            if (event.getPlacedBlock().getBlock() instanceof LeverBlock){
                player_has_placed_lever = true;
            }
            if (player_has_placed_lever && player_has_placed_trap_door){
                if (event.getEntity() instanceof ServerPlayer player) {
                    player.sendSystemMessage(Component.literal("Challenge is complete.")
                            .withStyle(ChatFormatting.GREEN));
                    Day21.completeSafeHouseChallenge(player);
                }

            }
        }
    }
//****************************************************************************************************
//                                          Challenge 3
//****************************************************************************************************
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        // Server-side check
        if (event.getEntity().level().isClientSide()) return;

        // Only check on Day 46 and if not already completed
        if (currentServerDay == 46 && !player_survived_second_blood_moon) {

            // 1. Get the ID of the entity that died
            String deadEntityId = EntityType.getKey(event.getEntity().getType()).toString();

            // 2. Check if it matches our Mutant Skeleton Boss ID (Index 1 in your list)
            if (deadEntityId.equals(MOB_IDS.get(1))) {

                // 3. Check if a player killed it (optional, but good practice)
                if (event.getSource().getEntity() instanceof ServerPlayer player) {
                    // Mark as complete so it doesn't trigger twice
                    player_survived_second_blood_moon = true;
                    Day46.completeBloodMoonChallenge(player);
                }
                // If the boss died from something else (like sunlight or lava),
                // we might still want to give the reward to the 'targetPlayer' we stored earlier.
                else if (tragetPlayer instanceof ServerPlayer) {
                    player_survived_second_blood_moon = true;
                    Day46.completeBloodMoonChallenge((ServerPlayer) tragetPlayer);
                }
            }
        }
    }

//############################################################################################################
//                                   Misc Functions and Events
//############################################################################################################

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (!start)
            return;
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

    @SubscribeEvent
    public static void changeColor(ViewportEvent.ComputeFogColor event){
        if (currentServerDay >= 22 && currentServerDay <= 28) {
            event.setRed(0.5f);
            event.setGreen(0.3f);
            event.setBlue(0.3f);
        } else if (currentServerDay == 29 || currentServerDay == 46) {
            event.setRed(1f);
            event.setGreen(0f);
            event.setBlue(0f);
        }
    }

    public static void playRandomRunningSound(ServerLevel world, ServerPlayer player) {
        // 5% chance per tick
        if (world.random.nextFloat() < 0.0001f) {
            double x = player.getX() + (world.random.nextDouble() - 0.5) * 10.0;
            double y = player.getY();
            double z = player.getZ() + (world.random.nextDouble() - 0.5) * 10.0;
            BlockPos soundPos = new BlockPos((int)x, (int)y, (int)z);

            playSound(world, soundPos, SoundEvents.GRASS_STEP, 1.0f, 1.5f);
        }
    }

    public static boolean isPlayer_has_mystery_object() {
        return player_has_mystery_object;
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
        setDayTick = getCurrentServerDay() - currentServerDay * 24000;
        lastServerDay = currentServerDay -1; // So the tick event re-triggers
        return 1;
    }

    public static void setFarmChallengeCompleted(boolean farmChallengeCompleted) {
        Manager.farmChallengeCompleted = farmChallengeCompleted;
    }

    public static void sendTitleToPlayer(ServerPlayer player, Component titleText, Component subtitleText, int fadeIn, int stay, int fadeOut) {

        // 1. Set the timings
        ClientboundSetTitlesAnimationPacket timingsPacket = new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut);
        player.connection.send(timingsPacket);

        // 2. Set the subtitle (if any)
        if (subtitleText != null) {
            ClientboundSetSubtitleTextPacket subtitlePacket = new ClientboundSetSubtitleTextPacket(subtitleText);
            player.connection.send(subtitlePacket);
        }

        // 3. Set the main title
        ClientboundSetTitleTextPacket titlePacket = new ClientboundSetTitleTextPacket(titleText);
        player.connection.send(titlePacket);
    }

    public static void spawnMob(ServerLevel world, BlockPos pos, String mobId, int count) {
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobId));
        if (entityType == null) {
            System.out.println("ERROR: Cannot find mob ID: " + mobId);
            return;
        }

        for(int i = 0; i < count; i++) {
            // Spawn in a 10x10 area around the position
            int x = pos.getX() + world.random.nextInt(20) - 10;
            int z = pos.getZ() + world.random.nextInt(20) - 10;
            int y = pos.getY();
            entityType.spawn(world, new BlockPos(x, y, z), MobSpawnType.MOB_SUMMONED);
        }
    }

    public static void spawnParticles(ServerLevel world, ParticleOptions particle, BlockPos pos, int count, double radius) {
        for (int i = 0; i < count; i++) {
            // Get random coordinates within the radius
            double x = pos.getX() + (world.random.nextDouble() - 0.5) * (radius * 2);
            double y = pos.getY() + world.random.nextDouble() * (radius * 0.5); // Less vertical spread
            double z = pos.getZ() + (world.random.nextDouble() - 0.5) * (radius * 2);

            // world.sendParticles(particle, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
            // Using a speed of 0.1 makes cloud particles look better
            world.sendParticles(particle, x, y, z, 1, 0.0, 0.0, 0.0, 0.1);
        }
    }
    public static void playSound(ServerLevel world, BlockPos pos, net.minecraft.sounds.SoundEvent sound, float volume, float pitch) {
        // 'null' means the sound is not played *by* a specific entity
        world.playSound(null, pos, sound, SoundSource.MASTER, volume, pitch);
    }
    public static void replaceCageBlocks(Level world, BlockPos startPos, Block targetBlock, Block replaceBlock, int minY, int maxY) {
        BlockState replaceState = replaceBlock.defaultBlockState();
        int size = 6; // Cage size

        for (int x = 0; x < size; x++) {
            for (int y = minY; y <= maxY; y++) { // Only Y layers 0 and 5 (floor/ceiling)
                for (int z = 0; z < size; z++) {
                    BlockPos currentPos = startPos.offset(x, y, z);
                    if (world.getBlockState(currentPos).getBlock() == targetBlock) {
                        world.setBlock(currentPos, replaceState, 3);
                    }
                }
            }
        }
    }
}
