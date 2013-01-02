package ChestArena.me.max.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class gameHandler implements Listener
{
	public static ChestArena plugin;
	
	public static Map<Clan, Integer> Capturing = new HashMap<Clan, Integer>();
	
	public static Player cap;
	
	public static Map<Player, ArrayList<Block>> Choosing = new HashMap<Player, ArrayList<Block>>();

	public static Clan owner = null;
	
	public gameHandler(ChestArena p)
	{
		plugin = p;
	}
	
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////// 
	@EventHandler
	(priority = EventPriority.HIGHEST)
	public void onRightClick(PlayerInteractEvent e)
	{
		if(e.isCancelled()) return;
		
		Block b = e.getClickedBlock();
		if(b.getState() instanceof Chest)
		{
			Chest chest = (Chest)b.getState();
			if(plugin.arenaChest != null && chest.getLocation().toString().equals(plugin.arenaChest.getLocation().toString() ) ){
				
				
					
					
				
				if(!ChestArena.isGame) return;
				Player p = e.getPlayer();
				ClanPlayer cp = sc().getClanManager().getClanPlayer(p.getName());
				if(cp == null) return;
				Clan clan = cp.getClan();
				if(clan == null) return;

				if( Capturing.containsKey(clan) ){
					msg( p, "Same-Clan");
					
					return;
					
				}
				
				if(Capturing.isEmpty()){
					broadcast("New-Capturing-Clan", 0, clan, e.getPlayer());					
					
					cap = e.getPlayer();
					Capturing.put(clan, plugin.getConfig().getInt("Capture-Time"));
					return;
							
				} else {
					for(Clan c : Capturing.keySet()){
						Capturing.remove(c);
						
						
					}
					broadcast("New-Capturing-Clan", 0, clan, e.getPlayer());
					cap = e.getPlayer();
					Capturing.put(clan, plugin.getConfig().getInt("Capture-Time"));
					return;
				}
				
				
				
			}else{
				if(Choosing.containsKey(e.getPlayer())){
					int x = b.getX();
					int y = b.getY();
					int z = b.getZ();
					plugin.getConfig().set("Chest.x", x);plugin.getConfig().set("Chest.y", y);plugin.getConfig().set("Chest.z", z);plugin.saveConfig();
					Choosing.remove(e.getPlayer());

					
					msg(e.getPlayer(), "Chest-Selected");
				}
			}
			
		}
		
	}
	@EventHandler
	public static void onChestOpen(InventoryOpenEvent e)
	{
	
		Chest chest = null;
		if(e.getInventory().getHolder() instanceof Chest)
		{
			chest = (Chest)e.getInventory().getHolder();
		}
		if(chest == null) return;
		if(plugin.arenaChest == null) return;
		if(!(chest.getLocation().toString().equals(plugin.arenaChest.getLocation().toString()))) return;
		HumanEntity he = e.getPlayer();
		if( he instanceof Player )
		{
			Player p = (Player) he;
			
			if(ChestArena.permission.has(p,"ChestArena.admin")) return;
			
			if(ChestArena.isGame){
				e.setCancelled(true);
				return;
			
			}else{
				
				Clan pClan = sc().getClanManager().getClanByPlayerName(p.getName());
				if(owner == null || pClan == null || !pClan.equals(owner)){
					e.setCancelled(true);
					msg( p,"Open-Failed");
					
				}
				
			}
			
		}
		
	}
	@EventHandler
	public static void onPVP(EntityDamageByEntityEvent e){
		Entity v = e.getEntity();
		Entity a = e.getDamager();
		if(
			!(v instanceof Player) 
			||
			!(a instanceof Player)
			||
			cap == null
		) return;
		//Returns if either is not player
		
		Player p = (Player)v;
		Player d = (Player)a;
		//Setup Players
		
		if(p != cap) return;
		
		//Return if not the capper
		for(Clan c : Capturing.keySet()){
			broadcast("Capturing-Player-Injured", 0, null, p);
			Capturing.remove(c);
			cap = null;
			
			
			
		}

		
		
	}
	
	@EventHandler
	public static void onLeave(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(!ChestArena.isGame) return;
		if(p.equals(cap)){
			cap = null;
			broadcast("Capturing-Player-Left", 0, null, p);
			for(Clan c : Capturing.keySet()){
				Capturing.remove(c);
			}
		}
	}
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	
	
	public static SimpleClans sc()
	{
		return plugin.sc;
	}
	public static void msg(Player p, String s){
		msg(p,s,0);
	}
	public static void msg(Player p, String s, long time)
	{
		
		String message = plugin.getConfig().getString("Messages." + s);
		if(message.contains("%t"))
			message = message.replace("%t", String.valueOf(time));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[ChestArena]&f " + message));
		
	}
	public static void broadcast(String s, long time, Clan clan, Player pl)
	{
		for(Player p : Bukkit.getServer().getOnlinePlayers()){
			checkDistance(p,s,clan,pl,time);
		}
	}
	public static void checkDistance(Player p, String s, Clan clan, Player capturing, long time){
		Location loc = plugin.ArenaChestBlock.getLocation();
		int range = plugin.getConfig().getInt("Messages.Range-Distance");
		if(p.getWorld().equals(plugin.arena) && p.getLocation().distance(loc) <= range)
		{
			String message = plugin.getConfig().getString("Messages." + s);
			if(message == null) System.out.println(s);
			if(clan != null && message.contains("%c"))
				message = message.replace("%c", clan.getColorTag());
			if(capturing != null && message.contains("%p"))
				message = message.replace("%p", capturing.getDisplayName());
			if(time != 0 && message.contains("%t"))
				message = message.replace("%t", String.valueOf(time));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[ChestArena]&f " + message));
				
		}
		
	}
	public static void repeatCheck()
	{
		 Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
             public void run() {
            	 if(ChestArena.isGame){
            		 if(Capturing.isEmpty())
            		 {
            			 repeatCheck();
            			 return;
            		 }
            		 for(Clan clan : Capturing.keySet())
            		 {
            			 int time = Capturing.get(clan);
            			 Capturing.put(clan, time - 1);
            			 if((time - 1) <= 0){
            				 endGame(clan);
            				 return;
            			 }
            			 if(time%5==0){
            				
            					 
            				msg(cap, "Time-Remaining-Message", time);
            				 
            			 }
            			 if(time%(plugin.getConfig().getInt("Capture-Time")/4)==0){
            				broadcast("Time-Remaining-Broadcast", time, clan, null);
            			 }
            			 
            		}
            		repeatCheck();
            	
            	 }
             }
         }, 20);
	}
	public static void endGame(Clan winner)
	{
		
		ChestArena.isGame = false;
		owner = winner;
		if(owner != null)
		{
			broadcast("Game-End", 0, winner, null);
		}
		rewardHandler.printRewards(plugin);
		Capturing.clear();
		plugin.arenaTimer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
             public void run() {
 				plugin.unSetupArena();
 				System.out.println("\n\nChest Closed for CA\n\n");
             }
        }, 20*5*60);
		
	}
	public static void startGame()
	{
		plugin.setupArena();
		ChestArena.isGame = true;
		owner = null;
		Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Messages.Game-Start")));
		repeatCheck();
	}
	
}
