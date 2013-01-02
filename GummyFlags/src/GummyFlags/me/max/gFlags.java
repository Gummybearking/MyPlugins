package GummyFlags.me.max;

import java.io.File;
import java.util.logging.Logger;



import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import GummyFlags.me.max.Flags.BreakSpecialFlag;
import GummyFlags.me.max.Flags.DropFlag;
import GummyFlags.me.max.Flags.ExplosionFlag;
import GummyFlags.me.max.Listeners.SpellListener;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class gFlags extends JavaPlugin{
	public final Logger logger = Logger.getLogger("Minecraft");
    
    public void onEnable() {
    	PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Enabled!");
		hookIntoWorldGuard();
		getServer().getPluginManager().registerEvents(new  SpellListener(), this);

		
	}
		
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");
	}
	 public static void hookIntoWorldGuard() {
	        WorldGuardPlugin wg = (WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	        // Reflect the IPVP flags into WorldGuard(as there is no API for this)
	        ExplosionFlag.reflectIntoFlags();
	        DropFlag.reflectIntoFlags();
	        BreakSpecialFlag.reflectIntoFlags();
	        // Reload WorldGuard (the same as the "reload" command)
	        wg.getGlobalStateManager().unload();
	        wg.getGlobalRegionManager().unload();
	        wg.getGlobalStateManager().load();
	        wg.getGlobalRegionManager().preload();
	 }
	 
	 
}
