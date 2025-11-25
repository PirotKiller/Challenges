package me.PirotKiller.challenges.Days;

import me.PirotKiller.challenges.Managers.Manager;
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
        BlockPos wardenPos = player.getOnPos().offset(0, 0, 20); // 20 blocks away
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(Manager.MOB_IDS.get(5)));
        entityType.spawn(overworld, wardenPos, MobSpawnType.NATURAL);
        player.sendSystemMessage(Component.literal("Corrupt Champion is coming."));

    }
}
