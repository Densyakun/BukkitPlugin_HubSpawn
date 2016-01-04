package org.densyakun.bukkit.hubspawn;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin implements Listener{
	public Location lobbyspawn;
	public boolean joinspawn = true;
	public boolean firstonly = true;
	public boolean respawn = true;
	public void onEnable(){
		saveDefaultConfig();
		firstonly = getConfig().getBoolean("firstonly", true);
		World lobby = getServer().getWorld(getConfig().getString("world", "world"));
		if (lobby != null) {
			getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[" + getName() + "] SpawnLocation: " + (lobbyspawn = new Location(lobby, getConfig().getDouble("x", 0.5), getConfig().getDouble("y", -2), getConfig().getDouble("z", 0.5), (float) getConfig().getDouble("yaw", 0), (float) getConfig().getDouble("pitch", 0))).toString());
			lobby.setSpawnLocation(lobbyspawn.getBlockX(), lobbyspawn.getBlockY(), lobbyspawn.getBlockZ());
		} else {
			getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[" + getName() + "] " + ChatColor.RED + "ワールドが見つかりません");
		}
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[" + getName() + "]有効");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if ((sender instanceof Player) && (command.getName().equalsIgnoreCase("spawn") || command.getName().equalsIgnoreCase("hub"))) {
			spawn((Player) sender);
		}
		return true;
	}
	public boolean spawn(Player player) {
		if (lobbyspawn != null) {
			return player.teleport(lobbyspawn);
		}
		return false;
	}
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e) {
		if (joinspawn && ((e.getPlayer().getLastPlayed() == 0) || !firstonly)) {
			spawn(e.getPlayer());
		}
	}
	@EventHandler
	public void PlayerRespawn(PlayerRespawnEvent e) {
		if (respawn) {
			spawn(e.getPlayer());
		}
	}
}
