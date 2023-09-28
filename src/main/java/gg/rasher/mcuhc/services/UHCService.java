package gg.rasher.mcuhc.services;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import gg.rasher.mcuhc.Plugin;

public class UHCService {
    
    private World uhcWorld;
    private static Plugin plugin = Plugin.getInstance();
    private FileConfiguration config;

    public UHCService(Plugin pluginLatest) {
        plugin = pluginLatest;
    }

    public void countdownAndStart(World world) {

        config = plugin.getConfig();
        uhcWorld = world;
        if (uhcWorld == null) return;

        toggleInvulnerability(true);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);

        world.setGameRule(GameRule.NATURAL_REGENERATION, true);
        world.setGameRule(GameRule.FALL_DAMAGE, false);
        world.setGameRule(GameRule.DROWNING_DAMAGE, false);
        world.setGameRule(GameRule.FIRE_DAMAGE, false);

        world.setTime(0);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

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
            if (countdown > -3600) {
                for (Player player : UHCService.this.uhcWorld.getPlayers()) {
                    if (countdown == 0) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                        player.sendMessage("Game Started!\n You are still invulnerable for another 30 seconds");
                        player.setGameMode(GameMode.SURVIVAL);
                        player.getInventory().clear();
                    }
                    if (countdown > 0) {
                        player.sendMessage("Game starting in: " + countdown + " seconds");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, (countdown < 10 ? 1.5f : 1.0f));
                    }
                    if (countdown == -30) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.5f);
                        player.sendMessage(ChatColor.RED + "Invulnerability has worn off!!!");
                    }
                }
                if (countdown == -30) {
                    toggleInvulnerability(false);
                    uhcWorld.setGameRule(GameRule.DO_FIRE_TICK, true);
                    uhcWorld.setGameRule(GameRule.NATURAL_REGENERATION, false);
                    uhcWorld.setGameRule(GameRule.FALL_DAMAGE, true);
                    uhcWorld.setGameRule(GameRule.DROWNING_DAMAGE, true);
                    uhcWorld.setGameRule(GameRule.FIRE_DAMAGE, true);
                }
                if (countdown == 0) {
                    uhcWorld.setDifficulty(Difficulty.NORMAL);
                    uhcWorld.getWorldBorder().setSize(5, 3000);
                    uhcWorld.setTime(0);
                    uhcWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
                }
                if (countdown == 15) {
                    spreadPlayers(uhcWorld, 0, 0, 400, 475);
                    uhcWorld.getWorldBorder().setSize(1000, 0);
                }
                countdown--;
            } else {
                cancel();
            }
        }
    }

    public void entityDamage(EntityDamageByEntityEvent event) {
        if (isInvulnerabilityEnabled() == false) return;
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    public void playerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        ItemStack goldenApples = new ItemStack(Material.GOLDEN_APPLE, 3);
        player.getWorld().dropItemNaturally(player.getLocation(), goldenApples);
        player.setGameMode(GameMode.SPECTATOR);
    }

    public void playerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        new DelayedTask(player, location).runTaskTimer(plugin, 1, 1);
    }

    private class DelayedTask extends BukkitRunnable {
        private Player player;
        private Location location;
        public DelayedTask(Player player2, Location location2) {
            player = player2;
            location = location2;
        }
        public void run() {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(location);
            cancel();
        }
    }

    private void toggleInvulnerability(boolean newState) {
        config.set("uhcInvulnerability", newState);
        plugin.saveConfig();
    }
    
    private boolean isInvulnerabilityEnabled() {
        config = plugin.getConfig();
        return config.getBoolean("uhcInvulnerability");
    }

    private void spreadPlayers(World world, double centerX, double centerZ, double minDistance, double maxDistance) {
    // Get a list of online players
    for (Player player : world.getPlayers()) {
        double x = centerX + (Math.random() - 0.5) * 2 * maxDistance;
        double z = centerZ + (Math.random() - 0.5) * 2 * maxDistance;

        // Ensure the new location is within the specified minDistance from the center
        double distanceSquared = Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2);
        if (distanceSquared < minDistance * minDistance) {
            double angle = Math.random() * 2 * Math.PI;
            x = centerX + minDistance * Math.cos(angle);
            z = centerZ + minDistance * Math.sin(angle);
        }

        // Set the player's location
        player.teleport(new Location(world, x, world.getHighestBlockYAt((int) x, (int) z), z));
    }
}
}
