package gg.rasher.mcuhc.services;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import gg.rasher.mcuhc.Plugin;

public class UHCService {
    
    private static UHCService instance;
    public boolean invunerability;
    public World uhcWorld;
    private Plugin plugin = Plugin.getInstance();

    public void countdownAndStart(World world) {
        
        this.invunerability = true;
        this.uhcWorld = world;
        if (this.uhcWorld == null) return;
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);

        world.setGameRule(GameRule.NATURAL_REGENERATION, true);
        world.setGameRule(GameRule.FALL_DAMAGE, false);
        world.setGameRule(GameRule.DROWNING_DAMAGE, false);
        world.setGameRule(GameRule.FIRE_DAMAGE, false);

        for (Entity entity : world.getEntities()) {
            if (entity instanceof Creeper) entity.remove();
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("Sending you to the UHC world...");
            player.teleport(world.getSpawnLocation());
            player.setGameMode(GameMode.ADVENTURE);
        }

        new CountdownTask().runTaskTimer(plugin, 0L, 20L);
    }

    private class CountdownTask extends BukkitRunnable {
        private int countdown = 30;
        public void run() {
            if (countdown > 0) {
                for (Player player : UHCService.this.uhcWorld.getPlayers()) {
                    player.sendMessage("Countdown: " + countdown + " seconds");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                }
                countdown--;
            } else {
                cancel();
            }
        }
    }

    public void entityDamage(EntityDamageByEntityEvent event) {
        if (this.invunerability == false) {
            return;
        }
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }
    
}
