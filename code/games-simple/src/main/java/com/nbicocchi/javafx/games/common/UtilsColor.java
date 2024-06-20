package com.nbicocchi.javafx.games.common;

import javafx.scene.paint.Color;

public class UtilsColor {
    /**
     * @param min Minimum value of allowed range (lower values are set to min)
     * @param max Maximum value of allowed range (higher values are set to max)
     * @param minhue Color hue associated with min
     * @param maxhue Color hue associated with max
     * @param value Value to be mapped to a color
     *
     * @return Color Return a color representing the position of value within [min, max] range
     */
    public static Color getColorScale(double min, double max, double minhue, double maxhue, double value) {
        value = Math.min(Math.max(value, min), max);
        double hue = minhue + (maxhue - minhue) * (value - min) / (max - min);
        return Color.hsb(hue, 1.0, 1.0);
    }
}
