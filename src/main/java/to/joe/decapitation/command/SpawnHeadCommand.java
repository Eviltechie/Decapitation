package to.joe.decapitation.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

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
        ItemStack i = new ItemStack(Material.SKULL_ITEM, numHeads, (short) 3);
        SkullMeta meta = (SkullMeta) i.getItemMeta();
        meta.setOwner(args[0]);
        i.setItemMeta(meta);
        ((Player) sender).getInventory().addItem(i);
        return true;
    }

}
