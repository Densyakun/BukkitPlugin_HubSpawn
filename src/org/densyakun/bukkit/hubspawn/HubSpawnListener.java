package org.densyakun.bukkit.hubspawn;
import org.bukkit.entity.Player;
/**Ver 1.7~*/
public interface HubSpawnListener {
	public void spawn(Main main, Player player);
	public void bed(Main main, Player player);
	public void home(Main main, Player player, Home home);
}
