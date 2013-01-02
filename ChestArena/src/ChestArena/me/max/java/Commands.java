package ChestArena.me.max.java;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Commands {
	
	public static void Choose(Player p){
		if(!gameHandler.Choosing.containsKey(p)){
			gameHandler.Choosing.put(p, null);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[ChestArena] &eYou are now selecting a chest"));
			

		}
		else{
			gameHandler.Choosing.remove(p);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[ChestArena] &eYou are no longer selecting a chest"));

		}
	}
	public static void Start()
	{
		if(ChestArena.isGame) return;
		gameHandler.startGame();
	}
	public static void End()
	{
		if(!ChestArena.isGame) return;
		gameHandler.endGame(null);
		gameHandler.broadcast("Admin-End", 0, null, null);
			
		
	}
	
	
	
	public static void errmsg(Player p, String error)
	{
		
		p.sendMessage(ChatColor.RED + "Error: " + error);
	
	}
	
	public static void msg(Player p, String s){
		
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
		
	}
	
	public static boolean hasPerm( Player p, String s ){
		
		if( !ChestArena.permission.has( p, "ChestArena." + s ) ){
			
			errmsg( p, "You do not have permission" );
			
			return false;
			
		}
		
		return true;
	}
}
