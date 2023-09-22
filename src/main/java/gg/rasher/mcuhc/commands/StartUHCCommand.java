package gg.rasher.mcuhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import gg.rasher.mcuhc.Plugin;
import gg.rasher.mcuhc.services.UHCService;

public class StartUHCCommand implements CommandExecutor {

    private UHCService uhcService;

    public StartUHCCommand(Plugin plugin) {
        this.uhcService = new UHCService(plugin);
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 1) {
            sender.sendMessage("Usage: /startuhc <world_name>");
            return true;
        }

        World world = Bukkit.getWorld(args[0]);

        if (world == null) {
            sender.sendMessage("Could not find world");
            return true;
        }

        uhcService.countdownAndStart(world);

        return true;
    }
}