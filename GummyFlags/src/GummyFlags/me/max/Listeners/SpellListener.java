package GummyFlags.me.max.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import GummyFlags.me.max.Flags.BreakSpecialFlag;
import GummyFlags.me.max.Flags.DropFlag;
import GummyFlags.me.max.Flags.ExplosionFlag;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class SpellListener implements Listener{
	@EventHandler
	public static void onBoom(EntityExplodeEvent e) {
        WorldGuardPlugin wg = (WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        
		Location loc = e.getLocation();
		RegionManager man = wg.getRegionManager(loc.getWorld());
	    ApplicableRegionSet regions = man.getApplicableRegions(e.getLocation());
	    if(regions.getFlag(ExplosionFlag.flag) == State.ALLOW) {
	    	e.blockList().clear();
	    }  
	}
	@EventHandler
	public static void onDrop(PlayerDropItemEvent e) {
		WorldGuardPlugin wg = (WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if(e.getPlayer().isOp()) {
        	return;
        }
		Location loc = e.getPlayer().getLocation();
		RegionManager man = wg.getRegionManager(loc.getWorld());
	    ApplicableRegionSet regions = man.getApplicableRegions(loc);
	    if(regions.getFlag(DropFlag.flag) == State.DENY) {
	    	e.setCancelled(true);
	    }
	    
	}
	@EventHandler
	public static void onHangingBreak(HangingBreakByEntityEvent e) {
		WorldGuardPlugin wg = (WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if(e.getRemover() instanceof Player && ((Player)e.getRemover()).isOp()) {
			return;
		}
		Location loc = e.getEntity().getLocation();
		RegionManager man = wg.getRegionManager(loc.getWorld());
	    ApplicableRegionSet regions = man.getApplicableRegions(loc);
	    if(regions.getFlag(BreakSpecialFlag.flag) == State.DENY) {
	    	e.setCancelled(true);
	    }
	   
	}
	
	
}
