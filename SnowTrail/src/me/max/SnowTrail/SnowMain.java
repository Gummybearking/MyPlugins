package me.max.SnowTrail;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class SnowMain extends JavaPlugin implements Listener{
	public HashMap<String, Integer> map = new HashMap<String, Integer>();
	
	
	public final Logger logger = Logger.getLogger("Minecraft");
	 
	public void onEnable() {
	
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Enabled!");
		utils.setupPermissions();
		this.getServer().getPluginManager().registerEvents(new SnowMain(), this);
//		if(!config.exists()) {
//			this.saveDefaultConfig();
//			System.out.println("[Races] Generating a config for the race: Ender");
//		}

			

	}
			
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(label.equalsIgnoreCase("SnowTrail")) {
			Player p = (Player) sender;
			if(map.containsKey(p.getName())){
				map.remove(p.getName());
				p.sendMessage(ChatColor.AQUA + "[" + ChatColor.WHITE + "SnowTrail" + ChatColor.AQUA + "]" + ChatColor.WHITE + " Deactivated");
			}else {
				map.put(p.getName(), 1);
				p.sendMessage(map.keySet().toString() + ChatColor.AQUA + "[" + ChatColor.WHITE + "SnowTrail" + ChatColor.AQUA + "]" + ChatColor.WHITE + " Activated");
			}
		}
		return false;
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(e.getFrom() == null)
			return;
		p.sendMessage("Not null");
		if(!utils.hasPerm(p, "use") && !p.isOp())
			return;
		p.sendMessage("Has perm");
		if(!map.containsKey(p.getName()))
			return;
		p.sendMessage("in map");
		if(e.getFrom().getBlock().getType() != Material.AIR)
			return;
		
		e.getFrom().getBlock().setType(Material.SNOW);
		startDelayedRemoval(e.getFrom().getBlock());
	}
	public void startDelayedRemoval(final Block b) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(new SnowMain(), new Runnable() {
            public void run () {
            	if(b.getType() != Material.AIR)
            		b.setType(Material.AIR);
            }
		}, 10 * 20);
	}
}
