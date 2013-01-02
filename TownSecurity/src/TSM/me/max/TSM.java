package TSM.me.max;

import java.io.File;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import TSM.me.max.Listeners.PlayerListener;

public class TSM extends JavaPlugin {
	public final Logger logger = Logger.getLogger("Minecraft");
    public static Permission permission = null;
    public static Chat chat = null;
	
    

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
		this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		File config = new File(this.getDataFolder(), "config.yml");
		if(!config.exists()) {
			this.saveDefaultConfig();
		}
    }
}
