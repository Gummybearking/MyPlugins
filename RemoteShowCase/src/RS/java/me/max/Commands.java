package RS.java.me.max;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.kellerkindt.scs.ShowCaseStandalone;
import com.kellerkindt.scs.internals.Storage;
import com.kellerkindt.scs.shops.BuyShop;
import com.kellerkindt.scs.shops.SellShop;
import com.kellerkindt.scs.shops.Shop;
import com.kellerkindt.scs.utilities.Properties;


public class Commands {
	
	public static void Help(Player p) {
		RemoteShowcase.Message(p, "&a[]--- Shops ---[]");
		RemoteShowcase.Message(p, "&aSell&7: Create a shop to sell what you have in your hand"           );
		RemoteShowcase.Message(p, "        &7Usage: /Shop sell <Amount> <Price>"                         );
		RemoteShowcase.Message(p, "&aSell&7: Create a shop to buy items from players and give them money");
		RemoteShowcase.Message(p, "        &7Usage: /Shop buy <MaxAmount> <Price>"                       );
		RemoteShowcase.Message(p, "&aAdd&7: Add items to a previousley created shop"                     );
		RemoteShowcase.Message(p, "        &7Usage: /Shop add <ShopId> <Amount>"                         );
		RemoteShowcase.Message(p, "&aRemove&7: Destroy a previousley created shop"                       );
		RemoteShowcase.Message(p, "        &7Usage: /Shop remove <ShopId>"                               );
		if(Utils.HasPermission(p, "Admin")){
			RemoteShowcase.Message(p, "&4Create&7: Make the block you are looking at become an open shop");
			RemoteShowcase.Message(p, "        &7Usage: /Shop create"                                       );
		}

	}
	public void Buy(RemoteShowcase scs,Player p, int amount, int price) throws Exception {
		if(!(Utils.HasPermission(p, "Buy"))) {
			RemoteShowcase.ErrorMessage(p, "You do not have permission");
			return;
		}
		Location loc = getOpenShop(scs, p.getWorld());
		CreateBuyShowcase(scs,p,amount,price,p.getItemInHand(),loc);
	}
	//SELL
	public void Sell(RemoteShowcase scs,Player p, int amount, int price) throws Exception{
		if(!(Utils.HasPermission(p, "Sell"))) {
			RemoteShowcase.ErrorMessage(p, "You do not have permission");
			return;
		}
		Location loc = getOpenShop(scs, p.getWorld());
		CreateSellShowcase(scs,p,amount,price,p.getItemInHand(),loc);
	}
	
	public void CreateBuyShowcase(RemoteShowcase rs,Player player, int MaxAmount, int price, ItemStack is, Location loc) throws IOException{
		if(!(hasMoney(player,rs.getConfig().getDouble("Price-To-Create")))){
			RemoteShowcase.ErrorMessage(player, "You need more Voxels");
			return;
		}
		if(loc == null){
			RemoteShowcase.ErrorMessage(player, "No locations availible");
			return;
		}
        int number = getNumber(rs,player);
        String name = player.getName();
        String sha1 = name + String.valueOf(number);
		Storage	storage	= new Storage(Properties.storageVersion, sha1);
        Shop p = new BuyShop(getScs(), storage);
        		p.setUnlimited	(false            );
        		p.setItemStack	(is               );
        		p.setPrice		(price			  );
        		p.setOwner		(player.getName() );
        		p.setLocation   (loc              );
        		p.setVisible    (true             );
        		p.setMaxAmount  (MaxAmount        );
        		
       getScs().getShopHandler().addShop(p);
       getShops(rs).set("Shops." + player.getName() +"."+ String.valueOf(number), Utils.LocationToString(p.getLocation()));
       rs.saveShops();
       RemoteShowcase.Message(player, "&eShop created! &7ID: " + String.valueOf(number));
       Utils.economy.withdrawPlayer(player.getName(), rs.getConfig().getDouble("Price-To-Create"));
	}

