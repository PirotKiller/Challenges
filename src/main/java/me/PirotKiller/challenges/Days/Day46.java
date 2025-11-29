package me.PirotKiller.challenges.Days;

import me.PirotKiller.challenges.Managers.Manager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class Day46 {
    public static void execute(ServerLevel overworld, BlockPos pos, ServerPlayer player){

        Manager.sendTitleToPlayer(player, Component.literal("☽ BLOOD MOON ☽").withStyle(ChatFormatting.RED),
                Component.literal("☠ They are back... and stronger.").withStyle(ChatFormatting.AQUA), 10, 70, 20);

        Manager.spawnMob(overworld, pos, Manager.MOB_IDS.get(2), 30);
        Manager.spawnMob(overworld, pos, Manager.MOB_IDS.get(3), 15);
        Manager.spawnMob(overworld, pos, Manager.MOB_IDS.get(1), 1);
        Manager.playSound(overworld, player.getOnPos().east(6), SoundEvents.WITHER_SPAWN, 2.0f, 1.0f);
    }

    public static void completeBloodMoonChallenge(ServerPlayer player) {

        Manager.sendTitleToPlayer(player, Component.literal("SURVIVED!").withStyle(ChatFormatting.GREEN),
                Component.literal("The Blood Moon fades..."), 10, 70, 20);
        player.sendSystemMessage(Component.literal("You survived the Second Blood Moon!").withStyle(ChatFormatting.GREEN));
        player.sendSystemMessage(Component.literal("You received Two Villager spawn eggs and a Stack of ").withStyle(ChatFormatting.GREEN)
                .append("Bullets!").withStyle(ChatFormatting.GOLD));

        player.getInventory().add(new ItemStack(Items.VILLAGER_SPAWN_EGG, 2));
        ItemStack ammo = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Manager.reward_items.get(1))),64);
        player.getInventory().add(ammo);
    }
}
