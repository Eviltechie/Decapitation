package to.joe.decapitation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;


import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;
import to.joe.decapitation.command.BountyCommand;
import to.joe.decapitation.command.ClearNameCommand;
import to.joe.decapitation.command.SetNameCommand;
import to.joe.decapitation.command.SpawnHeadCommand;
import to.joe.decapitation.datastorage.DataStorageException;
import to.joe.decapitation.datastorage.DataStorageInterface;
import to.joe.decapitation.datastorage.MySQLDataStorageImplementation;
import to.joe.decapitation.datastorage.YamlDataStorageImplementation;

public class Decapitation extends JavaPlugin implements Listener {

    double allDeaths;
    double killedByPlayer;
    public boolean bounties = false;
    private boolean huntedDropOnly;
    private boolean placeInKillerInv;
    public boolean canClaimOwn;
    private double tax;
    public double minimumBounty;
    private DataStorageInterface dsi;
    private String color;

    public static Economy economy = null;

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    public DataStorageInterface getDsi() {
        return dsi;
    }

    public double getTax() {
        return tax;
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        allDeaths = getConfig().getDouble("dropSkulls.allDeaths", 0);
        killedByPlayer = getConfig().getDouble("dropSkulls.killedByPlayer", 1);
        placeInKillerInv = getConfig().getBoolean("dropSkulls.placeInKillerInv", false);
        tax = getConfig().getDouble("bounty.tax", 0.05D);
        minimumBounty = getConfig().getDouble("bounty.minimum", 10);
        huntedDropOnly = getConfig().getBoolean("bounty.huntedDropOnly", false);
        canClaimOwn = getConfig().getBoolean("bounty.canClaimOwn", true);
        color = getConfig().getString("tagapi.wanted-color");

        getServer().getPluginManager().registerEvents(this, this);

        getCommand("setname").setExecutor(new SetNameCommand());
        getCommand("clearname").setExecutor(new ClearNameCommand());
        getCommand("spawnhead").setExecutor(new SpawnHeadCommand(this));
        getCommand("bounty").setExecutor(new BountyCommand(this));


        if (getConfig().getBoolean("bounty.enabled")) {
            bounties = setupEconomy();
            if (bounties)
                getLogger().info("Econ detected");
            else
                getLogger().info("Econ not detected");
        }
        if (bounties) {
            if (getConfig().getString("datastorage").equalsIgnoreCase("mysql")) {
                try {
                    dsi = new MySQLDataStorageImplementation(this, getConfig().getString("database.url"), getConfig().getString("database.username"), getConfig().getString("database.password"));
                } catch (SQLException e) {
                    getLogger().log(Level.SEVERE, "Error connecting to mysql database", e);
                    bounties = false;
                }
            } else if (getConfig().getString("datastorage").equalsIgnoreCase("yaml")) {
                try {
                    dsi = new YamlDataStorageImplementation(this);
                } catch (IOException e) {
                    getLogger().log(Level.SEVERE, "Error setting up yaml storage", e);
                    bounties = false;
                }
            }
        }
        if (bounties)
            getLogger().info("Bounties enabled");
        else
            getLogger().info("Bounties not enabled");
    }
    @EventHandler
    public void onNameTagChange(AsyncPlayerReceiveNameTagEvent event){
        Player p = event.getNamedPlayer();
        ChatColor c = color.charAt(0) == '&' ? ChatColor.getByChar(color.substring(0,1)) : ChatColor.valueOf(color);
        if(c == null){
            getLogger().log(Level.SEVERE, "Error parsing color to plugin. Check your config");
            return;
        }
        try {
            Bounty b = getDsi().getBounty(p.getName());
            if(p.hasPermission("decapitation.wanted-color") && b != null && c != null){
                event.setTag(c+p.getName());
            }
        } catch (DataStorageException e) {
            getLogger().log(Level.SEVERE, "Error getting if player has bounty", e);
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        Player k = p.getKiller();
        try {
            if (bounties && huntedDropOnly && dsi.getBounty(p.getName()) == null) {
                return;
            }
        } catch (DataStorageException e) {
            getLogger().log(Level.SEVERE, "Error getting if player has bounty", e);
        }
        if (p.hasPermission("decapitation.dropheads") && (allDeaths > Math.random() || ((killedByPlayer > Math.random()) && k != null)) && (k == null || (k != null && k.hasPermission("decapitation.collectheads")))) {
            ItemStack i = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) i.getItemMeta();
            meta.setOwner(event.getEntity().getName());
            i.setItemMeta(meta);
            if (placeInKillerInv && k != null) {
                if (!k.getInventory().addItem(i).isEmpty()) {
                    k.getWorld().dropItem(k.getLocation(), i);
                }
            } else {
                event.getDrops().add(i);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (bounties) {
            int unclaimedHeads = 0;
            try {
                unclaimedHeads = dsi.getNumUnclaimedHeads(event.getPlayer().getName());
            } catch (DataStorageException e) {
                getLogger().log(Level.SEVERE, "Error getting number of unclaimed heads", e);
            }
            if (unclaimedHeads > 0) {
                if (unclaimedHeads == 1)
                    event.getPlayer().sendMessage(ChatColor.GOLD + "You have " + unclaimedHeads + " unclaimed head.");
                else
                    event.getPlayer().sendMessage(ChatColor.GOLD + "You have " + unclaimedHeads + " unclaimed heads.");
                event.getPlayer().sendMessage(ChatColor.GOLD + "Type /bounty redeem to receive them");
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().hasPermission("decapitation.info") && event.getClickedBlock().getType() == Material.SKULL) {
            Skull s = (Skull) event.getClickedBlock().getState();
            if (s.hasOwner()) {
                event.getPlayer().sendMessage(ChatColor.GREEN + "The head of " + s.getOwner());
            } else {
                event.getPlayer().sendMessage(ChatColor.GREEN + "That head has no name attached");
            }
        }
    }
}