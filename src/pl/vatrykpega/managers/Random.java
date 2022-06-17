package pl.vatrykpega.managers;

public class Random {
    public static double getRandom() {
        double min = 1.0;
        double max = 100.0;
        return Math.random() * max - min + 1.0 + min;
    }
}
