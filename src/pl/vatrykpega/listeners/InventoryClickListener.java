package pl.vatrykpega.listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.vatrykpega.Main;
import pl.vatrykpega.managers.InventoryManager;

public class InventoryClickListener implements Listener {
    static InventoryManager inventoryManager;

    public InventoryClickListener(InventoryManager im) {
        inventoryManager = im;
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null || !e.getClickedInventory().getName().equals(inventoryManager.invName)) {
            return;
        }
        e.setCancelled(true);

        if (!Main.playersWithDisabledDrops.containsKey(p.getUniqueId())) {
            Main.playersWithDisabledDrops.put(p.getUniqueId(), InventoryManager.listOfMaterials);
        }

        if (Main.dropMaterials.containsKey(e.getCurrentItem().getType())) {
            if (e.getCurrentItem().containsEnchantment(Enchantment.DURABILITY)) {
                (Main.playersWithDisabledDrops.get(p.getUniqueId())).add(e.getCurrentItem().getType());
            } else {
                (Main.playersWithDisabledDrops.get(p.getUniqueId())).remove(e.getCurrentItem().getType());
            }
        }

        if (e.getCurrentItem().equals(InventoryManager.cobblestone)) {
            if (!Main.disableCobbleDrop.contains(p.getUniqueId())) {
                Main.disableCobbleDrop.add(p.getUniqueId());
                p.sendMessage("§a§lWlaczono drop cobblestone'a!");
            } else {
                Main.disableCobbleDrop.remove(p.getUniqueId());
                p.sendMessage("§c§lWylaczono drop cobblestone'a!");
            }
        }

        if (e.getCurrentItem().equals(InventoryManager.hopper)) {
            if (!Main.dropToEq.contains(p.getUniqueId())) {
                Main.dropToEq.add(p.getUniqueId());
                p.sendMessage("§a§lOd teraz przedmioty same wpadaja do eq!");
            } else {
                Main.dropToEq.remove(p.getUniqueId());
                p.sendMessage("§c§lOd teraz przedmioty wypadaja na ziemie!");
            }
        }

        if (e.getCurrentItem().equals(InventoryManager.greenWool)) {
            (Main.playersWithDisabledDrops.get(p.getUniqueId())).clear();
            p.sendMessage("§a§lWlaczono wszystkie dropy!");
            Main.disableCobbleDrop.remove(p.getUniqueId());
        }

        if (e.getCurrentItem().equals(InventoryManager.redWool)) {
            for (Material material : Main.dropMaterials.keySet()) {
                if (!(Main.playersWithDisabledDrops.get(p.getUniqueId())).contains(material))
                    (Main.playersWithDisabledDrops.get(p.getUniqueId())).add(material);
            }
            p.sendMessage("§c§lWylaczono wszystkie dropy!");
            Main.disableCobbleDrop.add(p.getUniqueId());
        }
        inventoryManager.openInv(p);
    }
}