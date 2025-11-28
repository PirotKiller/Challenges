package me.PirotKiller.challenges.Days;

import me.PirotKiller.challenges.Managers.Manager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class Day82 {
    public void execute(ServerLevel overworld, BlockPos pos, ServerPlayer player){
        Manager.sendTitleToPlayer(player, Component.literal("TRAP TAXMAN."), Component.literal(""), 10, 70, 20);

        spawnTrapTaxman(overworld,pos,Manager.isPlayer_has_mystery_object());
        if (Manager.isPlayer_has_mystery_object()) {

            Manager.spawnParticles(overworld, ParticleTypes.ENCHANT, pos.east(10), 200, 5.0);

            Manager.playSound(overworld, player.getOnPos(), SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0f, 1.5f);
        } else {

            Manager.playSound(overworld, player.getOnPos(), SoundEvents.ENDER_DRAGON_GROWL, 1.0f, 1.0f);
        }
    }


    public static void spawnTrapTaxman(ServerLevel world, BlockPos pos, boolean isTrapped) {
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(Manager.MOB_IDS.get(4)));

        if (entityType == null){
            System.out.println("ERROR: Cannot find mob ID");
            return;
        }
        net.minecraft.world.entity.Entity entity = entityType.create(world);
        if (entity instanceof net.minecraft.world.entity.Mob mob) {
            mob.setPos(pos.getX() + 0.5+10, pos.getY(), pos.getZ() + 0.5);
            mob.setPersistenceRequired();
            if (isTrapped) {
                mob.setNoAi(true);
            }
            world.addFreshEntity(mob);
        }

    }
}
