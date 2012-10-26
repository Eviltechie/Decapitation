package to.joe.decapitation;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Decapitation extends JavaPlugin implements Listener {
    
    boolean allDeaths;
    boolean killedByPlayer;
    
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        allDeaths = getConfig().getBoolean("dropSkulls.allDeaths");
        killedByPlayer = getConfig().getBoolean("dropSkulls.killedByPlayer");
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("setname").setExecutor(new SetNameCommand());
    }
    
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (allDeaths || (killedByPlayer && event.getEntity().getKiller() != null)) {
            CraftItemStack c = new CraftItemStack(98, 1, (short) 0, (byte) 3); //XXX Set 397 here
            new Head(c).setName(event.getEntity().getName());
            event.getDrops().add(c);
        }
    }
	
}