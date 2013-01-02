package me.max;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;




import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class bd extends JavaPlugin {
	public final Logger logger = Logger.getLogger("Minecraft");
    public static Permission perm = null;
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            perm = permissionProvider.getProvider();
        }
        return (perm != null);
       
    }
    

   
    public void onEnable() {
    	PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Enabled!");
		this.getServer().getPluginManager().registerEvents(new bdl(this), this);
		setupPermissions();
		File config = new File(this.getDataFolder(), "config.yml");

		if(!config.exists()){
			this.saveDefaultConfig();
		}
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
	           public void run() {
	        	   checkMap(bdl.placeDelay);
	        	   checkMap(bdl.pearlDelay);
	        	   checkMap(bdl.eatDelay);
	           }
	    }, 0, 20);
		String you = getConfig().getString("Descriptions.you");

	}
    public void checkMap(Map<String, Integer> map) {
    	for(String p : map.keySet()) {
    		if(map.get(p) == 0){
    			map.remove(p);
    		}else{
    			int oldValue = map.get(p);
    			map.remove(p);
    			map.put(p, oldValue - 20);
    		}
    	}
    }
    public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");
	}
   
    
}
