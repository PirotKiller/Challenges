package me.PirotKiller.challenges.Days;

import me.PirotKiller.challenges.Managers.Manager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.registries.ForgeRegistries;

public class Day95 {
    public void execute(ServerLevel overworld, BlockPos pos, ServerPlayer player){
        Manager.sendTitleToPlayer(player,
                Component.literal("☠ THE CHAMPION ☠").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD),
                Component.literal("He is here.").withStyle(ChatFormatting.GRAY),
                10, 70, 20);

        player.sendSystemMessage(Component.literal("Corrupt Champion is coming.")
                .withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));

        BlockPos wardenPos = player.getOnPos().offset(0, 0, 20); // 20 blocks away
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(Manager.MOB_IDS.get(5)));

        if (entityType != null) {
            entityType.spawn(overworld, wardenPos, MobSpawnType.NATURAL);
        }

    }
}
