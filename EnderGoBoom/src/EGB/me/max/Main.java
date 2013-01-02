package EGB.me.max;

import java.io.File;
import java.util.Random;
import java.util.logging.Logger;


import org.bukkit.Location;
import org.bukkit.entity.Enderman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public final Logger logger = Logger.getLogger("Minecraft");
	 
	public void onEnable() {
	
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Enabled!");
		this.getServer().getPluginManager().registerEvents(this, this);

	}
			
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");
	}
	@EventHandler
	public void onMobDeath(EntityDeathEvent e) {
		if(e.getEntity() instanceof Enderman) {
			Enderman ender = (Enderman) e.getEntity();
			Random r   = new Random();
			Location l = ender.getLocation();
			int chance = r.nextInt(9);
			if(chance == 0)
				ender.getWorld().createExplosion(l, 4);
		}
	}

}
