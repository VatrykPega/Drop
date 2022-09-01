package pl.vatrykpega.drop.managers;

import pl.vatrykpega.drop.Main;

public class ConfigManager {
    private Main main;
    public ConfigManager(Main main) {
        this.main = main;
    }

    public int getInt(String str) {
        return main.getConfig().getInt(str);
    }
    public boolean getBoolean(String str) {
        return main.getConfig().getBoolean(str);
    }
}
