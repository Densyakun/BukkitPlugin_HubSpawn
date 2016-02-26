package org.densyakun.bukkit.hubspawn;
import java.util.UUID;

import org.bukkit.Location;
/**Ver 1.7~*/
public class Home {
	private UUID uuid;
	private String name;
	private Location location;
	public Home(UUID uuid, String name, Location location) {
		this.uuid = uuid;
		this.name = name;
		this.location = location;
	}
	public UUID getUuid() {
		return uuid;
	}
	public String getName() {
		return name;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	@Override
	public boolean equals(Object obj) {
		return this instanceof Home ? super.equals(obj) && uuid.equals(((Home) obj).uuid) && name.equals(((Home) obj).name) && location.equals(((Home) obj).location) : false;
	}
}
