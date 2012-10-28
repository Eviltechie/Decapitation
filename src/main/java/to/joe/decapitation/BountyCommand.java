package to.joe.decapitation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BountyCommand implements CommandExecutor {
    
    Decapitation plugin;
    
    public BountyCommand(Decapitation decapitation) {
        plugin = decapitation;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { //TODO Perms, checking return statements
        /*
         * bounty search [username]
         * bounty list <page>
         * bounty place [username] [price]
         * bounty claim
         * bounty remove [username]
         */
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Players only");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/bounty search [username] - search for a bounty on a player");
            sender.sendMessage(ChatColor.RED + "/bounty list <page> - list current bounties");
            sender.sendMessage(ChatColor.RED + "/bounty place [username] [price] - place a bounty on a player");
            sender.sendMessage(ChatColor.RED + "/bounty claim - claim the bounty of the head you are holding");
            sender.sendMessage(ChatColor.RED + "/bounty remove [username] - remove the bounty of a player");
            sender.sendMessage(ChatColor.RED + "/bounty redeem - claim any heads that are owed to you");
            sender.sendMessage(ChatColor.RED + "Current tax rate is " + plugin.tax + "%");
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                try {
                    PreparedStatement ps1 = plugin.sql.getFreshPreparedStatementColdFromTheRefrigerator("SELECT * FROM bounties WHERE hunter IS NULL ORDER BY bounties.reward DESC LIMIT 0,9");
                    ResultSet rs1 = ps1.executeQuery();
                    PreparedStatement ps2 = plugin.sql.getFreshPreparedStatementColdFromTheRefrigerator("SELECT count(*) FROM bounties WHERE hunter IS NULL");
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs1.next()) {
                        sender.sendMessage(ChatColor.GREEN + "=========" + ChatColor.GOLD + "Bounties [Page 1 of " + (rs2.getInt(1)+ 8) / 9 + "]" + ChatColor.GREEN + "=========");
                        do {
                            sender.sendMessage(ChatColor.GOLD + "" + rs1.getInt("reward") + " - " + rs1.getString("hunted"));
                        } while (rs1.next());
                    } else {
                        sender.sendMessage(ChatColor.RED + "There are no bounties");
                    }
                } catch (SQLException e) {
                    plugin.getLogger().log(Level.SEVERE, "Error getting list of bounties", e);
                    sender.sendMessage(ChatColor.RED + "Something went wrong :(");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("claim")) {
                //TODO Claim a bounty
                if (p.getItemInHand().getTypeId() != Decapitation.HEAD) {
                    sender.sendMessage(ChatColor.RED + "That's not a head");
                    return true;
                }
                Head h = new Head((CraftItemStack) p.getItemInHand());
                if (!h.isNamed()) {
                    sender.sendMessage(ChatColor.RED + "That head is not named");
                    return true;
                }
                try {
                    String hunted = h.getName();
                    PreparedStatement ps1 = plugin.sql.getFreshPreparedStatementColdFromTheRefrigerator("SELECT * FROM bounties WHERE hunted LIKE ? AND hunter IS NULL ORDER BY bounties.reward DESC LIMIT 1");
                    ps1.setString(1, hunted);
                    ResultSet rs1 = ps1.executeQuery();
                    if (rs1.next()) {
                        PreparedStatement ps2 = plugin.sql.getFreshPreparedStatementColdFromTheRefrigerator("UPDATE bounties SET hunter = ?, turnedin = NOW() LIMIT 1");
                        ps2.setString(1, p.getName());
                        ps2.execute();
                        Decapitation.economy.depositPlayer(p.getName(), rs1.getDouble("reward"));
                        p.setItemInHand(new ItemStack(0));
                        Player i = plugin.getServer().getPlayer(rs1.getString("issuer"));
                        if (i != null) {
                            CraftItemStack c = new CraftItemStack(Decapitation.HEAD, 1, (short) 0, (byte) 3);
                            new Head(c).setName(hunted);
                            ((Player) sender).getInventory().addItem(c);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "There does not appear to be a bounty on that head");
                        return true;
                    }
                } catch (SQLException e) {
                    plugin.getLogger().log(Level.SEVERE, "Error claiming bounty", e);
                    sender.sendMessage(ChatColor.RED + "Something went wrong :(");
                    return true;
                }
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("search")) {
                //TODO Search for bounties
            }
            if (args[0].equalsIgnoreCase("list")) {
                //TODO List bounties w/ page
            }
            if (args[0].equalsIgnoreCase("remove")) {
                //TODO Remove bounty
            }
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("place")) {
            //TODO Place bounty
        }
        return true;
    }

}
