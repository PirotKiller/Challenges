package me.PirotKiller.challenges.Days;

import me.PirotKiller.challenges.Managers.Manager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class Day21 {
    public static void execute(ServerLevel overworld, BlockPos pos, ServerPlayer player){
        Manager.sendTitleToPlayer(player, Component.literal("Challenge!").withStyle(ChatFormatting.AQUA),
                Component.literal("Build a safe house before the Blood ☽ rises.").withStyle(ChatFormatting.YELLOW), 10, 70, 20);
        player.sendSystemMessage(Component.literal("Build a safe house before the Blood Moon rises.").withStyle(ChatFormatting.YELLOW));

        Manager.playSound(overworld, player.getOnPos(), SoundEvents.BEACON_ACTIVATE, 1.0f, 1.0f);

        Manager.spawnParticles(overworld, ParticleTypes.WAX_ON, player.getOnPos(), 20, 2.0);
    }

    public static void completeSafeHouseChallenge(ServerPlayer player) {
        Manager.setFarmChallengeCompleted(true);

        Manager.sendTitleToPlayer(player, Component.literal("SAFE HOUSE COMPLETE!").withStyle(ChatFormatting.GREEN),
                Component.literal("Here is your reward."), 10, 70, 20);

        player.sendSystemMessage(Component.literal("You received three Stack of").withStyle(ChatFormatting.GREEN)
                .append(" Bullets!").withStyle(ChatFormatting.GOLD));

        if (player.level() instanceof ServerLevel serverLevel) {
            Manager.playSound(serverLevel, player.getOnPos(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);

            Manager.spawnParticles(serverLevel, ParticleTypes.HAPPY_VILLAGER, player.getOnPos(), 30, 2.0);
        }

        ItemStack ammo = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Manager.reward_items.get(1))),64*3);
        player.getInventory().add(ammo);

    }
}
