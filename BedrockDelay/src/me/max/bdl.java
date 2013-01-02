package me.max;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.Potion;

public class bdl implements Listener{
	public static Map<String, Integer> placeDelay = new HashMap<String, Integer>();
	public static Map<String, Integer> eatDelay   = new HashMap<String, Integer>();
	public static Map<String, Integer> pearlDelay = new HashMap<String, Integer>();


	public bd plugin;
	public bdl(bd p){
		plugin = p;
	}
	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		Player p = e.getPlayer();
		if(bd.perm.has(p,"BedrockDelay.bypass") || p.isOp()) return;
		Block b = e.getBlock();
		if(b.getType() == Material.BEDROCK){
			if(placeDelay.containsKey(p.getName())){
				e.setCancelled(true);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Cannot-Place-Message") + " &6Time Remaining: " + placeDelay.get(p.getName())/20)+ " seconds");
			}
			delayPlayer(p.getName(), placeDelay, "Bedrock-Delay");
		}
	}
	public void delayPlayer(final String p, final Map<String, Integer> map, String config){
		if(map.containsKey(p)) return;
		int time = plugin.getConfig().getInt(config);
		map.put(p, time*20);
	}
	public void delayPlayerPotion(final String p, final Map<String, Integer> map, String time){
		if(map.containsKey(p)) return;
		map.put(p, Integer.valueOf(time)*20);
	}
 	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(bd.perm.has(p, "BedrockDelay.bypass") || p.isOp()) {
			return;
		}
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(p.getItemInHand().getType() == Material.GOLDEN_APPLE) {
				if(eatDelay.containsKey(p.getName())){
					e.setCancelled(true);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Cannot-Eat-Message") + " &6Time Remaining: " + eatDelay.get(p.getName())/20)+ " seconds");
				}else{
					delayPlayer(p.getName(),eatDelay,"GoldenApple-Delay");
				}
			}
			if(p.getItemInHand().getType() == Material.ENDER_PEARL) {
				if(pearlDelay.containsKey(p.getName())){
					e.setCancelled(true);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Cannot-Use-Pearl-Message") + " &6Time Remaining: " + pearlDelay.get(p.getName())/20)+ " seconds");
				}else{
					delayPlayer(p.getName(),pearlDelay,"EnderPearl-Delay");
				}
			}
			p.getItemInHand().setAmount(1);
			if(getPotions().containsKey(p.getItemInHand())) {
				delayPlayerPotion(null, null, null);
			}
		}
	}
 	public HashMap<ItemStack, String> getPotions() {
 		HashMap<ItemStack, String> potions = new HashMap<ItemStack, String>();
 		for(String s : plugin.getConfig().getStringList("Potions")) {
 			String split[] = s.split("-");
 			ItemStack stack = new ItemStack(Material.getMaterial(Integer.valueOf(split[0])));
 			stack.setData(new MaterialData(Byte.valueOf(split[1])));
 			stack.setAmount(1);
 			potions.put(stack, split[2]);
 		}
 		return potions;
 	}
//	@EventHandler
//	public void onLeave(PlayerQuitEvent e) {
//		Player p = e.getPlayer();
//		if(placeDelay.containsKey(p.getName()))
//			plugin.getConfig().set("Users.Bedrock." + p.getName(), placeDelay.get(p));
//		if(eatDelay.containsKey(p.getName()))
//			plugin.getConfig().set("Users.Apple."   + p.getName(), eatDelay.get(p));
//		if(pearlDelay.containsKey(p.getName()))
//			plugin.getConfig().set("Users.Pearl."   + p.getName(), pearlDelay.get(p));
//	}
//	@EventHandler
//	public void onJoin(PlayerJoinEvent e) {
//		Player p = e.getPlayer();
//		
//		FileConfiguration c    = plugin.getConfig();
//		ConfigurationSection s = c.getConfigurationSection("Users");
//		if(s.getKeys(true).contains("Bedrock." + p.getName())) {
//			placeDelay.put(p,s.get("Bedrock" + p.getName()));
//		}
//		
//	}
}
