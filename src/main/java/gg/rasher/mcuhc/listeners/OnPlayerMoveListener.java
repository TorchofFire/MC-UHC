package gg.rasher.mcuhc.listeners;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import gg.rasher.mcuhc.Plugin;
import gg.rasher.mcuhc.services.HubWorldService;

public class OnPlayerMoveListener implements Listener {

    private final HubWorldService hubWorldService = new HubWorldService();

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (world.getName().equals(Plugin.hubWorld)) {
            hubWorldService.playerMove(event);
        }
    }
}
