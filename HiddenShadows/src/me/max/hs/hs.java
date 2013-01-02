package me.max.hs;

import java.util.logging.Logger;


import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class hs extends JavaPlugin {
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
    	getConfig().options().copyDefaults(true);
		saveConfig();
    	PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Enabled!");
		setupPermissions();
		getServer().getPluginManager().registerEvents(new hsl(this), this);
		
		
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

			public void run() {
				for(Player p : hsl.hiddenInLight.keySet()){
					Long v = hsl.hidden.get(p);
					hsl.hidden.put(p, v - 1);
					msg(p, "You have " +v+ " seconds to get to a dark place!");
					if(v <= 0){
						hsl.hiddenInLight.remove(p);
						hsl.hidden.remove(p);
						msg(p, "You have become visible");
						for(Player player : getServer().getOnlinePlayers()){
							player.showPlayer(p);
							
						}
					}
			    }
				
			   }
		}, 0L, 20L);
		

	}
		
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");
	}
	public void msg(Player p,String s){
		p.sendMessage(ChatColor.GRAY+ "[" + ChatColor.BLUE + "HiddenShadows" + ChatColor.GRAY + "]" + ChatColor.DARK_AQUA + s);
	}
	
	
	
	
	
	
	
}