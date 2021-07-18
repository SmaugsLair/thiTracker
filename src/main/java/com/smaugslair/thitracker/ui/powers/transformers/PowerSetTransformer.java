package com.smaugslair.thitracker.ui.powers.transformers;

import com.smaugslair.thitracker.data.powers.PowerSet;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class PowerSetTransformer implements Transformer<PowerSet>{

    private final Logger log = LoggerFactory.getLogger(PowerSetTransformer.class);

    public static final String[] labels = {"name", "ssid", "updated", "ps_Perception", "ps_Stealth", "ps_Aim",	"ps_Dodge",
            "ps_Strength", "ps_Toughness",	"ps_Influence",	"ps_Self-Control",	"ps_Initiative",
            "ps_Movement",	"ps_Travel Mult",	"ps_Choice",	"openText",	"abilityText",
            "powersText"};


    @Override
    public String[] getLabels() {
        return labels;
    }

    @Override
    public int getLabelRowIndex() {
        return 3;
    }

    public PowerSet transformRow(XSSFRow row) throws InvocationTargetException, IllegalAccessException {
        PowerSet powerSet = new PowerSet();
        int i;
        for (i = 0; i < labels.length; ++i) {
            String label = labels[i];
            XSSFCell cell = row.getCell(i);
            if (cell == null) {
                throw new IllegalAccessException("Sheet:"+getSheetName()+", cell is null at index "+i+ " for PowerSet "+ powerSet.getName());
            }
            if (label.startsWith("ps_")) {
                try {
                    String value;
                    if (cell.toString().startsWith("*")) {
                        value = cell.toString();
                    }
                    else {
                        value = String.valueOf(Double.valueOf(cell.toString()).intValue());
                    }
                    String pre = powerSet.getAbilityMods();
                    if (pre == null) {
                        pre = "";
                    }
                    powerSet.setAbilityMods(pre + " " + label.substring(3) + ":" + value);
                }
                catch (Throwable t) {}
            }
            else if (label.equals("updated")) {
                try {
                    powerSet.setUpdated(cell.getDateCellValue());
                } catch (Throwable e) {
                    log.info(e.getMessage()+", cell contents:"+cell.toString());
                    //e.printStackTrace();
                }
            }
            else {
                BeanUtils.setProperty(powerSet, label, cell.toString());
            }
        }
        if (powerSet.getAbilityMods() != null) {
            powerSet.setAbilityMods(powerSet.getAbilityMods().trim());
        }
        return powerSet;
    }

    @Override
    public String getSheetName() {
        return "PowerSetInformation";
    }
}
