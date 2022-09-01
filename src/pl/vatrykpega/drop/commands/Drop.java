package pl.vatrykpega.drop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.vatrykpega.drop.Main;
import pl.vatrykpega.drop.managers.InventoryManager;

public class Drop extends InventoryManager implements CommandExecutor {
    public Drop(Main main){
        super(main);
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cNie mozesz wywolac tej komendy z consoli!");
            return false;
        }
        Player p = (Player)sender;
        openInv(p);
        return false;
    }
}
