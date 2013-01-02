package BalancedPvP.Gummy.bear.king.java;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import BalancedPvP.Gummy.bear.king.java.Listeners.BPListener;
import BalancedPvP.Gummy.bear.king.java.Utils.utils;

public class BPMain extends JavaPlugin{
public final Logger logger = Logger.getLogger("Minecraft");
    
   public void onEnable() {
	   PluginDescriptionFile pdffile = this.getDescription();
	   this.logger.info(pdffile.getName() + " Has Been Enabled!");
	   getServer().getPluginManager().registerEvents(new BPListener(this), this);
	   File config = new File(this.getDataFolder(), "config.yml");
	   utils.setupPermissions();
	   if(!config.exists()){
		   this.saveDefaultConfig();
	   }
	   Timer();
	}
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");
	}
	public void Timer() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
				Timer();
				for(Object o : BPListener.ToDelay.keySet().toArray()) {
					String p = (String) o;
					if(BPListener.ToDelay.get(p) <=0) 
						BPListener.ToDelay.remove(p);
					else
						BPListener.ToDelay.put(p, BPListener.ToDelay.get(p) - 1);
				}
            }
       }, 20);
	}
}
