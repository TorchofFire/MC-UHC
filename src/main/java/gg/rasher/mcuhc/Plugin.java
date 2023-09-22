package gg.rasher.mcuhc;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import gg.rasher.mcuhc.commands.UHCCreateWorldCommand;
import gg.rasher.mcuhc.commands.GiveStickCommand;
import gg.rasher.mcuhc.commands.StartUHCCommand;
import gg.rasher.mcuhc.commands.TeleportWorldCommand;
import gg.rasher.mcuhc.listeners.OnEntityDamageListener;
import gg.rasher.mcuhc.listeners.OnPlayerMoveListener;
import gg.rasher.mcuhc.listeners.OnWorldLoadListener;
import gg.rasher.mcuhc.services.HubWorldService;

/*
 * mcuhc java plugin
 */
public class Plugin extends JavaPlugin {

  private static Plugin instance;
  private static Logger LOGGER=Logger.getLogger("mcuhc");
  private OnWorldLoadListener onWorldLoadListener = new OnWorldLoadListener();
  private OnPlayerMoveListener onPlayerMoveListener = new OnPlayerMoveListener();
  private OnEntityDamageListener onEntityDamageListener = new OnEntityDamageListener(this);
  private HubWorldService hubWorldService = new HubWorldService();
  public static String hubWorld = "UHC hub";

  private FileConfiguration config;

  public static Plugin getInstance() {
    return instance;
  }

  @Override
  public void onEnable()
  {
    instance = this;
    LOGGER.info("mcuhc enabled");

    config = getConfig();
    config.addDefault("uhcInvulnerability", false);

    // set hub world to act like a hub
    if (hubWorldService.configureHubWorld(Bukkit.getWorld(hubWorld)) == false) LOGGER.warning("hub could not be configured");

    // Register our events
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(onWorldLoadListener, this);
    pm.registerEvents(onPlayerMoveListener, this);
    pm.registerEvents(onEntityDamageListener, this);

    // Register our commands
    getCommand("givestick").setExecutor(new GiveStickCommand());
    getCommand("uhccreateworld").setExecutor(new UHCCreateWorldCommand());
    getCommand("tpworld").setExecutor(new TeleportWorldCommand());
    getCommand("startuhc").setExecutor(new StartUHCCommand(this));
  }

  public void onDisable()
  {
    LOGGER.info("mcuhc disabled");
  }
}
