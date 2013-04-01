package to.joe.decapitation.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import to.joe.decapitation.Decapitation;
import to.joe.decapitation.exception.BadPlayerMatchException;

public class SpawnHeadCommand implements CommandExecutor {

    Decapitation plugin;

    public SpawnHeadCommand(Decapitation plugin) {
        this.plugin = plugin;
    }

    private Player getPlayer(String target, CommandSender searcher) throws BadPlayerMatchException {

        final List<Player> players = new ArrayList<Player>();

        for (final Player player : plugin.getServer().getOnlinePlayers()) {
            if (searcher instanceof Player && !((Player) searcher).canSee(player)) {
                continue;
            }
            if (player.getName().equalsIgnoreCase(target)) {
                return player;
            }
            if (player.getName().toLowerCase().contains(target.toLowerCase())) {
                players.add(player);
            }
        }
        if (players.size() > 1) {
            StringBuilder sb = new StringBuilder();
            for (Player player : players) {
                sb.append(player.getName());
                sb.append(", ");
            }
            sb.setLength(sb.length() - 2);
            throw new BadPlayerMatchException("Matches too many players (" + sb.toString() + ")");
        }
        if (players.size() == 0) {
            throw new BadPlayerMatchException("No players matched");
        }
        return players.get(0);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int quantity = 1;
        Player target;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/spawnhead [username] <quantity> <player>");
            return true;
        }

        if (!args[0].matches("[A-Za-z0-9_]{2,16}")) {
            sender.sendMessage(ChatColor.RED + "That doesn't appear to be a valid username");
            return true;
        }

        if (args.length > 1) {
            try {
                quantity = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "That's not a number");
                return true;
            }
            if (quantity < 1) {
                quantity = 1;
            } else if (quantity > 64) {
                quantity = 64;
            }
        }

        if (args.length > 2) {
            try {
                target = getPlayer(args[2], sender);
            } catch (BadPlayerMatchException e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
                return true;
            }
        } else {
            if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                sender.sendMessage(ChatColor.RED + "Specify a player to give the weapon to");
                return true;
            }
        }

        ItemStack i = new ItemStack(Material.SKULL_ITEM, quantity, (short) 3);
        SkullMeta meta = (SkullMeta) i.getItemMeta();
        meta.setOwner(args[0]);
        i.setItemMeta(meta);
        target.getInventory().addItem(i);
        return true;
    }

}
