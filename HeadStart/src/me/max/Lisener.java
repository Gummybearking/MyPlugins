package me.max;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Lisener implements Listener{
	@EventHandler
	public void playerjoin(PlayerJoinEvent event){
		
		Player player = event.getPlayer();
		if(main.permission.has(player, "Headstart.logon")|| player.isOp()){
			Random rand = new Random();
			
			int effectNum = rand.nextInt(4);
			PotionEffect effect = null;
			switch (effectNum) {
				case 0:
					effect = new PotionEffect(PotionEffectType.FAST_DIGGING, 1, 30);
					player.sendMessage(ChatColor.GOLD + "[" + ChatColor.YELLOW + "HeadStart" + ChatColor.GOLD + "] " + ChatColor.GRAY +"You have been given the ability to dig faster!");
					
					break;
				case 1:
					effect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1, 30);
					player.sendMessage(ChatColor.GOLD + "[" + ChatColor.YELLOW + "HeadStart" + ChatColor.GOLD + "] " + ChatColor.GRAY +"Your Strength has been increased!");

				
					break;
				case 2:
					effect = new PotionEffect(PotionEffectType.JUMP, 1, 30);
					player.sendMessage(ChatColor.GOLD + "[" + ChatColor.YELLOW + "HeadStart" + ChatColor.GOLD + "] " + ChatColor.GRAY +"You have been given the ability to jump higher!");

				
					break;
				case 3:
					effect = new PotionEffect(PotionEffectType.SPEED, 1, 30);
					player.sendMessage(ChatColor.GOLD + "[" + ChatColor.YELLOW + "HeadStart" + ChatColor.GOLD + "] " + ChatColor.GRAY +"You have been given the ability to move faster!");

				
					break;
				default:
					break;
					
			}
			player.addPotionEffect(effect);
			
			
			
			
		}
	}
}
