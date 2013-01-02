package me.max;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class creepL implements Listener{
	public creeper plugin;
	public creepL(creeper p){
		plugin = p;
	}
	//DeathEvent
	@EventHandler
	public void onPlayerDeath(EntityDeathEvent e){
		if(e.getEntity() instanceof Player) {
			Player p = (Player)e.getEntity();
			if(!utils.hasPerm(p, "deathexplosion")) {
				return;
			}
			p.getWorld().createExplosion(p.getLocation(), 4F);
			msg(p, "deathExplosion");
		}
	}
	public void msg(Player p, String s){
		utils.msg(p,plugin.getConfig().getStringList("messages." + s));
	}
}
