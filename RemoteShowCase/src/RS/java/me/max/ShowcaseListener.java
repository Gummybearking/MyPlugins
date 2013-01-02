package RS.java.me.max;


import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.kellerkindt.scs.events.ShowCaseChangedEvent;
import com.kellerkindt.scs.events.ShowCaseCreateEvent;;

public class ShowcaseListener implements Listener{
	public static RemoteShowcase plugin;
	public ShowcaseListener(RemoteShowcase p) {
		plugin = p;
	}
	@EventHandler
	public static void onShowcaseCreation(ShowCaseCreateEvent e) {
		Location loc = e.getLocation();
		List<String> list = plugin.getConfig().getStringList("Open-Shops");
		String array[] = new String[list.size()];
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i)!=null) array[i] = list.get(i);
		}
		for(String s : array) {
			Location l = Utils.StringToLocation(plugin, s);
			if(l.toString().equals(loc.toString())) {
					list.remove(s);
			}
			
		}
		plugin.getConfig().set("Open-Shops", list);
		plugin.saveConfig();
	}
	
	

}
