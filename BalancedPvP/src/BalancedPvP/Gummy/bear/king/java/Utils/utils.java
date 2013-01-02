package BalancedPvP.Gummy.bear.king.java.Utils;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.permission.Permission;

public class utils {
	 	public static Permission permission = null;
	    public static boolean setupPermissions()
	    {
	        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
	        if (permissionProvider != null) {
	            permission = permissionProvider.getProvider();
	        }
	        return (permission != null);
	    }
	    public static Permission getPerms(){
	    	return permission;
	    }
	    public static boolean hasPerm(Player p, String s){
	    	boolean hasperms = false;
	    	if(permission.has(p, "BalancedPvP." + s)) hasperms = true;
	    	return hasperms;
	    }
	    //////
	    public static void msg(Player p, List<String> list){
			String msg = null;
			List<String> msgs = list;
			int amount = msgs.size();
			Random r = new Random();
			msg = msgs.get(r.nextInt(amount)); 
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		}
	    public static void debug( String s){
	    	boolean debug = true;
	    	if(!debug) return;
	    	System.out.println("debug " + s);
	    }
}
