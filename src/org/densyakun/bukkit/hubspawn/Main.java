package org.densyakun.bukkit.hubspawn;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin implements Listener{
	World world;
	public void onEnable(){
		saveDefaultConfig();
		world = getServer().getWorld(getConfig().getString("world", "world"));
	}
	@Override
	public boolean onCommand(CommandSender sender,Command command,String label,String[]args){
		if(command.getName().equalsIgnoreCase("spawn") || command.getName().equalsIgnoreCase("hub")){
			if (sender instanceof Player) {
				if (world != null) {
					((Player) sender).teleport(world.getSpawnLocation());
				} else {
					sender.sendMessage(ChatColor.GOLD + "[HubSpawn]World: " + world != null ? world.getName() : null);
				}
			} else 	if (sender instanceof ConsoleCommandSender) {
				getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[HubSpawn]World: " + world != null ? world.getName() : null);
			} else {
				sender.sendMessage("[HubSpawn]World: " + world != null ? world.getName() : null);
			}
		}
		return true;
	}
}
