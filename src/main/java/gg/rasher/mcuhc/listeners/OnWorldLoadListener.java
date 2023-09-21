package gg.rasher.mcuhc.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class OnWorldLoadListener implements Listener {

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        // Check if the loaded world is the default world named "world"
        World world = event.getWorld();
      
    }
}
