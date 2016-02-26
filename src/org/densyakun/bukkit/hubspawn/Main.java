package org.densyakun.bukkit.hubspawn;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import org.densyakun.csvm.CSVFile;

import com.iCo6.iConomy;
import com.iCo6.system.Account;
import com.iCo6.system.Holdings;
import com.iCo6.util.Messaging;
import com.iCo6.util.Template;
public class Main extends JavaPlugin implements Listener{
	public static Main hubspawn;
	public static String msg_prefix;
	public static String msg_cmddisable = ChatColor.RED + "このコマンドは実行できません";
	public Location lobbyspawn;
	public boolean joinspawn = true;
	public boolean firstonly = true;
	public boolean respawn = true;
	public boolean worldsetspawn = true;
	public boolean respawntobed = true;
	public boolean bedteleported = true;
	public boolean spawned = true;
	public double spawncost = 0.0;
	public double bedcost = 0.0;
	public double homecost = 0.0;
	public double sethomecost = 0.0;
	public boolean home = true;
	public int homemax = 0;
	CSVFile homecsv;
	List<Home> homelist;
	List<UUID> spawnban = new ArrayList<UUID>();
	List<UUID> bedban = new ArrayList<UUID>();
	List<UUID> homeban = new ArrayList<UUID>();
	List<UUID> sethomeban = new ArrayList<UUID>();
	List<HubSpawnListener> listeners = new ArrayList<HubSpawnListener>();
	public void onEnable(){
		hubspawn = this;
		msg_prefix = ChatColor.GREEN + "[" + getName() + "]";
		load();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getConsoleSender().sendMessage(msg_prefix + "有効");
	}
	/**Ver 1.7~*/
	public void load() {
		saveDefaultConfig();
		reloadConfig();
		joinspawn = getConfig().getBoolean("joinspawn", true);
		firstonly = getConfig().getBoolean("firstonly", true);
		respawn = getConfig().getBoolean("respawn", true);
		worldsetspawn = getConfig().getBoolean("worldsetspawn", true);
		respawntobed = getConfig().getBoolean("respawntobed", true);
		bedteleported = getConfig().getBoolean("bedteleported", true);
		spawned = getConfig().getBoolean("spawned", true);
		spawncost = getConfig().getDouble("spawncost", 0.0);
		bedcost = getConfig().getDouble("bedcost", 0.0);
		homecost = getConfig().getDouble("homecost", 0.0);
		sethomecost = getConfig().getDouble("sethomecost", 0.0);
		home = getConfig().getBoolean("home", true);
		homemax = getConfig().getInt("homemax", 0);
		getDataFolder().mkdirs();
		homelist = new ArrayList<Home>();
		homecsv = new CSVFile(new File(getDataFolder(), "home.csv"));
		if (homecsv.getFile().exists()) {
			try {
				List<List<String>> data = homecsv.AllRead();
				for (int a = 0; a < data.size(); a++) {
					if (8 <= data.get(a).size()) {
						World b = getServer().getWorld(data.get(a).get(2));
						if (b != null) {
							homelist.add(new Home(UUID.fromString(data.get(a).get(0)), data.get(a).get(1), new Location(b, Double.valueOf(data.get(a).get(3)), Double.valueOf(data.get(a).get(4)), Double.valueOf(data.get(a).get(5)), Float.valueOf(data.get(a).get(6)), Float.valueOf(data.get(a).get(7)))));
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		World lobby = getServer().getWorld(getConfig().getString("world", "world"));
		if (lobby != null) {
			lobbyspawn = new Location(lobby, getConfig().getDouble("x", 0.5), getConfig().getDouble("y", -2), getConfig().getDouble("z", 0.5), (float) getConfig().getDouble("yaw", 0), (float) getConfig().getDouble("pitch", 0));
			if (worldsetspawn) {
				lobby.setSpawnLocation(lobbyspawn.getBlockX(), lobbyspawn.getBlockY(), lobbyspawn.getBlockZ());
			}
		} else {
			getServer().getConsoleSender().sendMessage(msg_prefix + ChatColor.RED + "ワールドが見つかりません");
		}
	}
	/**Ver 1.7~*/
	public void save() {
		saveDefaultConfig();
		getConfig().set("joinspawn", joinspawn);
		getConfig().set("firstonly", firstonly);
		getConfig().set("respawn", respawn);
		getConfig().set("worldsetspawn", worldsetspawn);
		getConfig().set("respawntobed", respawntobed);
		getConfig().set("bedteleported", bedteleported);
		getConfig().set("spawned", spawned);
		getConfig().set("spawncost", spawncost);
		getConfig().set("bedcost", bedcost);
		getConfig().set("homecost", homecost);
		getConfig().set("sethomecost", sethomecost);
		getConfig().set("home", home);
		getConfig().set("homemax", homemax);
		if (lobbyspawn != null) {
			getConfig().set("world", lobbyspawn.getWorld().getName());
			getConfig().set("x", lobbyspawn.getX());
			getConfig().set("y", lobbyspawn.getY());
			getConfig().set("z", lobbyspawn.getZ());
			getConfig().set("yaw", lobbyspawn.getYaw());
			getConfig().set("pitch", lobbyspawn.getPitch());
		}
		saveConfig();
		getDataFolder().mkdirs();
		homecsv = new CSVFile(new File(getDataFolder(), "home.csv"));
		if (!homecsv.getFile().exists()) {
			try {
				homecsv.getFile().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			List<List<String>> data = new ArrayList<List<String>>();
			for (int a = 0; a < homelist.size(); a++) {
				List<String> line = new ArrayList<String>();
				line.add(homelist.get(a).getUuid().toString());
				line.add(homelist.get(a).getName());
				line.add(homelist.get(a).getLocation().getWorld().getName());
				line.add(String.valueOf(homelist.get(a).getLocation().getX()));
				line.add(String.valueOf(homelist.get(a).getLocation().getY()));
				line.add(String.valueOf(homelist.get(a).getLocation().getZ()));
				line.add(String.valueOf(homelist.get(a).getLocation().getYaw()));
				line.add(String.valueOf(homelist.get(a).getLocation().getPitch()));
				data.add(line);
			}
			homecsv.AllWrite(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		load();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (label.equalsIgnoreCase("spawn") || label.equalsIgnoreCase("hub")) {
				if ((1 <= args.length) && sender.isOp() && args[0].equalsIgnoreCase("reload")) {
					load();
					sender.sendMessage(msg_prefix + ChatColor.AQUA + "reloaded.");
				} else {
					sender.sendMessage(msg_prefix + (spawned ? (spawn((Player) sender, spawncost) ? ChatColor.AQUA + "スポーンしました" : ChatColor.RED + "スポーンできません") : msg_cmddisable));
				}
			} else if (label.equalsIgnoreCase("setspawn") && sender.isOp()) {
				if (3 <= args.length) {
					if (4 <= args.length) {
						if (5 <= args.length) {
							if (6 <= args.length) {
								World lobby = getServer().getWorld(args[0]);
								if (lobby != null) {
									lobbyspawn = new Location(((Player) sender).getWorld(), Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3]), Float.valueOf(args[4]), Float.valueOf(args[5]));
								} else {
									getServer().getConsoleSender().sendMessage(msg_prefix + ChatColor.RED + "ワールドが見つかりません");
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
								getServer().getConsoleSender().sendMessage(msg_prefix + ChatColor.RED + "ワールドが見つかりません");
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
				sender.sendMessage(msg_prefix + ChatColor.AQUA + "スポーンを設定しました: " + lobbyspawn);
			} else if (label.equalsIgnoreCase("bed")) {
				if (bedteleported) {
					if (((Player) sender).getBedSpawnLocation() != null) {
						if (bed((Player) sender, bedcost)) {
							sender.sendMessage(msg_prefix + ChatColor.AQUA + "自分のベッドに移動しました");
						} else {
							sender.sendMessage(msg_prefix + ChatColor.RED + "自分のベッドに移動できません");
						}
					} else {
						sender.sendMessage(msg_prefix + ChatColor.RED + "自分のベッドがありません");
					}
				} else {
					sender.sendMessage(msg_prefix + msg_cmddisable);
				}
			} else if (label.equalsIgnoreCase("home")) {
				if (home) {
					String name = 0 < args.length ? args[0] : "";
					Home a = getHome(((Player) sender).getUniqueId(), name);
					if (a != null) {
						if (home((Player) sender, a, homecost)) {
							sender.sendMessage(msg_prefix + ChatColor.AQUA + "ホーム" + (a.getName().isEmpty() ? "" : "\"" + a.getName() + "\"") + "に移動しました");
						} else {
							sender.sendMessage(msg_prefix + ChatColor.RED + "ホーム" + (a.getName().isEmpty() ? "" : "\"" + a.getName() + "\"") + "に移動できません");
						}
					} else {
						sender.sendMessage(msg_prefix + ChatColor.RED + "指定したホームがありません");
					}
				} else {
					sender.sendMessage(msg_prefix + msg_cmddisable);
				}
			} else if (label.equalsIgnoreCase("sethome")) {
				if (home) {
					if (issethomeban(((Player) sender).getUniqueId())) {
						sender.sendMessage(msg_prefix + ChatColor.RED + "ホームを設定できません");
					} else {
						String name = 1 <= args.length ? args[0] : "";
						if ((name.indexOf(',') == -1) && (name.indexOf('[') == -1) && (name.indexOf(']') == -1)) {
							if (getHome(((Player) sender).getUniqueId(), name) == null ? homemax <= 0 || getHomes(((Player) sender).getUniqueId()).size() < homemax : true) {
								if (sethome((Player) sender, name, sethomecost)) {
									sender.sendMessage(msg_prefix + ChatColor.AQUA + "ホーム" + (name.isEmpty() ? "" : "\"" + name + "\"") + "を設定しました");
								} else {
									sender.sendMessage(msg_prefix + ChatColor.RED + "ホームを設定できません");
								}
							} else {
								sender.sendMessage(msg_prefix + ChatColor.RED + "ホームがいっぱいです");
							}
						} else {
							sender.sendMessage(msg_prefix + ChatColor.RED + "ホームの名前に',', '[', ']'は使用できません");
						}
					}
				} else {
					sender.sendMessage(msg_prefix + msg_cmddisable);
				}
			} else if (label.equalsIgnoreCase("homelist")) {
				if (home) {
					if (issethomeban(((Player) sender).getUniqueId())) {
						sender.sendMessage(msg_prefix + ChatColor.RED + "ホームを設定できません");
					} else {
						List<Home> homes = getHomes(((Player) sender).getUniqueId());
						int page = 1;
						if (1 <= args.length) {
							try {
								page = Integer.valueOf(args[0]).intValue();
								if (homes.size() + 3 <= 3 * page) {
									page = (int) Math.ceil((double) homes.size() / 3);
								}
								if (page < 1) {
									page = 1;
								}
							} catch (NumberFormatException e) {
							}
						}
						sender.sendMessage(msg_prefix + ChatColor.AQUA + "ホーム一覧" + (homes.size() == 0 ? ": " + ChatColor.GRAY + "なし" : "(ページ:" + page + "/" + (int) Math.ceil((double) homes.size() / 3) + "):"));
						for (int a = 3 * (page - 1); (a < 3 * (page - 1) + 3) && (a < homes.size()); a++) {
							Home home = homes.get(a);
							sender.sendMessage(msg_prefix + ChatColor.AQUA + "|" + ChatColor.GOLD + "(" + (a + 1) + ") Name: " + (home.getName().isEmpty() ? ChatColor.GRAY + "(Main)" : ChatColor.WHITE + home.getName()) + ChatColor.GOLD + " Location: " + ChatColor.WHITE + home.getLocation() + ChatColor.AQUA + "|");
						}
					}
				} else {
					sender.sendMessage(msg_prefix + msg_cmddisable);
				}
			} else if (label.equalsIgnoreCase("removehome")) {
				if (home) {
					String name = 0 < args.length ? args[0] : "";
					Home a = getHome(((Player) sender).getUniqueId(), name);
					if (a != null) {
						if (removehome(a)) {
							homelist.remove(a);
							save();
							sender.sendMessage(msg_prefix + ChatColor.AQUA + "ホーム" + (a.getName().isEmpty() ? "" : "\"" + a.getName() + "\"") + "を削除しました");
						} else {
							sender.sendMessage(msg_prefix + ChatColor.AQUA + "ホームを削除できません");
						}
					} else {
						sender.sendMessage(msg_prefix + ChatColor.RED + "指定したホームがありません");
					}
				} else {
					sender.sendMessage(msg_prefix + msg_cmddisable);
				}
			}
		}
		return true;
	}
	/**Ver 1.7~*/
	public boolean spawn(Player player, double cost) {
		if (!isspawnban(player.getUniqueId())) {
			boolean a = true;
			if (getServer().getPluginManager().getPlugin("iConomy") != null && 0.1 <= cost) {
				Holdings holdings = new Account(player.getName()).getHoldings();
				if (cost <= holdings.getBalance().doubleValue()) {
					holdings.subtract(cost);
					iConomy.Template.set(Template.Node.PLAYER_DEBIT);
					iConomy.Template.add("name", player.getName());
					iConomy.Template.add("amount", iConomy.format(cost));
				} else {
					iConomy.Template.set(Template.Node.ERROR_FUNDS);
					a = false;
				}
				Messaging.send(player, iConomy.Template.color(Template.Node.TAG_MONEY) + iConomy.Template.parse());
			}
			if (a && player.teleport(lobbyspawn)) {
				for (int b = 0; b < listeners.size(); b++) {
					listeners.get(b).spawn(this, player);
				}
				return true;
			}
		}
		return false;
	}
	/**Ver 1.7~*/
	public boolean bed(Player player, double cost) {
		if (!isbedban(player.getUniqueId()) && player.getBedSpawnLocation() != null) {
			boolean a = true;
			if (getServer().getPluginManager().getPlugin("iConomy") != null && 0.1 <= cost) {
				Holdings holdings = new Account(player.getName()).getHoldings();
				if (cost <= holdings.getBalance().doubleValue()) {
					holdings.subtract(cost);
					iConomy.Template.set(Template.Node.PLAYER_DEBIT);
					iConomy.Template.add("name", player.getName());
					iConomy.Template.add("amount", iConomy.format(cost));
				} else {
					iConomy.Template.set(Template.Node.ERROR_FUNDS);
					a = false;
				}
				Messaging.send(player, iConomy.Template.color(Template.Node.TAG_MONEY) + iConomy.Template.parse());
			}
			if (a && player.teleport(player.getBedSpawnLocation())) {
				for (int b = 0; b < listeners.size(); b++) {
					listeners.get(b).bed(this, player);
				}
				return true;
			}
		}
		return false;
	}
	/**Ver 1.7~*/
	public boolean home(Player player, Home home, double cost) {
		if (!ishomeban(player.getUniqueId())) {
			boolean a = true;
			if (getServer().getPluginManager().getPlugin("iConomy") != null && 0.1 <= cost) {
				Holdings holdings = new Account(player.getName()).getHoldings();
				if (cost <= holdings.getBalance().doubleValue()) {
					holdings.subtract(cost);
					iConomy.Template.set(Template.Node.PLAYER_DEBIT);
					iConomy.Template.add("name", player.getName());
					iConomy.Template.add("amount", iConomy.format(cost));
				} else {
					iConomy.Template.set(Template.Node.ERROR_FUNDS);
					a = false;
				}
				Messaging.send(player, iConomy.Template.color(Template.Node.TAG_MONEY) + iConomy.Template.parse());
			}
			if (a && player.teleport(home.getLocation())) {
				for (int b = 0; b < listeners.size(); b++) {
					listeners.get(b).home(this, player, home);
				}
				return true;
			}
		}
		return false;
	}
	/**Ver 1.7~*/
	public boolean sethome(Player player, String name, double cost) {
		if (!issethomeban(player.getUniqueId())) {
			boolean a = true;
			if (getServer().getPluginManager().getPlugin("iConomy") != null && 0.1 <= cost) {
				Holdings holdings = new Account(player.getName()).getHoldings();
				if (cost <= holdings.getBalance().doubleValue()) {
					holdings.subtract(cost);
					iConomy.Template.set(Template.Node.PLAYER_DEBIT);
					iConomy.Template.add("name", player.getName());
					iConomy.Template.add("amount", iConomy.format(cost));
				} else {
					iConomy.Template.set(Template.Node.ERROR_FUNDS);
					a = false;
				}
				Messaging.send(player, iConomy.Template.color(Template.Node.TAG_MONEY) + iConomy.Template.parse());
			}
			if (a) {
				Home b = getHome(player.getUniqueId(), name);
				if (b != null) {
					b.setLocation(player.getLocation());
				} else {
					homelist.add(b = new Home(player.getUniqueId(), name, player.getLocation()));
				}
				save();
				return true;
			}
		}
		return false;
	}
	/**Ver 1.7~*/
	public boolean removehome(Home home) {
		for (int a = 0; a < homelist.size(); a++) {
			if (homelist.get(a).equals(home)) {
				homelist.remove(a);
				return true;
			}
		}
		return false;
	}
	/**Ver 1.7~*/
	public void addspawnban(UUID uuid) {
		if (!isspawnban(uuid)) {
			spawnban.add(uuid);
		}
	}
	/**Ver 1.7~*/
	public boolean isspawnban(UUID uuid) {
		for (int a = 0; a < spawnban.size(); a++) {
			if (spawnban.get(a).equals(uuid)) {
				return true;
			}
		}
		return false;
	}
	/**Ver 1.7~*/
	public void removespawnban(UUID uuid) {
		for (int a = 0; a < spawnban.size(); a++) {
			if (spawnban.get(a).equals(uuid)) {
				spawnban.remove(a);
				break;
			}
		}
	}
	/**Ver 1.7~*/
	public void addbedban(UUID uuid) {
		if (!isbedban(uuid)) {
			bedban.add(uuid);
		}
	}
	/**Ver 1.7~*/
	public boolean isbedban(UUID uuid) {
		for (int a = 0; a < bedban.size(); a++) {
			if (bedban.get(a).equals(uuid)) {
				return true;
			}
		}
		return false;
	}
	/**Ver 1.7~*/
	public void removebedban(UUID uuid) {
		for (int a = 0; a < bedban.size(); a++) {
			if (bedban.get(a).equals(uuid)) {
				bedban.remove(a);
				break;
			}
		}
	}
	/**Ver 1.7~*/
	public void addhomeban(UUID uuid) {
		if (!ishomeban(uuid)) {
			homeban.add(uuid);
		}
	}
	/**Ver 1.7~*/
	public boolean ishomeban(UUID uuid) {
		for (int a = 0; a < homeban.size(); a++) {
			if (homeban.get(a).equals(uuid)) {
				return true;
			}
		}
		return false;
	}
	/**Ver 1.7~*/
	public void removehomeban(UUID uuid) {
		for (int a = 0; a < homeban.size(); a++) {
			if (homeban.get(a).equals(uuid)) {
				homeban.remove(a);
				break;
			}
		}
	}
	/**Ver 1.7~*/
	public void addsethomeban(UUID uuid) {
		if (!issethomeban(uuid)) {
			sethomeban.add(uuid);
		}
	}
	/**Ver 1.7~*/
	public boolean issethomeban(UUID uuid) {
		for (int a = 0; a < sethomeban.size(); a++) {
			if (sethomeban.get(a).equals(uuid)) {
				return true;
			}
		}
		return false;
	}
	/**Ver 1.7~*/
	public void removesethomeban(UUID uuid) {
		for (int a = 0; a < sethomeban.size(); a++) {
			if (sethomeban.get(a).equals(uuid)) {
				sethomeban.remove(a);
				break;
			}
		}
	}
	/**Ver 1.7~*/
	public List<Home> getHomes(UUID uuid) {
		List<Home> a = new ArrayList<Home>();
		for (int b = 0; b < homelist.size(); b++) {
			if (uuid.equals(homelist.get(b).getUuid())) {
				a.add(homelist.get(b));
			}
		}
		return a;
	}
	/**Ver 1.7~*/
	public Home getHome(UUID uuid, String name) {
		for (int a = 0; a < homelist.size(); a++) {
			if (uuid.equals(homelist.get(a).getUuid()) && name.equalsIgnoreCase(homelist.get(a).getName())) {
				return homelist.get(a);
			}
		}
		return null;
	}
	/**Ver 1.7~*/
	public void addHubSpawnListener(HubSpawnListener listener) {
		listeners.add(listener);
	}
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e) {
		if (joinspawn && ((e.getPlayer().getLastPlayed() == 0) || !firstonly)) {
			spawn(e.getPlayer(), 0);
		}
	}
	@EventHandler
	public void PlayerRespawn(PlayerRespawnEvent e) {
		if ((lobbyspawn != null) && respawn && (e.getPlayer().getBedSpawnLocation() != null ? !respawntobed : true)) {
			e.setRespawnLocation(lobbyspawn);
		}
	}
}
