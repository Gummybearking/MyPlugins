package me.max.nlh;

import java.io.File;
import java.util.logging.Logger;


import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class nlh extends JavaPlugin {
	public final Logger logger = Logger.getLogger("Minecraft");
    public static Permission permission = null;
    
    

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    public void onEnable() {
    	PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Enabled!");
		setupPermissions();
		getServer().getPluginManager().registerEvents(new  nlhl(this), this);

		File config = new File(this.getDataFolder(), "config.yml");
		if(!config.exists()){
			this.saveDefaultConfig();
			System.out.println("[NoLightHax] No config.yml detected, config.yml created");

		}

	}
		
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");
	}
	public int getConfigLightLevel(){
		return getConfig().getInt("MinLightLevel");
	}
	public int getConfigChance(){
		return getConfig().getInt("chance");
			
		
	}
}
