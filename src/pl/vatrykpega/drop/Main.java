package pl.vatrykpega.drop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import pl.vatrykpega.drop.commands.Drop;
import pl.vatrykpega.drop.listeners.BlockBreakListener;
import pl.vatrykpega.drop.listeners.InventoryClickListener;

public class Main extends JavaPlugin {
    public Set<UUID> dropToEq = new HashSet<>();
    public Set<UUID> disableEXP = new HashSet<>();
    public Set<UUID> disableCobble = new HashSet<>();
    public HashMap<Material, Double> dropMaterials = new HashMap<>();
    public HashMap<UUID, List<Material>> playersWithDisabledDrops = new HashMap<>();

    public void onEnable() {
        registerConfig();
        registerCommands();
        registerEvents();
    }

    public void onDisable() {}

    public void registerCommands() {
        getCommand("drop").setExecutor(new Drop(this));
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(this),this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(this),this);
    }

    public void registerConfig() {
        getConfig().addDefault("Wypadanie Rud", Boolean.FALSE);
        getConfig().addDefault("Exp", 5);
        getConfig().addDefault("Dropy.OBSIDIAN", 15.0);
        getConfig().addDefault("Dropy.DIAMOND", 5.0);
        getConfig().addDefault("Dropy.GOLD_INGOT", 7.0);
        getConfig().addDefault("Dropy.IRON_INGOT", 11.0);
        getConfig().options().copyDefaults(true);
        for (String key : getConfig().getConfigurationSection("Dropy").getKeys(false)) {
            double chance = (Double) getConfig().get("Dropy." + key);
            if (chance > 100) {
                chance = 100;
            }
            dropMaterials.put(Material.valueOf(key), chance);
        }
        saveConfig();
    }
}