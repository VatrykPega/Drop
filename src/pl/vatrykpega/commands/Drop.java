package pl.vatrykpega.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.vatrykpega.managers.InventoryManager;

public class Drop implements CommandExecutor {
    InventoryManager inventoryManager;
    public Drop(InventoryManager im){
        inventoryManager = im;
    }
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cNie mozesz wywolac tej komendy z consoli!");
            return false;
        }
        Player p = (Player)sender;
        inventoryManager.openInv(p);
        return false;
    }
}
