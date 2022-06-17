package pl.vatrykpega.listeners;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import pl.vatrykpega.Main;
import pl.vatrykpega.managers.ConfigManager;
import pl.vatrykpega.managers.FortuneManager;
import pl.vatrykpega.managers.Random;

public class BlockBreakListener implements Listener {
    private final ItemStack cobble = new ItemStack(Material.COBBLESTONE, 1);
    private final Material[] mat = new Material[]
            {
                    Material.IRON_ORE, Material.COAL_ORE, Material.EMERALD_ORE,
                    Material.DIAMOND_ORE, Material.LAPIS_ORE, Material.GOLD_ORE,
                    Material.REDSTONE_ORE
            };
    private final ArrayList<Material> ores = new ArrayList<>(Arrays.asList(this.mat));

    static ConfigManager configManager;
    public BlockBreakListener(ConfigManager cm) {

        configManager = cm;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        final boolean dropRud = configManager.getBoolean("Wypadanie Rud");
        final int exp = configManager.getInt("Exp");

        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.SURVIVAL && ores.contains(e.getBlock().getType())) {
            if (dropRud) {
                return;
            }

            e.getBlock().breakNaturally(null);
            if (Main.disableCobbleDrop.contains(p.getUniqueId())) {
                return;
            }

            if (Main.dropToEq.contains(p.getUniqueId()) && p.getInventory().firstEmpty() != -1) {
                p.getInventory().addItem(cobble);
            } else {
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), cobble);
            }
        }

        if (p.getGameMode() == GameMode.SURVIVAL && e.getBlock().getType() == Material.STONE) {
            ArrayList<Material> listOfMaterials = new ArrayList<>();
            if (!Main.playersWithDisabledDrops.containsKey(p.getUniqueId()))
                Main.playersWithDisabledDrops.put(p.getUniqueId(), listOfMaterials);
            p.giveExp(exp);
            double var = FortuneManager.checkFortune(p);

            if (Main.dropToEq.contains(p.getUniqueId()) && p.getInventory().firstEmpty() != -1) {
                e.getBlock().breakNaturally(null);
                if (!Main.disableCobbleDrop.contains(p.getUniqueId()))
                    p.getInventory().addItem(this.cobble);
                for (Material drop : Main.dropMaterials.keySet()) {
                    if (!(Main.playersWithDisabledDrops.get(p.getUniqueId())).contains(drop)) {
                        ItemStack is = new ItemStack(drop, 1);
                        double random = Random.getRandom();
                        if (random <= Main.dropMaterials.get(drop) * var)
                            p.getInventory().addItem(is);
                    }
                }
            } else {
                if (Main.disableCobbleDrop.contains(p.getUniqueId()))
                    e.getBlock().breakNaturally(null);
                for (Material drop : Main.dropMaterials.keySet()) {
                    if (!(Main.playersWithDisabledDrops.get(p.getUniqueId())).contains(drop)) {
                        ItemStack is = new ItemStack(drop, 1);
                        double random = Random.getRandom();
                        if (random <= Main.dropMaterials.get(drop) * var)
                            e.getBlock().getLocation().getWorld()
                                    .dropItemNaturally(e.getBlock().getLocation(), is);

                    }
                }
            }
        }
    }
}
