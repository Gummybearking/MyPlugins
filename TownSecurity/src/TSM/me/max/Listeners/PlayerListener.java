package TSM.me.max.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import TSM.me.max.TSM;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownBlockType;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.object.TownyUniverse;

public class PlayerListener implements Listener{
	public TSM tsm;
	public PlayerListener(TSM t) {
		tsm = t;
	}
	
	TownyUniverse towny = new TownyUniverse(new Towny());
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		//Did they move a block? 
		if (e.getFrom().getBlockX() == e.getTo().getBlockX()
                 && e.getFrom().getBlockY() == e.getTo().getBlockY()
                 && e.getFrom().getBlockZ() == e.getTo().getBlockZ())
			 return;
		
		
		Player p   = e.getPlayer();
		Location l = p.getLocation();
		Resident r = new Resident(p.getName());

		if(p.isOp())
			return;
		
		//permission
		if((TSM.permission.has(p,"TownSecurity.bypass")))
				return;
		
		//wilderness
		if(TownyUniverse.isWilderness(l.getBlock()))
			return;
		
		TownBlock tb = getTownBlock(l);
		
		if(tb == null)
			return;
		
		TownyPermission plotperms = tb.getPermissions();
		TownyPermission townperms = null;
		
		
		try {
			townperms = tb.getTown().getPermissions();
		} catch (NotRegisteredException e2) {
			e2.printStackTrace();
		}
		
		//pvp
		if(plotperms.pvp || (townperms != null && townperms.pvp))
			return;
		
		//public
		try {
			if(tb.getTown().isPublic())
				return;
		} catch (NotRegisteredException e2) {
			e2.printStackTrace();
		}
		try {
		
			if(tb.getTown().hasResident(p.getName()))
			return;
		
		} catch (NotRegisteredException e1) {
			e1.printStackTrace();
		}
		
		//Embassy resident
		try {
			if(tb.getType() == TownBlockType.EMBASSY && tb.getResident().getName().equals(r.getName()))
				return;
		} catch (NotRegisteredException e3) {
			e3.printStackTrace();
		}
		
		//shop plots
		if(tb.getType() == TownBlockType.COMMERCIAL)
			return;
		
		//If has embassy
		try {
			for(TownBlock block : tb.getTown().getTownBlocks()) {
				if(block.getType() == TownBlockType.EMBASSY && block.getResident().getName().equals(r.getName()))
					return;
			}
		} catch (NotRegisteredException e2) {
			e2.printStackTrace();
		}
		
		//Bypassed town
		try {
			if(tsm.getConfig().getStringList("Bypass").contains(tb.getTown().getName()))
				return;
		} catch (NotRegisteredException e1) {
			e1.printStackTrace();
		}
		
		
		//friends
		try {
			if(tb.hasResident() && tb.getResident().hasFriend(r))
				return;
			if(tb.getTown().getMayor().hasFriend(r))
				return;
		} catch (NotRegisteredException e1) {
			e1.printStackTrace();
		}
		
		
		Location newLoc = e.getFrom();
        newLoc.setX(newLoc.getBlockX() + 0.5);
        newLoc.setY(newLoc.getBlockY());
        newLoc.setZ(newLoc.getBlockZ() + 0.5);
        e.setTo(newLoc);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[Towny] &aYou Cannot Enter This Area!"));
		

	}
	public TownBlock getTownBlock(Location l) {
		return TownyUniverse.getTownBlock(l);
	}
	
}
