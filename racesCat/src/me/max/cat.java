package me.max;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class cat extends JavaPlugin{
	public final Logger logger = Logger.getLogger("Minecraft");
 
	public void onEnable() {
	
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Enabled!");
		utils.setupPermissions();
		this.getServer().getPluginManager().registerEvents(new catL(this), this);
		File config = new File(this.getDataFolder(), "config.yml");
		if(!config.exists()) {
			this.saveDefaultConfig();
			System.out.println("[Races] Generating a config for the race: Cat");
		}

			

	}
	
			
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");
	}
}
