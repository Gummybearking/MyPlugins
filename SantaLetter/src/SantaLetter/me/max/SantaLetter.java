package SantaLetter.me.max;

import java.io.File;
import java.util.logging.Logger;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class SantaLetter extends JavaPlugin implements Listener{
	public final Logger logger = Logger.getLogger("Minecraft");
	 
	public void onEnable() {
	
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Enabled!");
		this.getServer().getPluginManager().registerEvents(this, this);
		File config = new File(this.getDataFolder(), "config.yml");
		if(!config.exists()) {
			this.saveDefaultConfig();
		}
	}
			
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");
	}
	
	public Location getChest() {
		String xyz     = getConfig().getString("LetterBox");
		String s[] = xyz.split(",");
				return 
				Bukkit.getWorld("world").getBlockAt(i(s[0]), i(s[1]), i(s[2])).getLocation();
	}
	
	public int i(String s) {
		return Integer.valueOf(s);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(label.equalsIgnoreCase("slc")) {
			Player p = (Player) sender;
			if(!p.isOp())
				return false;
			Block b = p.getTargetBlock(null, 10);
			if(b == null)
				return false;
			if(b.getType() != Material.CHEST)
				return false;
			getConfig().set("LetterBox", b.getX() + "," + b.getY() + "," + b.getZ());
			saveConfig();
		}
		return true;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(!e.hasBlock())
			return;
		if(e.getClickedBlock().getState() instanceof Chest) {
			if(!e.getClickedBlock().getLocation().equals(getChest()))
				return;
			if(e.getAction() != Action.LEFT_CLICK_BLOCK){
				if(!p.isOp()) {
					e.setCancelled(true);
				}
				return;
			}
			if(p.getItemInHand().getType() == Material.WRITTEN_BOOK) {
				ItemStack stack = p.getItemInHand();
				Chest ch = (Chest) e.getClickedBlock().getState();
				Inventory inv = ch.getBlockInventory();
				if(inv.firstEmpty() != -1) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou letter has been sent to &cSanta!"));
					inv.addItem(stack);
					p.setItemInHand(new ItemStack(Material.AIR));
					e.setCancelled(true);
				}else{
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSanta &aalready has too many letters!"));
					e.setCancelled(true);
				}
			}
		}
	}
}
