package com.smaugslair.thitracker.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ColorCollection {

    private static final Logger log = LoggerFactory.getLogger(ColorCollection.class);

    public final static String[] colors = {
            "aqua","light-blue","cyan","cerulean","little-boy-blue",
            "pale-blue","meerkat","brilliant-white","biscay-green","light-green",
            "lime","lime-punch","green-sheen","pale-green","green-ash",
            "amber", "khaki","sand","yellow","ceylon-yellow",
            "pale-yellow","illuminating","aspen-gold","limelight","meadowlark",
            "orange","mango-mojito","saffron","sunlight",
            "buttercream","desert-mist","soybean","sweet-corn",
            "warm-sand","coconut-milk",
            "vanilla-custard","tofu","almond-buff",
            "white","grey","light-grey","quiet-grey","harbor-mist",
            "pale-red","coral-pink","rose-tan","sweet-lilac",
            "creme-de-peche","crocus-petal","pink-lavender",
            "blooming-dahlia","almost-mauve",
    };

    public static final List<List<Color>> colorLists;

    static {
        colorLists = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            colorLists.add(new ArrayList<>());
        }
        for (int i = 0; i < colors.length; ++i) {
            colorLists.get(i%5).add(new Color(colors[i]));
        }
    }

    public static class Color {
        private final String name;

        public Color(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
