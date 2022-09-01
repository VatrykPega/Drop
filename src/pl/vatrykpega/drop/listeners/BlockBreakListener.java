package pl.vatrykpega.drop.listeners;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import pl.vatrykpega.drop.Main;
import pl.vatrykpega.drop.managers.ConfigManager;
import pl.vatrykpega.drop.managers.FortuneManager;
import pl.vatrykpega.drop.managers.Random;

public class BlockBreakListener extends ConfigManager implements Listener {
    private Main main;
    public BlockBreakListener(Main main) {
        super(main);
        this.main = main;
    }

    private final ItemStack cobble = new ItemStack(Material.COBBLESTONE, 1);
    private final Material[] mat = new Material[]
            {
                    Material.IRON_ORE, Material.COAL_ORE, Material.EMERALD_ORE,
                    Material.DIAMOND_ORE, Material.LAPIS_ORE, Material.GOLD_ORE,
                    Material.REDSTONE_ORE
            };
    private final ArrayList<Material> ores = new ArrayList<>(Arrays.asList(mat));

    private final boolean dropRud = getBoolean("Wypadanie Rud");
    private final int exp = getInt("Exp");

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.SURVIVAL && ores.contains(e.getBlock().getType())) {
            if (dropRud) {
                return;
            }

            e.getBlock().breakNaturally(null);
            if (main.disableCobble.contains(p.getUniqueId())) {
                return;
            }

            if (main.dropToEq.contains(p.getUniqueId()) && p.getInventory().firstEmpty() != -1) {
                p.getInventory().addItem(cobble);
            } else {
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), cobble);
            }
        }

        if (p.getGameMode() == GameMode.SURVIVAL && e.getBlock().getType() == Material.STONE) {
            ArrayList<Material> listOfMaterials = new ArrayList<>();
            if (!main.playersWithDisabledDrops.containsKey(p.getUniqueId()))
                main.playersWithDisabledDrops.put(p.getUniqueId(), listOfMaterials);
            if (!main.disableEXP.contains(p.getUniqueId()))
                p.giveExp(exp);
            double var = FortuneManager.checkFortune(p);

            if (main.dropToEq.contains(p.getUniqueId()) && p.getInventory().firstEmpty() != -1) {
                e.getBlock().breakNaturally(null);
                if (!main.disableCobble.contains(p.getUniqueId()))
                    p.getInventory().addItem(this.cobble);
                for (Material drop : main.dropMaterials.keySet()) {
                    if (!(main.playersWithDisabledDrops.get(p.getUniqueId())).contains(drop)) {
                        ItemStack is = new ItemStack(drop, 1);
                        double random = Random.getRandom();
                        if (random <= main.dropMaterials.get(drop) * var)
                            p.getInventory().addItem(is);
                    }
                }
            } else {
                if (main.disableCobble.contains(p.getUniqueId()))
                    e.getBlock().breakNaturally(null);
                for (Material drop : main.dropMaterials.keySet()) {
                    if (!(main.playersWithDisabledDrops.get(p.getUniqueId())).contains(drop)) {
                        ItemStack is = new ItemStack(drop, 1);
                        double random = Random.getRandom();
                        if (random <= main.dropMaterials.get(drop) * var)
                            e.getBlock().getLocation().getWorld()
                                    .dropItemNaturally(e.getBlock().getLocation(), is);

                    }
                }
            }
        }
    }
}
