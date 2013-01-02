package me.max;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class dwarfL implements Listener{
	public dwarf plugin;
	public dwarfL(dwarf p){
		plugin = p;
	}
	//Mining Event
	@EventHandler
	public void onMine(BlockBreakEvent e){
		//Check for permission
		if(!utils.hasPerm(e.getPlayer(), "randomGold")) return;
		//Create the chance
		Random r = new Random();
		int random = r.nextInt(1000);
		while(random == 0){
			random = r.nextInt(1000);
		}
		if(random == 1){
			Block block = e.getBlock();
			block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_NUGGET, 1));
			msg(e.getPlayer(), "foundGold");

		}
	}
	public void msg(Player p, String s){
		utils.msg(p,plugin.getConfig().getStringList("messages." + s));
	}
}
