package PumpkinPowers.main.Gummy.java;

import java.util.logging.Logger;



import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PPMain extends JavaPlugin implements Listener{
	
	public final Logger logger = Logger.getLogger("Minecraft");

    public void onEnable() {
    	PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Enabled!");
		Bukkit.getPluginManager().registerEvents(this, this);
		vCheck();
    }
    public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");
	}
    public void vCheck() {
    	this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
    		public void run() {		
				for(Player p : getServer().getOnlinePlayers()){
					if(p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getType() == Material.JACK_O_LANTERN) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
					}
				}
    		}
    	}, 40, 100);
    }
	
}
