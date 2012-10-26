package to.joe.decapitation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class SetNameCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may use this command");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "I'm not sure what you're telling me to do");
            return false;
        }
        if (!args[0].matches("[A-Za-z0-9_]{2,16}")) {
            sender.sendMessage(ChatColor.RED + "That doesn't appear to be a valid username");
            return true;
        }
        Player player = (Player) sender;
        if (player.getItemInHand().getTypeId() == 98) { //XXX Set 397 here
            ItemStack i = player.getItemInHand();
            i.setData(new MaterialData(98, (byte) 3)); //XXX Set 397 here
            new Head((CraftItemStack) i).setName(args[0]);
        } else {
            sender.sendMessage(ChatColor.RED + "That's not a head");
        }
        return true;
    }

}
