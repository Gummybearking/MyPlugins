package ChestArena.me.max.java;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class rewardHandler {
	public static List<String> rewardsList = new LinkedList<String>();
	static String rareReward;
	static String bookReward;
	public static String parseForItemType(String s){
		int firstColon = s.indexOf(';');
		s = s.substring(0,firstColon);
		return s.toUpperCase();
	}
	public static ItemStack parseForItemId(String s){
		String mod = parseForItemType(s);
		String split[] = mod.split("-");
		int id = Integer.valueOf(split[0]);
		int amount = Integer.valueOf(parseForAmount(s));
		
		if(split.length == 2){
			short data = Short.valueOf(split[1]);
			ItemStack item = new ItemStack(Material.getMaterial(id), amount, data);
			return item;
		}
		ItemStack item = new ItemStack(Material.getMaterial(id), amount);
		return item;
	}
	public static String parseForAmount(String s){
		int firstColon = s.indexOf(';');
		String mod = s.substring(firstColon + 1);
		int secondColon = mod.indexOf(';');
		s = mod.substring(0,secondColon);
		s = s.replace(";", "");
		String split[] = s.split("-");
		int lower = Integer.valueOf(split[0]);
		int larger = Integer.valueOf(split[1]);
		Random r = new Random();
		int toAdd = 0;
		if(lower != larger)
			toAdd = r.nextInt(larger - lower);
		s = String.valueOf(lower + toAdd);


		return s;
	}
	public static String parseForChance(String s){
		int firstColon = s.indexOf(';');
		String mod = s.substring(firstColon + 1);
		int secondColon = mod.indexOf(';');
		s = mod.substring(secondColon+1);
		int thirdColon = s.indexOf(';');
		
		if(thirdColon != -1){
			s = s.substring(0,thirdColon);
		}
			
		s = s.replace(";", "");
		return s;
	}
	public static String parseForEnchants(String s){
		int firstColon = s.indexOf(';');
		String mod = s.substring(firstColon + 1);
		int secondColon = mod.indexOf(';');
		s = mod.substring(secondColon+1);
		int thirdColon = s.indexOf(';');
		
		if(thirdColon != -1){
			s = s.substring(thirdColon + 1);
			int fourthColon = s.indexOf(";");
			if(fourthColon != -1){
				s = s.substring(0,fourthColon);
				return s;
			}else return s;
		}else return null;
	}
	public static String parseForTitle(String s) {
		if(s.contains("#")){ 
				String[] Split = parseForTitleLore(s).split("#");
				return ChatColor.translateAlternateColorCodes('&', Split[0]);
		}
		return null;
	}
	public static String[] parseForLore(String s) {
		if(parseForTitleLore(s) == null)
			return null;
		String[] Split = parseForTitleLore(s).split("#");
		if(Split.length < 2) return null;
		if(Split[1].contains("-")){
			String[] Split2 = Split[1].split("-");
			for(String str : Split2) {
				str = ChatColor.translateAlternateColorCodes('&', str);
			}
			return Split2;
		}
		return Split;
	}
	public static String parseForTitleLore(String s){
		int firstColon = s.indexOf(';');
		String mod = s.substring(firstColon + 1);
		int secondColon = mod.indexOf(';');
		s = mod.substring(secondColon+1);
		int thirdColon = s.indexOf(';');
		
		if(thirdColon != -1){
			s = s.substring(thirdColon + 1);
			int fourthColon = s.indexOf(";");
			if(fourthColon != -1){
				s = s.substring(fourthColon + 1);
				return s;
			}else return null;
		}else return null;
	}
	public static List<String> getAllPossible(ChestArena plugin)
	{
		return plugin.getConfig().getStringList("Chest.Rewards");
	}
	
	
	public static void setRewards(ChestArena plugin, Chest chest)
	{
		ItemStack Rare = getRareItem(plugin);
		ItemStack Book = getBooks(plugin);
		
		if(Rare != null)
			chest.getBlockInventory().addItem(Rare);
		if(Book != null)
			chest.getBlockInventory().addItem(Book);
		
		List<String> passedFirst = new LinkedList<String>();
		for(String s : getAllPossible(plugin)){
			Random firstParse = new Random();
			int itemChance =  2;
			if(Integer.valueOf( parseForChance( s ) ) == 100){
				itemChance = 1;
				passedFirst.add(s);
				
				
			}else if(Integer.valueOf( parseForChance( s ) ) > 50) 
			{
				itemChance =  Math.round(100 / (100-Integer.valueOf( parseForChance( s ) ) ) );
				int passFirst = firstParse.nextInt(itemChance - 1);
				if(passFirst != 0)
				{
					passedFirst.add(s);
					
				}
			}else if(Integer.valueOf( parseForChance( s ) ) < 50)
			{
				itemChance =  Math.round( 100 / Integer.valueOf( parseForChance( s ) ) );
				int passFirst = firstParse.nextInt(itemChance - 1);
				if(passFirst == 0)
				{
					passedFirst.add(s);
				}

			}else{
				int passFirst = firstParse.nextInt(1);
				if(passFirst == 0) passedFirst.add(s);
			}
			
		}
		Collections.shuffle(passedFirst);
		for(String itemS : passedFirst){
			chest.getBlockInventory().addItem(convertToItem(itemS));
		}
		
		
	}
	public static List<String> splitEnchants(String s)
	{
		if(s == null) return null;
		List<String> list = new LinkedList<String>();
		String enchString = parseForEnchants(s);
		if(enchString == null) return null;
		if(!(enchString.contains(","))){
			list.add(enchString);
		}
		String spilts[] = enchString.split(",");
		for(String enchant : spilts){
			list.add(enchant);
		}
		return list;
		
	}
	public static void addEnchants(String s, ItemStack i){
		List<String> list = splitEnchants(s);
		if(list.isEmpty()) return;
		for(String ench : list){
			
			String spilt[] = ench.split("-");
			Enchantment enchantment = Enchantment.getByName( spilt[0].toUpperCase() );
			int level = Integer.valueOf(spilt[1]);
			if(enchantment == null) return;
			i.addUnsafeEnchantment(enchantment, level);
		}
	}
	public static ItemStack convertToItem(String s)
	{
		if(parseForTitle(s) == null) rewardsList.add(s);
		if ( Material.getMaterial( parseForItemType(s) )  == null){
			ItemStack converted = parseForItemId(s);
			if(splitEnchants(s) == null) 
			{
				return converted;
			}else
			{
				addEnchants(s, converted);
				return converted;
			}
		}
		ItemStack converted = new ItemStack(Material.getMaterial( parseForItemType(s) ), Integer.valueOf( parseForAmount(s) ) );
		if(splitEnchants(s) == null) 
		{			
			return converted;
		}else
		{
			addEnchants(s, converted);
			return converted;
		}
		
		
	}
	public static ItemStack getRareItem(ChestArena plugin){

		List<String> list = plugin.getConfig().getStringList("Chest.Rares");
		ItemStack Rare = null;
		Random firstParse = new Random();
		int chooseRare = firstParse.nextInt(list.size());
		String chosen = list.get(chooseRare);
		//Now get the percentage
		int itemChance =  2;
		if(Integer.valueOf( parseForChance( chosen ) ) == 100){
			Rare = convertToItem(chosen);
			rareReward = chosen;
			
		}else if(Integer.valueOf( parseForChance( chosen ) ) > 50) 
		{
			itemChance =  Math.round(100 / (100-Integer.valueOf( parseForChance( chosen ) ) ) );
			int passFirst = firstParse.nextInt(itemChance - 1);
			if(passFirst != 0)
			{
				Rare = convertToItem(chosen);
				rareReward = chosen;
			}
		}else if(Integer.valueOf( parseForChance( chosen ) ) < 50)
		{
			itemChance =  Math.round( 100 / Integer.valueOf( parseForChance( chosen ) ) );
			int passFirst = firstParse.nextInt(itemChance - 1);
			if(passFirst == 0)
			{
				Rare = convertToItem(chosen);
				rareReward = chosen;
			}
		}else if(Integer.valueOf( parseForChance( chosen ) ) > 50){
			int passFirst = firstParse.nextInt(1);
			if(passFirst == 0)
			{
				Rare = convertToItem(chosen);
				rareReward = chosen;
			}
		}
		if(Rare != null) {
			Rare = renameItem(Rare,parseForTitle(chosen), parseForLore(chosen));
		}
		return Rare;
		
	}
	public static ItemStack getBooks(ChestArena plugin){

		List<String> list = plugin.getConfig().getStringList("Chest.Books");
		ItemStack Rare = null;
		Random firstParse = new Random();
		int chooseRare = firstParse.nextInt(list.size());
		String chosen = list.get(chooseRare);
		//Now get the percentage
		int itemChance =  2;
		if(Integer.valueOf( parseForChance( chosen ) ) == 100){
			Rare = convertToItem(chosen);
			bookReward = chosen;
			
		}else if(Integer.valueOf( parseForChance( chosen ) ) > 50) 
		{
			itemChance =  Math.round(100 / (100-Integer.valueOf( parseForChance( chosen ) ) ) );
			int passFirst = firstParse.nextInt(itemChance - 1);
			if(passFirst != 0)
			{
				Rare = convertToItem(chosen);
				bookReward = chosen;
			}
		}else if(Integer.valueOf( parseForChance( chosen ) ) < 50)
		{
			itemChance =  Math.round( 100 / Integer.valueOf( parseForChance( chosen ) ) );
			int passFirst = firstParse.nextInt(itemChance - 1);
			if(passFirst == 0)
			{
				Rare = convertToItem(chosen);
				bookReward = chosen;
			}
		}else if(Integer.valueOf( parseForChance( chosen ) ) > 50){
			int passFirst = firstParse.nextInt(1);
			if(passFirst == 0)
			{
				Rare = convertToItem(chosen);
				bookReward = chosen;
			}
		}
		if(Rare != null) {
			Rare = renameItem(Rare,parseForTitle(chosen), parseForLore(chosen));
		}
		return Rare;
		
	}
	public static void printRewards(ChestArena arena){
		Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6[ChestArena] " + arena.getConfig().getString("Messages.Say-Rewards")));
		for(String s : rewardsList){
			String typeS     = null;
			String amountS   = null;
			String itemS = null;
			
			if(Material.getMaterial(parseForItemType(s)) == null){
				typeS = parseForItemId(s).getType().toString().toLowerCase();
				typeS = capFirst(typeS);
				
			}else{
				typeS = parseForItemType(s).toLowerCase();
				typeS = capFirst(typeS);
			}
			
			//amount
			amountS = parseForAmount(s);
			//enchants
			List<String> list = splitEnchants(s);
			List<String> enchants = new LinkedList<String>();
 			if(list != null&&!(list.isEmpty())) {
 				for(String ench : list){
 					
 					String spilt[] = ench.split("-");
 					String EnchantType = spilt[0].toLowerCase();
 					String level =  spilt[1];
 					enchants.add(EnchantType + "-" + level);
 				}
 			}	
			if(amountS.equals("1")){
				if(enchants.isEmpty()){
					itemS = typeS.replace("_", " ");
				}else{
					itemS = typeS.replace("_", " ") + ": " + rd(enchants);
					itemS = itemS.replace("_", " ").replace("[", "").replace("]", "");

				}
				Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e- " + itemS));
			
			}else{
				if(enchants.isEmpty()){
					itemS = amountS + " " + typeS + "s";
				}else{
				
					itemS = amountS + " " + typeS + "s: " + rd(enchants);
					itemS = itemS.replace("_", " ").replace("[", "").replace("]", "");
				}
				Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e- " + itemS));

			}

		}
		if(bookReward != null && parseForTitle(bookReward) != null)
			Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b- " + parseForTitle(bookReward)));
		if(rareReward != null)
			Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b- " + parseForTitle(rareReward)));
		

		
	}
	public static List<String> rd(List<String> list){
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(list);
		list.clear();
		list.addAll(hs);
		for(String s : list){
			s = capFirst(s);
		}
		return list;
	}
	public static String capFirst(String s){
		String firstLetter = s.substring(0,1);
		String rest = s.substring(1);
		s = firstLetter.toUpperCase() + rest.toLowerCase();
		return s;
	}
	public static ItemStack renameItem(ItemStack input, String newName, String[] Lore){
		
		ItemMeta meta = input.getItemMeta();
		List<String> tagList = new LinkedList<String>();
		if (newName != null) {
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',newName));
		}
		
		if(Lore != null){
			for (String line : Lore) {
				line = ChatColor.translateAlternateColorCodes('&', line);
				tagList.add(line);
			}
			meta.setLore(tagList);
		}
		
		input.setItemMeta(meta);
		return input;
		
		
	}
	

}