	public void CreateSellShowcase(RemoteShowcase rs,Player player, int amount, int price, ItemStack is, Location loc) throws IOException{
		if(!(hasMoney(player,rs.getConfig().getDouble("Price-To-Create")))){
			RemoteShowcase.ErrorMessage(player, "You need more Voxels");
			return;
		}
		if(loc == null){
			RemoteShowcase.ErrorMessage(player, "No locations availible");
			return;
		}
		ItemStack iss = is.clone();
		iss.setAmount(amount);
		if(!hasItemStack(iss,player)){
			RemoteShowcase.ErrorMessage(player, "You are not holding "+String.valueOf(amount)+" x " + is.getType().toString().toLowerCase());
			List<String> list = rs.getConfig().getStringList("Open-Shops");
			list.add(Utils.LocationToString(loc));
			rs.getConfig().set("Open-Shops", list);
			return;
		}
        int number = getNumber(rs,player);
        String name = player.getName();
        String sha1 = name + String.valueOf(number);
		Storage	storage	= new Storage(Properties.storageVersion, sha1);
        Shop p = new SellShop(getScs(), storage);
        		p.setUnlimited	(false							      );
        		p.setItemStack	(is                                   );
        		p.setPrice		(price							      );
        		p.setOwner		(player.getName()		              );
        		p.setLocation   (loc                                  );
        		p.setAmount		(amount						   	      );
        		p.setVisible    (true                                 );
       getScs().getShopHandler().addShop(p);
       getShops(rs).set("Shops." + player.getName() +"."+ String.valueOf(number), Utils.LocationToString(p.getLocation()));
       rs.saveShops();
       RemoteShowcase.Message(player, "&eShop created! &7ID: " + String.valueOf(number));
       Utils.economy.withdrawPlayer(player.getName(), rs.getConfig().getDouble("Price-To-Create"));
	}
	public static Location getOpenShop(RemoteShowcase plugin, World w) throws Exception{
		int lowest = 0;
		int temp   = 0;
		Location loc;
		String xyz = null;
		Set<String> list = plugin.getConfig().getKeys(true);
		for(String key : list) {
			if(key.startsWith("Open-Shops.")) {
				if(plugin.getConfig().getString(key) != "Inactive") {
					key = key.replace("Open-Shops.", "");
					if(temp != 0) {
						if(Integer.valueOf(key) < temp) {
							temp = Integer.valueOf(key);
						}
					}else{
						temp = Integer.valueOf(key);
					}
				}
			}
		}
		lowest = temp;
		xyz = plugin.getConfig().getString("Open-Shops." + lowest);
		plugin.getConfig().set("Open-Shops." + lowest, "Inactive");
		if(xyz == null) return null;
		int x,y,z;
		String split[] = xyz.split(",");
			x = Integer.valueOf(split[0]);
			y = Integer.valueOf(split[1]);
			z = Integer.valueOf(split[2]);
		loc = w.getBlockAt(x,y,z).getLocation();
		return loc;
	}
	public static void Open(Player p,Location loc, RemoteShowcase rs) {
		Open(p,loc,rs,true);
	}
	public static void Open(Player p,Location loc, RemoteShowcase rs, boolean open) {
		if(!Utils.HasPermission(p, "Admin")) {
			RemoteShowcase.ErrorMessage(p, "You do not have permission");
			return;
		}
		PlayerListener.pList.add(p);
		RemoteShowcase.Message(p, "&aClick &7the block you want to open!");
		
	}
	public static void MarkAsOpen(Player p,Location loc, RemoteShowcase rs, boolean open) {
		String x = String.valueOf(loc.getBlockX());
		String y = String.valueOf(loc.getBlockY());
		String z = String.valueOf(loc.getBlockZ());
		rs.getConfig().set("Open-Shops." + getLowest(rs), x+","+y+","+z);
		rs.saveConfig();
		if(open)
			RemoteShowcase.Message(p, "&aShop opened!");
	}
	public static void Restock(RemoteShowcase rs,ShowCaseStandalone scs, String sha1, int amount, Player p) {
		if(!Utils.HasPermission(p, "Add")){
			RemoteShowcase.ErrorMessage(p, "You do not have permission");
			return;
		}
		if(isInactive(rs,"Shops." + p.getName() + "." + sha1)){
			RemoteShowcase.ErrorMessage(p, "Shop does not exist");
			return;
		}
		Shop shop = null;
		Location loc = Utils.StringToLocation(rs, Bukkit.getServer().getWorld("world"), p.getName() + "." + sha1);
		if(loc==null) {
			RemoteShowcase.ErrorMessage(p, "Shop not found!");
			System.out.println("Location is null");
			return;
		}
		try {
			shop = getScs().getShopHandler().getShopForBlock(loc.getBlock());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(shop == null) {
			RemoteShowcase.ErrorMessage(p, "Shop not found!");
			return;
		}
		ItemStack stack = shop.getItemStack();
		stack.setAmount(amount);
		if(!hasItemStack(stack,p)){
			RemoteShowcase.ErrorMessage(p, "You are not holding "+String.valueOf(amount)+" x " + stack.getType().toString().toLowerCase());
			return;
		}
		shop.setAmount(shop.getAmount() + amount);
		RemoteShowcase.Message(p, "&aItems added!");
		
		   
		        
	}
	public static void Remove(RemoteShowcase rs, String sha1, Player p) throws Exception {
		World w = Bukkit.getWorld("world");
		Location loc = Utils.StringToLocation(rs,w,p.getName() +"."+sha1);
		if(isInactive(rs,"Shops." + p.getName() +"."+sha1)) {
			RemoteShowcase.ErrorMessage(p, "Shop does not exist");
			return;
		}
		Shop shop = getScs().getShopHandler().getShopForBlock(loc.getBlock());
		getScs().getShopHandler().removeShop(shop);
		RemoteShowcase.Message(p, "&aShop removed!");
		getShops(rs).set("Shops." + p.getName() + "."  +sha1, "Inactive");
		rs.saveShops();
 		MarkAsOpen(p, loc, rs, false);
		
	}
	public static void List(RemoteShowcase rs, Player p) throws Exception {
		if(getPlayerShops(rs,p).isEmpty()){
			RemoteShowcase.ErrorMessage(p, "You have no shops!");
			return;
		}
		for(Shop shop : getPlayerShops(rs,p)){
			ConvertToListMessage(shop);
		}
	}
	public static void ConvertToListMessage(Shop s) {
		Player p = s.getPOwner();
		RemoteShowcase.Message(p, "&a--< " + s.getUSID().replace(p.getName(), "")                       );
		RemoteShowcase.Message(p,"&a- Item: &7"  + Utils.capFirst(s.getItemStack().getType().toString()));
		RemoteShowcase.Message(p,"&a- Amount: &7"+ s.getAmount()                                        );
		RemoteShowcase.Message(p,"&a- Price: &7" + s.getPrice()                                         );



	}
	
	public static List<Shop> getPlayerShops(RemoteShowcase rs,Player p) throws Exception {
		String pName = p.getName();
		List<Shop> shops = new LinkedList<Shop>();
		for(String s : rs.getShops().getKeys(true)) {
			if(s.startsWith("Shops." + pName + ".")){
				if(!isInactive(rs,s)){
					Shop shop = getScs().getShopHandler().getShopForBlock(Utils.StringToLocation(rs,rs.getShops().getString(s)).getBlock());
					shops.add(shop);
				}
			}
		}
		return shops;
	}
	public static boolean hasItemStack(ItemStack stack, Player p){
		if(p.getItemInHand().equals(stack)){
			removeItemStack(stack,p,true);
			return true;
		}else {
			if(stack.getAmount() < p.getItemInHand().getAmount()){
				ItemStack iss = stack.clone();
				iss.setAmount(1);
				ItemStack is = p.getItemInHand().clone();
				is.setAmount(1);
				if(is.equals(iss)){
					removeItemStack(stack,p,false);
					return true;
				}
			}
			
		}
		return false;
	}
	public static void removeItemStack(ItemStack s, Player p, boolean tf) {
		if(tf){
			p.setItemInHand(null);
		}else{
			ItemStack toSet = p.getItemInHand().clone();
			toSet.setAmount(toSet.getAmount() - s.getAmount());
			p.setItemInHand(toSet);
		}
	}
	public static ShowCaseStandalone getScs(){
		return RemoteShowcase.scs;
	}
	public static FileConfiguration getShops(RemoteShowcase rs){
		return rs.getShops();
	}
	public static int getNumber(RemoteShowcase rs, Player p){
		return Utils.getNumber(rs, p.getName());
	}
	public static boolean hasMoney(Player p, double amount){
		return (Utils.economy.getBalance(p.getName()) >= amount);
	}
	public static boolean isInactive(RemoteShowcase rs,String s) {
		return  rs.getShops().getString(s) != null && rs.getShops().getString(s).equals("Inactive");
	}
	public static int getLowest(RemoteShowcase plugin) {
		int lowest = 0;
		int temp   = 0;
		Location loc;
		String xyz = null;
		Set<String> list = plugin.getConfig().getKeys(true);
		for(String key : list) {
			if(key.startsWith("Open-Shops.")) {
				if(plugin.getConfig().getString(key) != "Inactive") {
					key = key.replace("Open-Shops.", "");
					if(temp != 0) {
						if(Integer.valueOf(key) > temp) {
							temp = Integer.valueOf(key);
						}
					}else{
						temp = Integer.valueOf(key);
					}
				}
			}
		}
		lowest = temp;
		return lowest + 1;
	}
}
