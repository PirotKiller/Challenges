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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;

public class Day82 {
    public static void execute(ServerLevel overworld, BlockPos pos, ServerPlayer player){
        Manager.sendTitleToPlayer(player, Component.literal("TRAP TAXMAN.").withStyle(ChatFormatting.DARK_RED), Component.literal(""), 10, 70, 20);

        // Spawn Taxman normally (he will be trapped later if player throws the item)
        spawnTaxman(overworld, pos);

        Manager.playSound(overworld, player.getOnPos(), SoundEvents.ENDER_DRAGON_GROWL, 1.0f, 1.0f);
        player.sendSystemMessage(Component.literal("The Taxman is here... Throw the Mystery Object to trap him!").withStyle(ChatFormatting.RED));
    }


    public static void spawnTaxman(ServerLevel world, BlockPos pos) {
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(Manager.MOB_IDS.get(6)));

        if (entityType == null){
            System.out.println("ERROR: Cannot find mob ID");
            return;
        }
        net.minecraft.world.entity.Entity entity = entityType.create(world);
        if (entity instanceof Mob mob) {
            // Spawn him offset by 10 blocks (center of the trap area)
            mob.setPos(pos.getX() + 0.5 + 10, pos.getY(), pos.getZ() + 0.5);
            mob.setPersistenceRequired(); // Ensure he doesn't despawn
            world.addFreshEntity(mob);
        }
    }
}
