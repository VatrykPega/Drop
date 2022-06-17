package pl.vatrykpega.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.vatrykpega.Main;

public class InventoryManager {
    static ConfigManager configManager;

    public InventoryManager(ConfigManager cm){
        configManager = cm;
    }
    public final String invName = "§3§l» Drop";
    public final Inventory inv = Bukkit.createInventory(null, 27, invName);

    public static ArrayList<Material> listOfMaterials = new ArrayList<>();

    public static ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE,1, DyeColor.BLACK.getData());
    public static ItemStack cobblestone = new ItemStack(Material.COBBLESTONE, 1);
    public static ItemStack hopper = new ItemStack(Material.HOPPER, 1);
    public static ItemStack greenWool = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getData());
    public static ItemStack redWool = new ItemStack(Material.WOOL, 1, DyeColor.RED.getData());

    public void openInv(Player p) {

        if (!Main.playersWithDisabledDrops.containsKey(p.getUniqueId())) {
            Main.playersWithDisabledDrops.put(p.getUniqueId(), listOfMaterials);
        }

        inv.clear();

        // Dodawanie dropow

        for (Material material : Main.dropMaterials.keySet()) {
            ItemStack is = new ItemStack(material, 1);
            ItemMeta meta = is.getItemMeta();
            double chance = Main.dropMaterials.get(material);

            List<String> loreChance = new ArrayList<>();
            loreChance.add("");
            loreChance.add("§7Domyslna szansa na drop przedmiotu: " + chance + " %");
            loreChance.add("    §7Szansa na drop z kilofem:");
            loreChance.add("        §3Szczescie I: §7" + String.format("%.1f",
                    chance * 1.1 <= 100 ? chance * 1.1 : 100) + " %");
            loreChance.add("        §3Szczescie II: §7" + String.format("%.1f",
                    chance * 1.2 <= 100 ? chance * 1.2 : 100) + " %");
            loreChance.add("        §3Szczescie III: §7" + String.format("%.1f",
                    chance * 1.3 <= 100 ? chance * 1.3 : 100) + " %");
            loreChance.add("");
            loreChance.add("§aTwoja szansa na wydropienie przedmiotu: "
                    + String.format("%.1f", chance * FortuneManager.checkFortune(p)) + " %");
            loreChance.add("");
            List<String> lore = new ArrayList<>();

            if (!(Main.playersWithDisabledDrops.get(p.getUniqueId())).contains(material)) {
                meta.addEnchant(Enchantment.DURABILITY, 2115, true);
                lore.add("§aDrop przedmiotu: ✔");
                lore.addAll(loreChance);
                lore.add("§7» Kliknij lewym przyciskiem myszy, aby §cwylaczyc §7drop.");
            } else {
                lore.add("");
                lore.add("§cDrop przedmiotu: ✘");
                lore.addAll(loreChance);
                lore.add("§7» Kliknij lewym przyciskiem myszy, aby §awlaczyc §7drop.");
            }

            meta.setDisplayName("§3§l" + material);
            meta.setLore(lore);
            is.setItemMeta(meta);
            inv.setItem(inv.firstEmpty(), is);
        }

        // Sprawdzanie czy wszystkie przedmioty dodane do dropu zmieszcza sie w GUI

        if (inv.firstEmpty() > 23){
            Bukkit.getConsoleSender().sendMessage("\n\n" + "§4[DROP] ERROR! ZA DUZO PRZEDMIOTOW W CONFIGU!" + "\n");
            return;
        }

        // Wypelnienie pustych okienek w GUI

        ItemMeta paneMeta = pane.getItemMeta();
        paneMeta.setDisplayName("   ");
        pane.setItemMeta(paneMeta);
        while (inv.firstEmpty() < 23) {
            inv.setItem(inv.firstEmpty(), pane);
        }

        // Wypadanie cobbelstone'a

        ItemMeta cobblestoneMeta = cobblestone.getItemMeta();
        List<String> cobbleLore = new ArrayList<>();
        cobbleLore.add("");
        if (!Main.disableCobbleDrop.contains(p.getUniqueId())) {
            cobblestoneMeta.addEnchant(Enchantment.DURABILITY, 2115, true);
            cobblestoneMeta.setDisplayName("§a§lDROP COBBELSTONE: ✔");
            cobbleLore.add("§7» Kliknij lewym przyciskiem myszy, aby §cwylaczyc §7wypadanie cobblestone'a.");
        } else {
            cobblestoneMeta.removeEnchant(Enchantment.DURABILITY);
            cobblestoneMeta.setDisplayName("§c§lDROP COBBELSTONE: ✘");
            cobbleLore.add("");
            cobbleLore.add("§7» Kliknij lewym przyciskiem myszy, aby §awlaczyc §7wypadanie cobblestone'a.");
        }
        cobblestoneMeta.setLore(cobbleLore);
        cobblestone.setItemMeta(cobblestoneMeta);
        inv.setItem(23, cobblestone);

        // Wypadanie przedmiotow do eq

        ItemMeta hopperMeta = hopper.getItemMeta();
        List<String> hopperLore = new ArrayList<>();
        hopperLore.add("");
        if (!Main.dropToEq.contains(p.getUniqueId())) {
            hopperMeta.removeEnchant(Enchantment.DURABILITY);
            hopperMeta.setDisplayName("§c§l» Wpadanie przedmiotów do ekwipunku: ✘");
            hopperLore.add("");
            hopperLore.add("§7» Kliknij lewym przyciskiem myszy, aby §awlaczyc §7wpadanie przedmiotow do eq.");
        } else {
            hopperMeta.addEnchant(Enchantment.DURABILITY, 2115, true);
            hopperMeta.setDisplayName("§a§l» Wpadanie przedmiotów do ekwipunku: ✔");
            hopperLore.add("§7» Kliknij lewym przyciskiem myszy, aby §cwylaczyc §7wpadanie przedmiotow do eq.");
        }
        hopperMeta.setLore(hopperLore);
        hopper.setItemMeta(hopperMeta);
        inv.setItem(24, hopper);

        // Wlaczanie wszystkich dropow

        ItemMeta gWoolMeta = greenWool.getItemMeta();
        gWoolMeta.setDisplayName("§a§l» Wlacz wszystkie dropy");
        List<String> gWoolLore = new ArrayList<>();
        if ((Main.playersWithDisabledDrops.get(p.getUniqueId())).isEmpty() &&
                !Main.disableCobbleDrop.contains(p.getUniqueId())) {
            gWoolMeta.addEnchant(Enchantment.DURABILITY, 2115, true);
        } else {
            gWoolMeta.removeEnchant(Enchantment.DURABILITY);
            gWoolLore.add("");
        }
        gWoolLore.add("");
        gWoolLore.add("§7» Kliknij lewym przyciskiem myszy, aby §awlaczyc §7wszystkie dropy.");
        gWoolMeta.setLore(gWoolLore);
        greenWool.setItemMeta(gWoolMeta);
        inv.setItem(25, greenWool);

        // Wylaczanie wszystkich dropow

        ItemMeta rWoolMeta = redWool.getItemMeta();
        rWoolMeta.setDisplayName("§c§l» Wylacz wszystkie dropy");
        List<String> rWoolLore = new ArrayList<>();
        if (Main.disableCobbleDrop.contains(p.getUniqueId())
                && new HashSet<>(Main.playersWithDisabledDrops.get(p.getUniqueId())).containsAll(Main.dropMaterials.keySet())) {
            rWoolMeta.addEnchant(Enchantment.DURABILITY, 2115, true);
        } else {
            rWoolMeta.removeEnchant(Enchantment.DURABILITY);
            rWoolLore.add("");
        }
        rWoolLore.add("");
        rWoolLore.add("§7» Kliknij lewym przyciskiem myszy, aby §cwylaczyc §7wszystkie dropy.");
        rWoolMeta.setLore(rWoolLore);
        redWool.setItemMeta(rWoolMeta);
        inv.setItem(26, redWool);


        p.openInventory(inv);
    }
}
