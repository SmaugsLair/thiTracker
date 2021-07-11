package com.smaugslair.thitracker.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ColorCollection {

    private static final Logger log = LoggerFactory.getLogger(ColorCollection.class);

    public final static String[] colors = {
            "amber", //hover-amber:hover{color:#000!important;background-color:#ffc107!important}
            "aqua", //hover-aqua:hover{color:#000!important;background-color:#00ffff!important}
            "blue", //hover-blue:hover{color:#fff!important;background-color:#2196F3!important}
            "light-blue", //hover-light-blue:hover{color:#000!important;background-color:#87CEEB!important}
            "brown", //hover-brown:hover{color:#fff!important;background-color:#795548!important}
            "cyan", //hover-cyan:hover{color:#000!important;background-color:#00bcd4!important}
            "blue-grey", //hover-blue-grey:hover", //blue-gray", //hover-blue-gray:hover{color:#fff!important;background-color:#607d8b!important}
            "green", //hover-green:hover{color:#fff!important;background-color:#4CAF50!important}
            "light-green", //hover-light-green:hover{color:#000!important;background-color:#8bc34a!important}
            "indigo", //hover-indigo:hover{color:#fff!important;background-color:#3f51b5!important}
            "khaki", //hover-khaki:hover{color:#000!important;background-color:#f0e68c!important}
            "lime", //hover-lime:hover{color:#000!important;background-color:#cddc39!important}
            "orange", //hover-orange:hover{color:#000!important;background-color:#ff9800!important}
            "deep-orange", //hover-deep-orange:hover{color:#fff!important;background-color:#ff5722!important}
            "pink", //hover-pink:hover{color:#fff!important;background-color:#e91e63!important}
            "purple", //hover-purple:hover{color:#fff!important;background-color:#9c27b0!important}
            "deep-purple", //hover-deep-purple:hover{color:#fff!important;background-color:#673ab7!important}
            "red", //hover-red:hover{color:#fff!important;background-color:#f44336!important}
            "sand", //hover-sand:hover{color:#000!important;background-color:#fdf5e6!important}
            "teal", //hover-teal:hover{color:#fff!important;background-color:#009688!important}
            "yellow", //hover-yellow:hover{color:#000!important;background-color:#ffeb3b!important}
            "white", //hover-white:hover{color:#000!important;background-color:#fff!important}
            //"black", //hover-black:hover{color:#fff!important;background-color:#000!important}
            "grey", //hover-grey:hover", //gray", //hover-gray:hover{color:#000!important;background-color:#9e9e9e!important}
            "light-grey", //hover-light-grey:hover", //light-gray", //hover-light-gray:hover{color:#000!important;background-color:#f1f1f1!important}
            //"dark-grey", //hover-dark-grey:hover", //dark-gray", //hover-dark-gray:hover{color:#fff!important;background-color:#616161!important}
            "pale-red", //hover-pale-red:hover{color:#000!important;background-color:#ffdddd!important}
            "pale-green", //hover-pale-green:hover{color:#000!important;background-color:#ddffdd!important}
            "pale-yellow", //hover-pale-yellow:hover{color:#000!important;background-color:#ffffcc!important}
            "pale-blue", //hover-pale-blue:hover{color:#000!important;background-color:#ddffff!important}
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
            log.info(""+name);
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
