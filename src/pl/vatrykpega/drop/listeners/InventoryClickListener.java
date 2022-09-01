package pl.vatrykpega.drop.listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.vatrykpega.drop.Main;
import pl.vatrykpega.drop.managers.InventoryManager;


public class InventoryClickListener extends InventoryManager implements Listener {
    private Main main;
    public InventoryClickListener(Main main) {
        super(main);
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null || !e.getClickedInventory().getName().equals(invName)) {
            return;
        }
        e.setCancelled(true);

        if (!main.playersWithDisabledDrops.containsKey(p.getUniqueId())) {
            main.playersWithDisabledDrops.put(p.getUniqueId(), listOfMaterials);
        }

        if (main.dropMaterials.containsKey(e.getCurrentItem().getType())) {
            if (e.getCurrentItem().containsEnchantment(Enchantment.DURABILITY)) {
                (main.playersWithDisabledDrops.get(p.getUniqueId())).add(e.getCurrentItem().getType());
            } else {
                (main.playersWithDisabledDrops.get(p.getUniqueId())).remove(e.getCurrentItem().getType());
            }
        }

        if (e.getCurrentItem().getType().equals(expBottle.getType())) {
            if (!main.disableEXP.contains(p.getUniqueId())) {
                main.disableEXP.add(p.getUniqueId());
                p.sendMessage("§c§lWylaczono wypadanie expa!");
            } else {
                main.disableEXP.remove(p.getUniqueId());
                p.sendMessage("§a§lWlaczono wypadanie expa!");
            }
        }

        if (e.getCurrentItem().getType().equals(cobblestone.getType())) {
            if (!main.disableCobble.contains(p.getUniqueId())) {
                main.disableCobble.add(p.getUniqueId());
                p.sendMessage("§c§lWylaczono drop cobblestone'a!");
            } else {
                main.disableCobble.remove(p.getUniqueId());
                p.sendMessage("§a§lWlaczono drop cobblestone'a!");
            }
        }

        if (e.getCurrentItem().getType().equals(hopper.getType())) {
            if (!main.dropToEq.contains(p.getUniqueId())) {
                main.dropToEq.add(p.getUniqueId());
                p.sendMessage("§a§lOd teraz przedmioty same wpadaja do eq!");
            } else {
                main.dropToEq.remove(p.getUniqueId());
                p.sendMessage("§c§lOd teraz przedmioty wypadaja na ziemie!");
            }
        }

        if (e.getCurrentItem().getData().equals(greenWool.getData())) {
            for (Material material : main.dropMaterials.keySet()) {
                if (main.playersWithDisabledDrops.get(p.getUniqueId()).contains(material))
                    main.playersWithDisabledDrops.get(p.getUniqueId()).remove(material);
            }
            main.disableCobble.remove(p.getUniqueId());
            p.sendMessage("§a§lWlaczono wszystkie dropy!");
        }

        if (e.getCurrentItem().getData().equals(redWool.getData())) {
            for (Material material : main.dropMaterials.keySet()) {
                if (!(main.playersWithDisabledDrops.get(p.getUniqueId()).contains(material)))
                    main.playersWithDisabledDrops.get(p.getUniqueId()).add(material);
            }
            main.disableCobble.add(p.getUniqueId());
            p.sendMessage("§c§lWylaczono wszystkie dropy!");
        }
        openInv(p);
    }
}