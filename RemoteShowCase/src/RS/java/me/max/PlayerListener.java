package RS.java.me.max;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener{
	public static List<Player> pList = new LinkedList<Player>();
	public RemoteShowcase rs;
	public PlayerListener(RemoteShowcase plugin) {
		rs = plugin;
	}
	@EventHandler
	public void pInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(!pList.contains(p)) {
			return;
		}
		if(rs.getConfig().getStringList("Open-Shops").contains(Utils.LocationToString(e.getClickedBlock().getLocation()))) {
			RemoteShowcase.ErrorMessage(p, "This has already been added to the Open-Shops");
			return;
		}
		Commands.MarkAsOpen(p, e.getClickedBlock().getLocation(), rs, true);
		pList.remove(p);
		
	}

}
