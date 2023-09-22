package gg.rasher.mcuhc.listeners;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import gg.rasher.mcuhc.Plugin;
import gg.rasher.mcuhc.services.HubWorldService;
import gg.rasher.mcuhc.services.UHCService;

public class OnEntityDamageListener implements Listener {

    private HubWorldService hubWorldService = new HubWorldService();
    private UHCService uhcService;

    public OnEntityDamageListener(Plugin plugin) {
        this.uhcService = new UHCService(plugin);
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        World world = entity.getWorld();

        if (world.getName().equals(Plugin.hubWorld)) {
            hubWorldService.entityDamage(event);
            return;
        }
        if (world.getName().startsWith("uhc-") ) {
            uhcService.entityDamage(event);
            return;
        }
    }
}
