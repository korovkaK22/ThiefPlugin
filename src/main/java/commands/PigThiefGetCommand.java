package commands;

import items.ThiefItems;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class PigThiefGetCommand implements TabExecutor {
    private final  String exception = "Correct usage: /thief <get> <item>";
    private final  List<String> atr1 = Arrays.asList("get");
    private final  List<String> atr2 = Arrays.asList("pigThief", "standLead");

    public PigThiefGetCommand() {
    }

    //Команда
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) { //Провірка на антиконсольку
            sender.sendMessage("Тільки гравці можуть писати цю команду");
            return true;
        }
        //Get
        if (args.length >= 1 && args[0].equalsIgnoreCase(atr1.get(0))) {
            return toGet(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
        }

        sender.sendMessage(ChatColor.RED + exception);
        return true;
    }

    //Заповнити таб
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return atr1;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase(atr1.get(0))) {
                return atr2;
            }
        }
        return null;
    }

//===========================================================================

    //обработка get
    private boolean toGet(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase(atr2.get(0))) {
                return toGetPigThief(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            }
            if (args[0].equalsIgnoreCase(atr2.get(1))) {
                return toGetBondage(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            }
        }

            sender.sendMessage(ChatColor.RED + exception);
            return true;
    }

        // Отримати ХрюшкоТибрялку
        private boolean toGetPigThief(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
        {
            Player player = (Player) sender;
            Inventory inventory = player.getInventory();
            ItemStack lead = ThiefItems.getPigLead();
            inventory.addItem(lead);

            return true;
        }

    // Отримати Bondage
    private boolean toGetBondage(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        Player player = (Player) sender;
        Inventory inventory = player.getInventory();
        ItemStack bondage = ThiefItems.getBondage();
        inventory.addItem(bondage);

        return true;
    }



}
