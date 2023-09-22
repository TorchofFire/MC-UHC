package gg.rasher.mcuhc.services;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
                    Bukkit.getServer().dispatchCommand(Bukkit.getPlayerExact("Torch_of_Fire"), String.format("worldborder set 5 3000"));
                    Bukkit.getServer().dispatchCommand(Bukkit.getPlayerExact("Torch_of_Fire"), String.format("time set day"));
                }
                if (countdown == 15) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getPlayerExact("Torch_of_Fire"), String.format("spreadplayers 0 0 450 500 true @a"));
                    Bukkit.getServer().dispatchCommand(Bukkit.getPlayerExact("Torch_of_Fire"), String.format("worldborder set 1000 0"));
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

    private void toggleInvulnerability(boolean newState) {
        config.set("uhcInvulnerability", newState);
        plugin.saveConfig();
    }
    
    private boolean isInvulnerabilityEnabled() {
        config = plugin.getConfig();
        return config.getBoolean("uhcInvulnerability");
    }
}
