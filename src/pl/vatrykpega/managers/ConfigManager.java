package pl.vatrykpega.managers;

import pl.vatrykpega.Main;

public class ConfigManager {
    static Main main;

    public ConfigManager(Main m) {
        main = m;
    }

    public int getInt(String str) {
        return main.getConfig().getInt(str);
    }

    public double getDouble(String str) {
        return main.getConfig().getDouble(str);
    }

    public String getString(String str) {
        return main.getConfig().getString(str);
    }

    public boolean getBoolean(String str) {
        return main.getConfig().getBoolean(str);
    }
}
