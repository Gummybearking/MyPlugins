package ChestArena.me.max.java;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import org.wargamer2010.capturetheportal.CaptureThePortal;
import org.wargamer2010.capturetheportal.CapturesStorage;
import org.wargamer2010.capturetheportal.CapturesStorage.CaptureInformation;

public class ChestArena extends JavaPlugin{
	public long time;
	public long timeL = 0;
	public final Logger logger = Logger.getLogger("Minecraft");
    public static Permission permission = null;
    public static Economy economy = null;
    public static Chat chat = null;
    public World arena;
    public static boolean isGame = false;
    public Chest arenaChest;
    public SimpleClans sc;
    public Material mat;
    public Block ArenaChestBlock;
    
    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    public void onEnable() {
    	PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Enabled!");
		setupPermissions();
		setupEconomy();
		//Clans
		Plugin plug = getServer().getPluginManager().getPlugin("SimpleClans");

	    if (plug != null)
	    {
	        sc = ((SimpleClans) plug);
	    }
		getServer().getPluginManager().registerEvents(new gameHandler(this), this);
		getServer().getPluginManager().registerEvents(new BlockListener(this), this);

		File config = new File(this.getDataFolder(), "config.yml");
		if(!config.exists()){
			this.saveDefaultConfig();
			System.out.println("[ChestArena] No config.yml detected, config.yml created");

		}
		if(getConfig() == null) System.out.println("\nNULL CONFIG?\n");
		arena = getServer().getWorld(getConfig().getString("Arena-World"));
		
		arenaTimer();
		Block chest = arena.getBlockAt( getConfig().getInt( "Chest.x" ), getConfig().getInt( "Chest.y" ), getConfig().getInt( "Chest.z" ) );
		ArenaChestBlock = chest;
		if( chest.getType() == Material.CHEST )
		{
			Chest c = (Chest)chest.getState();
			arenaChest = c;
			chest.setType(Material.AIR);

		}else{
		
		}
		time  = ( ( ( getConfig().getLong("Game-Interval.hours") *60) *60) +  ( getConfig().getLong("Game-Interval.minutes") *60) );
		timeL = getConfig().getLong("Time-Left");
		System.out.println(this.getDataFolder().toURI().toString());
		
		

	}
		
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Has Been Disabled!");
		try{
			unSetupArena();
		}catch(Exception e){
			
		}
		reloadConfig();
		getConfig().set("Time-Left", timeL);
		saveConfig();
	}
	
	public void setupArena()
	{

		arena.strikeLightningEffect(ArenaChestBlock.getLocation());
		ArenaChestBlock.setType(Material.CHEST);
		Chest c = new Chest()
		if(ArenaChestBlock.getState().getType() == Material.CHEST){
			arenaChest = (Chest) ArenaChestBlock.getState();
		}else{
			System.out.println("\n\n\nSevere Error: ChestBlock is not a Chest?\n\n");
		}
		setupChest();
		Block b = arena.getBlockAt(arenaChest.getX(), arenaChest.getY() - 1, arenaChest.getZ());
		mat = b.getType();
		b.setType(Material.GLOWSTONE);
		timeL = 0;
		
	}
	
	public void unSetupArena()
	{
		arenaChest.getBlockInventory().clear();
		ArenaChestBlock.setType(Material.AIR);
		arena.strikeLightning(ArenaChestBlock.getLocation());
		final Block b = arena.getBlockAt(ArenaChestBlock.getX(), ArenaChestBlock.getY() - 1, ArenaChestBlock.getZ());
		b.setType(mat);
		 
	}
	
	public void setupChest()
	{
		Block chest = arena.getBlockAt( getConfig().getInt( "Chest.x" ), getConfig().getInt( "Chest.y" ), getConfig().getInt( "Chest.z" ) );
		if( chest.getType() == Material.CHEST )
		{
			
		}else{
			System.out.println("[ERROR][ChestArena] The Specified block is NOT a chest it is a(n) " + chest.getType().toString());
			return;
		}
		Chest c = (Chest)chest.getState();
		c.getBlockInventory().clear();
		rewardHandler.setRewards(this, c);
		if(arenaChest == null)
		{
			arenaChest = c;
		}

	}
	
	
	
	
	public boolean onCommand(CommandSender Sender, Command cmd, String commandLabel, String[] args) {
		Player p = null;
		if( Sender instanceof Player ){
			
			p = (Player)Sender;
			
			if(commandLabel.equalsIgnoreCase("ca")){
				if(permission.has(p,"ChestArena.admin") || p.isOp()){
					if(args.length == 1)
					{
						System.out.println(args[0]);
						if(args[0] .equalsIgnoreCase("choose")) Commands.Choose(p);
						if(args[0] .equalsIgnoreCase("start")) Commands.Start();
						if(args[0] .equalsIgnoreCase("end")) Commands.End();

					}
				}
			}
			if(commandLabel.equalsIgnoreCase("ChestArena")) {
				double minutes = Math.floor(((time-timeL)%3600)/60);
				double seconds =             ((time-timeL)%3600)%60;
				double hours   =   Math.floor(((time-timeL)/60)/60);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[ChestArena] &eThe next ChestArena will begin in: &d " +hours+ "h " +minutes+ "m " +seconds+ "s").replace(".0", ""));
			
			}
			if(commandLabel.equalsIgnoreCase("portal")) {
				List<CaptureInformation> caps = new LinkedList<CaptureInformation>();
				for(Location l : CaptureThePortal.Storage.getAllCaptures().keySet()) {
					caps.add(CaptureThePortal.Storage.getAllCaptures().get(l));
				}
				for (CaptureInformation cap : caps) {
					double total = cap.cooldownleft;
					double minutes = Math.floor(((total)%3600)/60);
					double seconds =             ((total)%3600)%60;
					double hours   =   Math.floor(((total)/60)/60);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3[CTP] &fTime Until next CTP Game: &9" +hours+ "h " +minutes+ "m " + seconds+ "s"));
				}
			}
				
			
		}else{
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("start"))   Commands .Start();
				if(args[0].equalsIgnoreCase(  "end"))   Commands   .End();
			}
			if(commandLabel.equalsIgnoreCase("ChestArena")) {
				double minutes = Math.floor(((time-timeL)%3600)/60);
				double seconds =             ((time-timeL)%3600)%60;
				double hours   =   Math.floor(((time-timeL)/60)/60);
				System.out.println(ChatColor.translateAlternateColorCodes('&', "[ChestArena] &eThe next ChestArena will begin in: " +hours+ "h " +minutes+ "m " +seconds+ "s").replace(".0", ""));
			
			}
			if(commandLabel.equalsIgnoreCase("portal")) {
				List<CaptureInformation> caps = new LinkedList<CaptureInformation>();
				for(Location l : CaptureThePortal.Storage.getAllCaptures().keySet()) {
					caps.add(CaptureThePortal.Storage.getAllCaptures().get(l));
				}
				for (CaptureInformation cap : caps) {
					double total = cap.cooldownleft;
					double minutes = Math.floor(((total)%3600)/60);
					double seconds =             ((total)%3600)%60;
					double hours   =   Math.floor(((total)/60)/60);
					System.out.println(ChatColor.translateAlternateColorCodes('&', "[CTP] &fTime Until next CTP Game: " +hours+ "h " +minutes+ "m " + seconds+ "s"));
				}
			}
			
		}
		
		//return if console command
		
		
		return false;
		
	}
	
	public void arenaTimer(){
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
             public void run() {
         		if(timeL >= time) 
         		{
         			gameHandler.startGame();
         			return;
         		}else timeL++;
         		handleMessages();
            	if(!isGame)
         		{
         			arenaTimer();
         		}
             }
         },  20L);
	}
	public void handleMessages() {
		if(timeL%60 == 0) {
			long minutes = timeL /60;
			long tminus  = time  /60;
			minutes = tminus - minutes;
			double hours = Math.floor(minutes/60);
			double mins  = minutes%60; 
			//If the number ends in "0"
			if(String.valueOf(String.valueOf(minutes).charAt(String.valueOf(minutes).length() - 1)).equals("0")) {
				if(hours != 0) 
					broadcast(("The ChestArena will begin in: &d" +hours+"h "+ mins + "m").replace(".0", ""));
				else
					broadcast(("The ChestArena will begin in: &d" + mins + "m").replace(".0", ""));
				System.out.println(String.valueOf(minutes).charAt(String.valueOf(minutes).length() - 1));

			}else{
				System.out.println(String.valueOf(minutes).charAt(String.valueOf(minutes).length() - 1));
			}
		}
	}
	public static void broadcast(String s)
	{
		Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6[ChestArena] &e" + s));
	}
}
