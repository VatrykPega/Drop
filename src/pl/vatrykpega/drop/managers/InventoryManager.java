package pl.vatrykpega.drop.managers;

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
import pl.vatrykpega.drop.Main;

public class InventoryManager extends ConfigManager {
    private Main main;
    public InventoryManager(Main main) {
        super(main);
        this.main = main;
    }

    public ArrayList<Material> listOfMaterials = new ArrayList<>();

    public final String invName = "§3§l» Drop";
    public final Inventory inv = Bukkit.createInventory(null, 27, invName);

    public final ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE,1, DyeColor.BLACK.getData());

    public final ItemStack expBottle = new ItemStack(Material.EXP_BOTTLE, 1);
    public final ItemStack cobblestone = new ItemStack(Material.COBBLESTONE, 1);
    public final ItemStack hopper = new ItemStack(Material.HOPPER, 1);
    public final ItemStack greenWool = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getData());
    public final ItemStack redWool = new ItemStack(Material.WOOL, 1, DyeColor.RED.getData());

    public void openInv(Player p) {
        inv.clear();

        if (!main.playersWithDisabledDrops.containsKey(p.getUniqueId())) {
            main.playersWithDisabledDrops.put(p.getUniqueId(), listOfMaterials);
        }

        // Sprawdzanie czy wszystkie przedmioty dodane do dropu zmieszcza sie w GUI
        if (inv.firstEmpty() > 22){
            Bukkit.getConsoleSender().sendMessage("\n\n" + "§4[DROP] ERROR! ZA DUZO PRZEDMIOTOW W CONFIGU!" + "\n");
            return;
        }

        // Dodawanie dropow

        for (Material material : main.dropMaterials.keySet()) {
            ItemStack is = new ItemStack(material, 1);
            ItemMeta meta = is.getItemMeta();
            double chance = main.dropMaterials.get(material);

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

            if (!(main.playersWithDisabledDrops.get(p.getUniqueId())).contains(material)) {
                meta.addEnchant(Enchantment.DURABILITY, 2115, true);
                lore.add("§aDrop przedmiotu: ✔");
                lore.addAll(loreChance);
                lore.add("§7» Kliknij lewym przyciskiem myszy, aby §cwylaczyc §7drop.");
            } else {
                meta.removeEnchant(Enchantment.DURABILITY);
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

        // Wypelnienie pustych okienek w GUI

        ItemMeta paneMeta = pane.getItemMeta();
        paneMeta.setDisplayName("   ");
        pane.setItemMeta(paneMeta);
        while (inv.firstEmpty() < 22) {
            inv.setItem(inv.firstEmpty(), pane);
        }

        // Wypadanie cobbelstone'a

        ItemMeta cobblestoneMeta = cobblestone.getItemMeta();
        List<String> cobbleLore = new ArrayList<>();
        cobbleLore.add("");
        if (!this.main.disableCobble.contains(p.getUniqueId())) {
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
        inv.setItem(22, cobblestone);

        // Wlaczanie/wylaczanie wypadania expa

        ItemMeta expBottleMeta = expBottle.getItemMeta();
        List<String> expBottleLore = new ArrayList<>();
        expBottleLore.add("");
        if (!main.disableEXP.contains(p.getUniqueId())) {
            expBottleMeta.addEnchant(Enchantment.DURABILITY, 2115, true);
            expBottleMeta.setDisplayName("§a§lWYPADANIE XP: ✔");
            expBottleLore.add("§7» Kliknij lewym przyciskiem myszy, aby §cwylaczyc §7wypadanie expa.");
        } else {
            expBottleMeta.removeEnchant(Enchantment.DURABILITY);
            expBottleMeta.setDisplayName("§c§lWYPADANIE XP: ✘");
            expBottleLore.add("");
            expBottleLore.add("§7» Kliknij lewym przyciskiem myszy, aby §awlaczyc §7wypadanie expa.");
        }
        expBottleMeta.setLore(expBottleLore);
        expBottle.setItemMeta(expBottleMeta);
        inv.setItem(23, expBottle);

        // Wypadanie przedmiotow do eq

        ItemMeta hopperMeta = hopper.getItemMeta();
        List<String> hopperLore = new ArrayList<>();
        hopperLore.add("");
        if (!main.dropToEq.contains(p.getUniqueId())) {
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
        if ((main.playersWithDisabledDrops.get(p.getUniqueId())).isEmpty() &&
                !main.disableCobble.contains(p.getUniqueId())) {
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
        if (main.disableCobble.contains(p.getUniqueId())
                && new HashSet<>(main.playersWithDisabledDrops.get(p.getUniqueId())).containsAll(main.dropMaterials.keySet())) {
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

