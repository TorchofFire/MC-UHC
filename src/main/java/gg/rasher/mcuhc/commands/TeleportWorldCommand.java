package gg.rasher.mcuhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportWorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        // Check if the correct number of arguments are provided
        if (args.length != 1) {
            player.sendMessage("Usage: /teleportworld <world_name>");
            return true;
        }

        String worldName = args[0];

        // Teleport the player to the specified world
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage("Could not find \"" + worldName + "\"");
            return true;
        }
        player.teleport(world.getSpawnLocation());
        player.sendMessage("You have been teleported to the world: " + worldName);

        return true;
    }
}
