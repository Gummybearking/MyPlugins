package me.max;

import java.util.logging.Logger;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;



public class portaportal extends JavaPlugin {
    public static Permission permission = null;
    public static Economy economy = null;
    public static Chat chat = null;
    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

 

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
    public static Permission perms = null;
	public static Economy econ = null;
	public final Logger logger = Logger.getLogger("Minecraft");
	public void onEnable() {
		setupEconomy();
		setupPermissions();
		setupChat();
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Enabled!");
		getConfig().options().copyDefaults(true);
		saveConfig();

		
	}
		
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");

	}
 
	public boolean onCommand(CommandSender Sender, Command cmd, String commandLabel, String[] args){
		final Player player = (Player) Sender;
		if (commandLabel.equalsIgnoreCase("portal")) {
		


			if(permission.has(player, "portal.portal")){
				if(player.getInventory().contains(getConfig().getInt("item"), getConfig().getInt("amount"))){
					
					player.getInventory().removeItem(new ItemStack(getConfig().getInt("item"), getConfig().getInt("amount")));
					final Location location = player.getLocation();
					player.getLocation().getBlock().setTypeId(90);
					player.sendMessage(ChatColor.DARK_PURPLE + "Portal Created!");
					this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
 						   public void run() {
							   location.getBlock().setTypeId(0);
							   player.sendMessage(ChatColor.DARK_PURPLE + "Portal Destroyed");
						      
						   }
						}, 300L);
				}else{
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Error: You need more " + getConfig().getInt("item"));
				}

		
			

					
			}else{
				player.sendMessage(ChatColor.LIGHT_PURPLE + "Error: You Don't have Permission");
			}
			}
				


		return false;
			
	}

	
	
}
