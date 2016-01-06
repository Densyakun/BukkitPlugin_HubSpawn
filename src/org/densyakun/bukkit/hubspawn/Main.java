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
	public boolean worldsetspawn = true;
	public void onEnable(){
		load();
		save();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[" + getName() + "]有効");
	}
	void load() {
		saveDefaultConfig();
		joinspawn = getConfig().getBoolean("joinspawn", true);
		firstonly = getConfig().getBoolean("firstonly", true);
		respawn = getConfig().getBoolean("respawn", true);
		worldsetspawn = getConfig().getBoolean("worldsetspawn", true);
		World lobby = getServer().getWorld(getConfig().getString("world", "world"));
		if (lobby != null) {
			getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[" + getName() + "] SpawnLocation: " + (lobbyspawn = new Location(lobby, getConfig().getDouble("x", 0.5), getConfig().getDouble("y", -2), getConfig().getDouble("z", 0.5), (float) getConfig().getDouble("yaw", 0), (float) getConfig().getDouble("pitch", 0))).toString());
			if (worldsetspawn) {
				lobby.setSpawnLocation(lobbyspawn.getBlockX(), lobbyspawn.getBlockY(), lobbyspawn.getBlockZ());
			}
		} else {
			getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[" + getName() + "] " + ChatColor.RED + "ワールドが見つかりません");
		}
	}
	void save() {
		saveDefaultConfig();
		getConfig().set("joinspawn", joinspawn);
		getConfig().set("firstonly", firstonly);
		getConfig().set("respawn", respawn);
		getConfig().set("worldsetspawn", worldsetspawn);
		if (lobbyspawn != null) {
			getConfig().set("world", lobbyspawn.getWorld().getName());
			getConfig().set("x", lobbyspawn.getX());
			getConfig().set("y", lobbyspawn.getY());
			getConfig().set("z", lobbyspawn.getZ());
			getConfig().set("yaw", lobbyspawn.getYaw());
			getConfig().set("pitch", lobbyspawn.getPitch());
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (command.getName().equalsIgnoreCase("spawn") || command.getName().equalsIgnoreCase("hub")) {
				spawn((Player) sender);
			} else if (command.getName().equalsIgnoreCase("setspawn")) {
				if (3 <= args.length) {
					if (4 <= args.length) {
						if (5 <= args.length) {
							if (6 <= args.length) {
								World lobby = getServer().getWorld(args[0]);
								if (lobby != null) {
									lobbyspawn = new Location(((Player) sender).getWorld(), Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3]), Float.valueOf(args[4]), Float.valueOf(args[5]));
								} else {
									getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[" + getName() + "] " + ChatColor.RED + "ワールドが見つかりません");
									return true;
								}
							} else {
								lobbyspawn = new Location(((Player) sender).getWorld(), Double.valueOf(args[0]), Double.valueOf(args[1]), Double.valueOf(args[2]), Float.valueOf(args[3]), Float.valueOf(args[4]));
							}
						} else {
							World lobby = getServer().getWorld(args[0]);
							if (lobby != null) {
								lobbyspawn = new Location(((Player) sender).getWorld(), Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3]));
							} else {
								getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[" + getName() + "] " + ChatColor.RED + "ワールドが見つかりません");
								return true;
							}
						}
					} else {
						lobbyspawn = new Location(((Player) sender).getWorld(), Double.valueOf(args[0]), Double.valueOf(args[1]), Double.valueOf(args[2]));
					}
				} else {
					lobbyspawn = ((Player) sender).getLocation();
				}
				save();
				load();
				sender.sendMessage(ChatColor.GREEN + "[" + getName() + "] " + ChatColor.AQUA + "スポーンを設定しました: " + lobbyspawn);
			}
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
		if ((lobbyspawn != null) && respawn) {
			e.setRespawnLocation(lobbyspawn);
		}
	}
}
