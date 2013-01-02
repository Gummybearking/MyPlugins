package me.max;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class catL implements Listener{
	public cat plugin;
	public catL(cat p){
		plugin = p;
	}
	//PvpEvent
	@EventHandler
	public void onPVP(EntityDamageByEntityEvent e){
		Entity en = e.getDamager();
		if(en instanceof Player){

			Player p = (Player)en;
			if(p.getItemInHand().getType() != Material.AIR) return;
			if(!utils.hasPerm(p, "extradamage")) return;
			Random r = new Random();
			int isExtra = 0;
			do{
				isExtra = r.nextInt(40);
			}while(isExtra == 0);
			if(isExtra == 1){
				e.setDamage(e.getDamage() + 2);
				msg(p, "extraDamage");
			}

		}
		
	}

	public void msg(Player p, String s){
		utils.msg(p,plugin.getConfig().getStringList("messages." + s));
	}
}
