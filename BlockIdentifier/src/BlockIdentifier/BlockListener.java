package BlockIdentifier;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class BlockListener implements Listener {
	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			Block block = event.getClickedBlock();
			if (BlockId.map.containsKey(event.getPlayer())){
				event.getPlayer().sendMessage(block.getType() + ": " + block.getType().getId());
			}
			if(BlockId.map2.containsKey(event.getPlayer())){
				event.getPlayer().sendMessage("Top of Block Light Level: " + block.getRelative(BlockFace.UP).getLightLevel());
				event.getPlayer().sendMessage("Bottom of Block Light Level: " + block.getRelative(BlockFace.DOWN).getLightLevel());
			}
		}
	}

}
