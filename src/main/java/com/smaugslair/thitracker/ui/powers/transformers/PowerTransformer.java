package com.smaugslair.thitracker.ui.powers.transformers;

import com.smaugslair.thitracker.data.powers.Power;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class PowerTransformer implements Transformer<Power>{

    private static Logger log = LoggerFactory.getLogger(PowerTransformer.class);

    public static final String[] labels = {"name", "ssid", "am_Perception", "am_Stealth", "am_Aim",	"am_Dodge",
            "am_Strength", "am_Toughness",	"am_Influence",	"am_Self-Control",	"am_Initiative",
            "am_Movement",	"am_choice", "am_Travel Mult",	"shortDescr",	"fullDescr",	"metaPower",
            "subPowers", 	"powerTag",	"assRules",	"prerequisite",	"maxTaken"};


    public static final String sheetName = "PowersList";


    @Override
    public Power transformRow(XSSFRow row) throws InvocationTargetException, IllegalAccessException {
        Power power = new Power();
        int i;
        for (i = 0; i < labels.length; ++i) {
            String label = labels[i];
            XSSFCell cell = row.getCell(i);
            if (cell == null) {
                throw new IllegalAccessException("Sheet:"+getSheetName()+", cell is null at index "+i+ " for power "+power.getName());
            }
            if (label.startsWith("am_")) {
                try {
                    String value;
                    if (cell.toString().startsWith("*")) {
                        value = cell.toString();
                    }
                    else {
                        value = String.valueOf(Double.valueOf(cell.toString()).intValue());
                    }
                    String pre = power.getAbilityMods();
                    if (pre == null) {
                        pre = "";
                    }
                    power.setAbilityMods(pre + " " + label.substring(3) + ":" + value);

                }
                catch (Throwable t) {}
            }
            else if (label.equals("metaPower")) {
                power.setMetaPower(cell.toString().equals("1.0"));
            }
            else {
                BeanUtils.setProperty(power, label, cell.toString());
            }
            if (power.getAbilityMods() != null) {
                power.setAbilityMods(power.getAbilityMods().trim());
            }
        }
        XSSFRow labelRow = row.getSheet().getRow(getLabelRowIndex());

        //log.info("loading power sets for power "+power.getName());
        boolean found = false;
        while (!found) {
            //log.info("index="+i);
            String label = labelRow.getCell(i).toString();
            //log.info("label:"+label);
            if (label.startsWith("set_")) {
                XSSFCell cell = row.getCell(i);
                try {
                    int value = Double.valueOf(cell.toString()).intValue();
                    power.addToPowerSets(label.substring(4), value);
                }
                catch (Throwable t) {}
            }
            else if (label.equals("endColumn")) {
                found = true;
            }
            ++i;
        }

        return power;
    }

    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public String[] getLabels() {
        return labels;
    }

    @Override
    public int getLabelRowIndex() {
        return 3;
    }


}
