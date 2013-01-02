package RS.java.me.max;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;



import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.kellerkindt.scs.ShowCaseStandalone;



public class RemoteShowcase extends JavaPlugin{

    public File shops = null;
    public FileConfiguration shopsc = null;
	
	public final Logger logger = Logger.getLogger("Minecraft");
	public static ShowCaseStandalone scs;
	public void onEnable() {
    	PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Enabled!");
		
		this.getServer().getPluginManager().registerEvents(new ShowcaseListener (this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener   (this), this);
		
		Utils.setupEconomy();
		Utils.setupPermissions();
		Plugin plug = getServer().getPluginManager().getPlugin("ShowCaseStandalone");
		File config = new File(this.getDataFolder(), "config.yml");
	    if (plug != null)
	    {
	        scs = ((ShowCaseStandalone) plug);
	    }
	    reloadCustomConfig();
		getShops().options().copyDefaults(true);
		saveShops();
		if(!config.exists()){
			this.saveDefaultConfig();
			System.out.println("[RemoteShowcase] No config.yml detected, config.yml created");

		}
	}
    public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");
	}
    public boolean onCommand(CommandSender Sender, Command cmd, String commandLabel, String[] args) {
    	if( !(Sender instanceof Player) ) {
    		System.out.println("\n\nOnly a player may do that command\n");
    		return true;
    	}
    	Commands c = new Commands();
    	Player p = (Player) Sender;
    	if(!(commandLabel.equalsIgnoreCase("shop"))){
    		return false;
    	}
    	if(args.length < 1){
    		Commands.Help(p);
    		return true;
    	}else{
    		switch(args[0].toLowerCase()){
    			case "sell":
    				if(args.length == 3){
    					try{
    						c.Sell(this, p, Integer.valueOf(args[1]), Integer.valueOf(args[2]));
    					}catch(Exception e){
    						ErrorMessage(p,"The argumnents caused an internal error");
    						e.printStackTrace();
    					}
    				}else{
    					ErrorMessage(p, "Correct usage: /Shop sell <amount> <price>");
    				}
    				break;
    			case "buy":
    				if(args.length == 3){
    					try{
    						c.Buy(this, p, Integer.valueOf(args[1]), Integer.valueOf(args[2]));
    					}catch(Exception e){
    						ErrorMessage(p,"The argumnents caused an internal error");
    						e.printStackTrace();
    					}
    				}else{
    					ErrorMessage(p, "Correct usage: /Shop buy <MaxAmount> <Price>");
    				}
    				break;
    			case "create":
    				Block b = p.getTargetBlock(null, 20);
    				if(b!=null)
    					Commands.Open(p,b.getLocation(), this);
    				else
    					ErrorMessage(p, "No block in sight (20 block range)");
    				break;
    			case "add":
    				if(args.length == 3) {
    					try {
							Commands.Restock(this, scs, args[1], Integer.valueOf(args[2]), p);
						} catch (Exception e) {
    						ErrorMessage(p,"The argumnents caused an internal error");
							e.printStackTrace();
						} 
    				}else{
    					ErrorMessage(p, "Correct usage: /Shop add <ShopId> <amount>");
    				}
    				break;
    			case "remove":
    				if(args.length == 2){
    					try {
							Commands.Remove(this, args[1], p);
						} catch (Exception e) {
							ErrorMessage(p,"Shop does not exist!");
						}
    				}else{
    					ErrorMessage(p, "Correct usage: /Shop remove <ShopId>");
    				}
    				break;
    			case "list":
    				try {
    					Commands.List(this, p);
    				} catch (Exception e) {
    					ErrorMessage(p, "An internal error has occured");
    					e.printStackTrace();
    				}
    				break;
    			case "help":
    				Commands.Help(p);
    				break;
    			default:
    				Commands.Help(p);
    				break;
    		}
    	}
		return false;
    	
    }
    public static void ErrorMessage(Player p, String message){
    	p.sendMessage(ChatColor.RED + "Error: " + message);
    }
    public static void Message(Player p, String message){
    	p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
    
    
    
    
    
    
    public void reloadCustomConfig() {
	    if (shops == null) {
	    shops = new File(getDataFolder(), "shops.yml");
	    }
	    shopsc = YamlConfiguration.loadConfiguration(shops);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = this.getResource("shops.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        shopsc.setDefaults(defConfig);
	    }
	    
	}
	public FileConfiguration getShops() {
	    if (shopsc == null) {
	        this.reloadCustomConfig();
	    }
	    return shopsc;
	}
	public void saveShops() {
	    if (shopsc == null || shops == null) {
	    	System.out.println("Null");
	    	return;
	    }
	    try {
	        getShops().save(shops);
	    } catch (IOException ex) {
	        this.getLogger().log(Level.SEVERE, "Could not save config to " + shops, ex);
	    }
	}
   
    
}
