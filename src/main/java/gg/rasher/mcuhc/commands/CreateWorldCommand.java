package gg.rasher.mcuhc.commands;

import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateWorldCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("Usage: /createworld <world_name>");
            return true;
        }

        String worldName = args[0];

        player.sendMessage("Creating new world named " + worldName);

        // Create a new world with the specified name
        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.environment(Environment.NORMAL).createWorld();

        // TODO: make nether not share between worlds to be able to have nether in game
        // WorldCreator netherCreator = new WorldCreator(worldName + "_nether");
        // netherCreator.environment(Environment.NETHER).createWorld();

        player.sendMessage("A new world named '" + worldName + "' has been created.");

        return true;
    }
}
