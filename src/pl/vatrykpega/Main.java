package pl.vatrykpega;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import pl.vatrykpega.commands.Drop;
import pl.vatrykpega.listeners.BlockBreakListener;
import pl.vatrykpega.listeners.InventoryClickListener;
import pl.vatrykpega.managers.ConfigManager;
import pl.vatrykpega.managers.InventoryManager;

public class Main extends JavaPlugin {
    public static Main main;

    public static Set<UUID> dropToEq = new HashSet<>();

    public static Set<UUID> disableCobbleDrop = new HashSet<>();

    public static HashMap<Material, Double> dropMaterials = new HashMap<>();

    public static HashMap<UUID, List<Material>> playersWithDisabledDrops = new HashMap<>();

    public void onEnable() {
        main = this;
        registerConfig();
        registerEvents();
        registerCommands();
    }

    public void onDisable() {}

    public void registerCommands() {
        getCommand("drop").setExecutor(new Drop(new InventoryManager(new ConfigManager(this))));
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(new ConfigManager(this)), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(new InventoryManager(new ConfigManager(this))), this);
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