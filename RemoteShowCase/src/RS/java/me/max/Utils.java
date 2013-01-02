package RS.java.me.max;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;


public class Utils {

	public static Permission permission = null;
	public static Economy economy = null;
	
	public static boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public static boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
    
     /*Economy
     * Economy
     * Economy
     * Economy
     */
    
    public static void DebitPlayer(Player p, double amount){
    	economy.withdrawPlayer(p.getName(), amount);
    }
    public static void DepositPlayer(Player p, double amount){
    	economy.depositPlayer(p.getName(), amount);
    }
     /*Permission
     * Permission
     * Permission
     * Permission
     */
    
    public static boolean HasPermission(Player p, String s){
    	if(p.isOp())                                 return true;
    	if(permission.has(p, "RemoteShowcase." + s)) return true;
    	return false;
    }
    public static int getNumber(RemoteShowcase rs, String s){
    	int number = 0;
    	for (String st : rs.getShops().getKeys(true)) {
    		if(st.startsWith("Shops." +  s + ".")){
    			number++;
    		}
    	}
    	return number;
    }
    public static String LocationToString(Location loc) {

    	int x = loc.getBlockX();
    	int y = loc.getBlockY();
    	int z = loc.getBlockZ();
    	
    	return String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z);


    }
    public static Location StringToLocation(RemoteShowcase rs, String s) {
    	String split[] = s.split(",");
    	if(split.length != 3){
    		return null;
    	}
    	World world = Bukkit.getWorld("world");
    	Block b =world.getBlockAt(Integer.valueOf(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]));
    	return b.getLocation();
    }
    public static Location StringToLocation(RemoteShowcase rs,World w,String s) {
    	s = rs.getShops().getString("Shops." + s);
    	return StringToLocation(rs,s);
    }
    public static String capFirst(String s){
		String firstLetter = s.substring(0,1);
		String rest = s.substring(1);
		s = firstLetter.toUpperCase() + rest.toLowerCase();
		return s;
	}
 
	
	
    
}
