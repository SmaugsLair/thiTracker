package com.smaugslair.thitracker.util;

import com.smaugslair.thitracker.data.atd.ActionTimeDefault;

import java.util.List;

public class AtdCache {

    //private static Logger log = LoggerFactory.getLogger(AtdCache.class);

    private static List<ActionTimeDefault> atds;

    public static List<ActionTimeDefault> getAtds() {
        //log.info("returning atds: "+atds);
        return atds;
    }
    public static void setAtds(List<ActionTimeDefault> atds) {
        //log.info("setting atds: "+atds);
        AtdCache.atds = atds;
    }

}
