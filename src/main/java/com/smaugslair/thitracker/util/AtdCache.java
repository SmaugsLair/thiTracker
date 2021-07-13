package com.smaugslair.thitracker.util;

import com.smaugslair.thitracker.data.atd.ActionTimeDefault;

import java.util.List;

public class AtdCache {

    //private static Logger log = LoggerFactory.getLogger(AtdCache.class);

    private static List<ActionTimeDefault> atds;

    public static List<ActionTimeDefault> getAtds() {
        return atds;
    }
    public static void setAtds(List<ActionTimeDefault> atds) {
        AtdCache.atds = atds;
    }

}
