package me.max.hs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class hsl implements Listener{
	public static Map<Player, Long> hidden = new HashMap<Player, Long>();
	public static Map<Player, Long> hiddenInLight = new HashMap<Player, Long>();
	public hs plugin;
	public hsl(hs p) {
	    plugin = p;
	}

	@EventHandler
	public void Shift(PlayerToggleSneakEvent e){
		Player p = e.getPlayer();
		Block b = p.getWorld().getBlockAt(p.getLocation().subtract(0,1,0));
		if(hiding(p) && p.isSneaking()){
			hidden.remove(p);
			show(p);
		
		}
		if(!p.isSneaking() && b.getRelative(BlockFace.UP).getLightLevel() <= cInt("maxLight") && p.getGameMode()== GameMode.SURVIVAL){
			
			hide(p);
		}
	}
	@EventHandler
	public void move(PlayerMoveEvent e){
		
		Player p = e.getPlayer();
		Block b = p.getWorld().getBlockAt(p.getLocation().subtract(0,1,0));
		if(b.getRelative(BlockFace.UP).getLightLevel() > cInt("maxLight")){
			if(hiding(p)){
				hiddenInLight.put(p, 0L);
			}
			return;
		}else{
			if(hiddenInLight.containsKey(p)){
				hiddenInLight.remove(p);
				hidden.put(p, cInt("timer"));
			}
		}
		
	}
	public boolean hiding(Player p){
		return hidden.containsKey(p);
	}
	public void hide(final Player p){
		if(!hs.permission.has(p, "hiddenshadow.use")){
			return;
		}
		msg(p, "Preparing to become invisible");
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			   public void run() {
				   if(p.isSneaking()){
				   msg(p, "You have become invisible");
						for (Player plr : Bukkit.getServer().getOnlinePlayers()){
							if(!hs.permission.has(plr, "hiddenshadows.bypass")){
								plr.hidePlayer(p);
							}
						}
						hidden.put(p, cInt("timer"));
				   }else{
					   msg(p,"Invisibility failed!");
				   }
			   }
			}, 60L);
		
	}
	@EventHandler
	public void BlockHit(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(hiding(p)){
			show(p);
			hidden.remove(p);
		}
	}
	public void show(Player p){
		
			for (Player plr : Bukkit.getServer().getOnlinePlayers()){
				if(!hs.permission.has(plr, "hiddenshadows.bypass")){
					plr.showPlayer(p);
					
				}
			}
			msg(p, "You have become visible");
			if(hiddenInLight.containsKey(p)){
				hiddenInLight.remove(p);
			}
			return;
		
	}
	public Long cInt(String s){
		if(plugin.getConfig().getString(s)!=null){
			return plugin.getConfig().getLong(s);
		}
		return 0L;
	}
	public void msg(Player p,String s){
		p.sendMessage(ChatColor.GRAY+ "[" + ChatColor.BLUE + "HiddenShadows" + ChatColor.GRAY + "]" + ChatColor.DARK_AQUA + s);
	}
	

}
