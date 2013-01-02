package ChestArena.me.max.java;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockListener implements Listener{
	public ChestArena plugin;
	public BlockListener(ChestArena p){
		plugin = p;
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.isCancelled()) return;
		Block b = e.getBlock();
		if (b.getLocation().toString().equals(plugin.ArenaChestBlock.getLocation().toString())) {
			e.setCancelled(true);
		}
		
	}
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.isCancelled()){
            return;
        }
        Block b = event.getBlock();
        if(b.getLocation().toString().equals(plugin.ArenaChestBlock.getLocation().toString())){
        	event.setCancelled(true);
        }
	}
	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
       if (event.isCancelled()){
           return;
       }
       Block b = event.getBlock();
       if(b.getLocation().toString().equals(plugin.ArenaChestBlock.getLocation().toString())){
       	event.setCancelled(true);
       }
	}
	@EventHandler
	public void onExplosion(EntityExplodeEvent e){
		List<Block> blocks = e.blockList();
		if(blocks.contains(plugin.ArenaChestBlock)){
			e.blockList().remove(plugin.ArenaChestBlock);
		}
	}
	

}
