package gg.rasher.mcuhc.services;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class HubWorldService {
    private String[] messages = {
        "Ouch!",
        "That hurts!",
        "Owwww",
        "That tickles :3",
        "Hit me harder >:)"
    };
    public boolean configureHubWorld(World world) {
        if (world == null) {
            return false;
        }
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);

        world.setGameRule(GameRule.NATURAL_REGENERATION, true);
        world.setGameRule(GameRule.FALL_DAMAGE, false);
        return true;
    }

    public void playerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        
        if (player.getLocation().getY() < -60 || player.getLocation().getY() > -40) {
            Location spawnLocation = world.getSpawnLocation();
            player.getLocation().setYaw(0);
            player.teleport(spawnLocation);
        }

        player.setSaturation(20.0f);
    }

    public void entityDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        event.setCancelled(true);
        if (entity instanceof Player) return;
        if (damager instanceof Player) {
            damager.sendMessage(ChatColor.LIGHT_PURPLE + "<" + entity.getName() + "> " + ChatColor.WHITE + getRandomMessage());
        }
    }

    private String getRandomMessage() {
        int index = new Random().nextInt(messages.length);
        return messages[index];
    }
}
