package me.max;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class enderL implements Listener{
	public ender plugin;
	public HashMap<String, Integer> map = new HashMap<String, Integer>();
	public enderL(ender p){
		plugin = p;
	}
	//Mining Event
	@EventHandler
	public void onMine(BlockBreakEvent e){
		if(!utils.hasPerm(e.getPlayer(), "SilkTouch")) return;
		int mined = 0;
		Player p = e.getPlayer();
		if(map.containsKey(p.getName())) {
			mined = map.get(p.getName());
			map.remove(p.getName());
		}
		if(mined == 1000) {
			e.setCancelled(true);
			Block b = e.getBlock();
			Material Type = b.getType();
			b.setType(Material.AIR);
			p.getWorld().dropItem(b.getLocation(), new ItemStack(Type, 1));
			map.remove(p.getName());
			msg(p,"SilkTouched");
			return;
		}
		mined += 1;
		map.put(p.getName(), mined);		
	}
	public void msg(Player p, String s){
		utils.msg(p,plugin.getConfig().getStringList("messages." + s));
	}
}
