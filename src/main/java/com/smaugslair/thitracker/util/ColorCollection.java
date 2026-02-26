package com.smaugslair.thitracker.util;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ColorCollection {

    //private static final Logger log = LoggerFactory.getLogger(ColorCollection.class);

    public final static String[] colors = {
            "pale-blue",         "pale-green",    "amber",         "pale-red",         "white",
            "light-blue",       "light-green",       "sand",         "coral-pink",       "light-grey",
            "cyan",             "lime",              "yellow",       "sweet-lilac",      "quiet-grey",
            "cerulean",         "lime-punch",        "pale-yellow",  "creme-de-peche",   "harbor-mist",
            "little-boy-blue",  "green-sheen",      "aspen-gold",       "rose-tan",         "tofu",
            "aqua",            "biscay-green",          "sunlight",          "blooming-dahlia",   "grey",
            "meerkat",          "green-ash",        "orange",           "crocus-petal",     "DARK-gray",
            "DARK-blue",        "DARK-green",          "DARK-brown",    "DARK-red",         "DARK-black"
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

    public record Color(String name) {
    }
}
