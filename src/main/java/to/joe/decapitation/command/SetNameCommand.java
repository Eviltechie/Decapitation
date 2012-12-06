package to.joe.decapitation.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_4_5.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import to.joe.decapitation.Decapitation;
import to.joe.decapitation.Head;

public class SetNameCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may use this command");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "/setname [username]");
            return true;
        }
        if (!args[0].matches("[A-Za-z0-9_]{2,16}")) {
            sender.sendMessage(ChatColor.RED + "That doesn't appear to be a valid username");
            return true;
        }
        Player player = (Player) sender;
        if (player.getItemInHand().getTypeId() == Decapitation.HEAD) {
            player.setItemInHand(new ItemStack(Decapitation.HEAD, player.getItemInHand().getAmount(), (short) 0, (byte) 3));
            player.updateInventory();
            new Head((CraftItemStack) player.getItemInHand()).setName(args[0]);
            sender.sendMessage(ChatColor.GREEN + "Set name " + args[0]);
        } else {
            sender.sendMessage(ChatColor.RED + "That's not a head");
        }
        return true;
    }

}
