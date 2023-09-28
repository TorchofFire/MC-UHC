package gg.rasher.mcuhc.listeners;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import gg.rasher.mcuhc.services.UHCService;

public class OnPlayerRespawnListener implements Listener {

    private final UHCService uhcService = new UHCService(null);

    @EventHandler
    public void OnPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (world.getName().startsWith("uhc-") ) {
            uhcService.playerRespawn(event);
            return;
        }
        
    }
    
}
