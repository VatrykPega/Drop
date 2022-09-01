package pl.vatrykpega.drop.managers;

import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FortuneManager {
    public static double checkFortune(Player p) {
        if (p.getEquipment().getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
            ItemStack pickaxe = new ItemStack(p.getItemInHand());
            Map<Enchantment, Integer> enchantMap = pickaxe.getEnchantments();
            if (enchantMap.containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
                int fortuneLvl = enchantMap.get(Enchantment.LOOT_BONUS_BLOCKS);
                if (fortuneLvl == 1)
                    return 1.1;
                if (fortuneLvl == 2)
                    return 1.2;
                if (fortuneLvl >= 3)
                    return 1.3;
            }
        }
        return 1.0;
    }
}
