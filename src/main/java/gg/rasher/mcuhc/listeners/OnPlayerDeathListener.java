package gg.rasher.mcuhc.listeners;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import gg.rasher.mcuhc.services.UHCService;

public class OnPlayerDeathListener implements Listener {

    private final UHCService uhcService = new UHCService(null);

    @EventHandler
    public void OnPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        World world = player.getWorld();

        if (world.getName().startsWith("uhc-") ) {
            uhcService.playerDeath(event);
            return;
        }
        
    }
    
}
