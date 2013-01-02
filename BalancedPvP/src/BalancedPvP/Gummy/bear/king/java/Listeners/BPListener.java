package BalancedPvP.Gummy.bear.king.java.Listeners;





import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlockOwner;
import com.palmergames.bukkit.towny.object.TownyUniverse;

import BalancedPvP.Gummy.bear.king.java.BPMain;
import BalancedPvP.Gummy.bear.king.java.Utils.utils;

public class BPListener implements Listener{
	public static HashMap<String, Integer> ToDelay = new HashMap<String, Integer>();
	public BPMain plugin;
	public BPListener(BPMain p){
		plugin = p;
	}
	
	@EventHandler
	(priority = EventPriority.LOWEST)
	public void onPvP(EntityDamageByEntityEvent event) {
		Entity EntityDamager = event.getDamager();
		Entity EntityDamagee = event.getEntity();
		
		//Make sure the damage is done by a player
		if( !(EntityDamager instanceof Player) )
			return;
		
		//Check to see if its a player
		if(EntityDamagee instanceof Player){
			
			//Check to see if its enabled for players
			if(!getConfigBoolean("Options.Enable-For-PvP"))
				return;
			
			//Check to see if they can bypass
			if(utils.hasPerm((Player) EntityDamager, "Bypass")) return;

		}
		//Check to see if its a creature
		if(EntityDamagee instanceof Creature && !getConfigBoolean("Options.Enable-For-PvM")) {
			//Check to see if they can bypass
			if(utils.hasPerm((Player) EntityDamager, "Bypass")) return;
			return;
		}
		Player Damager = (Player) EntityDamager;
		double damage  = event.getDamage();
		double toDivide   = getConfigDouble("Options.Armor-Points-To-HalfHeart");
		int toSubtract = (int) Math.round(CalculateThreshold(Damager.getInventory().getArmorContents())/toDivide);
		event.setDamage((int)damage - toSubtract);
	}
	@EventHandler
	(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(EntityDeathEvent e) {
		if(!(e.getEntity() instanceof Player))  return;
		Player p = (Player) e.getEntity();
		if(!(p.getKiller() instanceof Player))  return;
		if(utils.hasPerm((Player) p, "Bypass")) return;
		int time = Integer.valueOf(getConfigString("Options.Death-Delay"));
		ToDelay.put(p.getName(), time);
	}
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		String command = e.getMessage().replace("/", "");
		for(String s : plugin.getConfig().getStringList("Balanced-PvP.Options.Death-To-Delay")){
			if(command.contains(s) && ToDelay.containsKey(p.getName())){
				e.setCancelled(true);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfigString("Options.Death-Delay-Message").replace("%t", String.valueOf(ToDelay.get(p.getName())))));
			}
		}
		for(String s : plugin.getConfig().getStringList("Balanced-PvP.Options.Towny-To-Deny")){
			if(command.contains(s)){
				if(p.isOp()) continue;
				if(TownyUniverse.isWilderness(p.getLocation().getBlock())) continue;
					try {
						if(TownyUniverse.getTownBlock(p.getLocation()).getTown().hasAssistant(new Resident(p.getName()))) continue;
						if(TownyUniverse.getTownBlock(p.getLocation()).getTown().hasResident((new Resident(p.getName())))) continue;
						if(TownyUniverse.getTownBlock(p.getLocation()).getTown().hasResident((new Resident(p.getName())))) continue;
						if(TownyUniverse.getTownBlock(p.getLocation()).isOwner((TownBlockOwner) p)) continue;
						e.setCancelled(true);
					} catch (NotRegisteredException e1) {
						e1.printStackTrace();
					}
					
			}
		}
		
	}
	public double CalculateThreshold(ItemStack[] Armors) {
		double ArmorTotal = 0D;
		double Modifier   = 0D;
		
		for(ItemStack armor : Armors) {
			ArmorTotal += CalculateArmor(armor);
		}
		
		Modifier += ArmorTotal;
		return Modifier;
		
	}
	public double CalculateArmor(ItemStack Armor) {
		Material Type = Armor.getType();
		
		if(Type == Material.DIAMOND_BOOTS)
			return 1.5D;
		if(Type == Material.DIAMOND_CHESTPLATE)
			return 4D;
		if(Type == Material.DIAMOND_LEGGINGS)
			return 3D;
		if(Type == Material.DIAMOND_HELMET)
			return 1.5D;
		
		if(Type == Material.IRON_BOOTS)
			return 1D;
		if(Type == Material.IRON_CHESTPLATE)
			return 3D;
		if(Type == Material.IRON_LEGGINGS)
			return 2.5D;
		if(Type == Material.IRON_HELMET)
			return 1D;
		
		if(Type == Material.GOLD_BOOTS)
			return 0.5D;
		if(Type == Material.GOLD_CHESTPLATE)
			return 2.5D;
		if(Type == Material.GOLD_LEGGINGS)
			return 1.5D;
		if(Type == Material.GOLD_HELMET)
			return 1D;
		
		if(Type == Material.CHAINMAIL_BOOTS)
			return 0.5D;
		if(Type == Material.CHAINMAIL_CHESTPLATE)
			return 2.5D;
		if(Type == Material.CHAINMAIL_LEGGINGS)
			return 2D;
		if(Type == Material.CHAINMAIL_HELMET)
			return 1D;
		
		if(Type == Material.LEATHER_BOOTS)
			return 0.5D;
		if(Type == Material.LEATHER_CHESTPLATE)
			return 1.5D;
		if(Type == Material.LEATHER_LEGGINGS)
			return 1D;
		if(Type == Material.LEATHER_HELMET)
			return 0.5D;
		
		return 0;
	}
	public double CalculateEnchantments(ItemStack Armor) {
		if(!Armor.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) 
			return 0D;
		return Armor.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
	}
	public Object getConfigObject(String s) {
		ConfigurationSection config = plugin.getConfig().getConfigurationSection("Balanced-PvP"); 
		return config.get(s);
	}
	public String getConfigString(String s) {
		return String.valueOf(getConfigObject(s));
	}
	public double getConfigDouble(String s) {
		return (Double) getConfigObject(s);
	}
	public boolean getConfigBoolean(String s) {
		return  Boolean.valueOf(getConfigString(s));
	}
	public void debug(String s) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.isOp()) p.sendMessage(s);
		}
	}
}
