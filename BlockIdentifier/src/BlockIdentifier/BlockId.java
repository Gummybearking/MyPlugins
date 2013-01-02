package BlockIdentifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;




public class BlockId extends JavaPlugin {
	public static Map<Player, ArrayList<Block>> map = new HashMap<Player, ArrayList<Block>>();
	public static Map<Player, ArrayList<Block>> map2 = new HashMap<Player, ArrayList<Block>>();
	public final Logger logger = Logger.getLogger("Minecraft");
	public void onEnable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Enabled!");
		this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
	}
		
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");
	}
	public boolean onCommand(CommandSender Sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) Sender;
		if (commandLabel.equalsIgnoreCase("getid")){
			ItemStack stack = player.getInventory().getItemInHand();
			player.sendMessage(stack.getType() + ": " + stack.getType().getId());
		}
		if (commandLabel.equalsIgnoreCase("getBlock")){
			if (map.containsKey(player)){
				map.remove(player);
				player.sendMessage("Block Identification Deactivated");
			}else{
				map.put(player, null);
				player.sendMessage("Block Identification Activated");
			}

		}
		if (commandLabel.equalsIgnoreCase("getlight")){
			if(map2.containsKey(player)){
				map2.remove(player);
				player.sendMessage("Light Identification Deactivated");
			}else{
				map2.put(player, null);
				player.sendMessage("Light Identification Activated");
			}
		}
		return false;

		
	}

}
