
package me.max.nlh;

import java.util.List;
import java.util.Random;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

public class nlhl implements Listener{
	public nlh plugin;
	 
	public nlhl(nlh p) {
	    plugin = p;
	}
	@EventHandler
	public void BlockBreak(BlockBreakEvent e){
		Player p = e.getPlayer();
		if(usingNightVision(p)) return;
		if(p.getWorld().getBlockAt(p.getLocation().subtract(0,1,0)).getRelative(BlockFace.UP).getLightLevel() <= plugin.getConfig().getInt("minLightLevel")){
			if(nlh.permission.has(p, "nolighthax.bypass")){
				debug("Bypassed");
				return;
			}
			List<String> wBlackList = plugin.getConfig().getStringList("World-BlackList");
			for(String s : wBlackList) {
				if(p.getWorld().equals(Bukkit.getWorld(s))) {
					return;
				}
			}
			if(p.getLocation().getBlockY() > plugin.getConfig().getInt("MinY")){
				return;
			}
			Random chance = new Random();
			int BreakChance = chance.nextInt(plugin.getConfig().getInt("chance"));
			if(BreakChance!=0){
				debug(String.valueOf(BreakChance));
				p.sendMessage(ChatColor.DARK_AQUA + "* " + plugin.getConfig().getString("failMessage"));
				e.setCancelled(true);
				e.setExpToDrop(0);
			}else{
				p.sendMessage(ChatColor.DARK_AQUA + "* " + plugin.getConfig().getString("successMessage"));
			}
		}
	}
	public void debug(String s){
		boolean debugs = false;
		if(!debugs){
			return;
		}
		System.out.println("debug" + s);
	}
	@SuppressWarnings("deprecation")
	public boolean usingNightVision(Player p) {
		return p.hasPotionEffect(PotionEffectType.NIGHT_VISION);
	}
}
