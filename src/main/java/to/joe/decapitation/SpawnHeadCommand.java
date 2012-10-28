package to.joe.decapitation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public class SpawnHeadCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may use this command");
            return true;
        }
        if (args.length < 1 || args.length > 2) {
            sender.sendMessage(ChatColor.RED + "/spawnhead [username] <quantity>");
            return true;
        }
        if (!args[0].matches("[A-Za-z0-9_]{2,16}")) {
            sender.sendMessage(ChatColor.RED + "That doesn't appear to be a valid username");
            return true;
        }
        int numHeads = 1;
        if (args.length == 2) {
            try {
                numHeads = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "That's not a number");
                return true;
            }
        }
        CraftItemStack c = new CraftItemStack(Decapitation.HEAD, numHeads, (short) 0, (byte) 3);
        new Head(c).setName(args[0]);
        ((Player) sender).getInventory().addItem(c);
        return true;
    }

}
