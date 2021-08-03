package com.smaugslair.thitracker.ui.powers.transformers;

import com.smaugslair.thitracker.data.powers.Power;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PowerTransformer extends Transformer<Power>{

    private static final Logger log = LoggerFactory.getLogger(PowerTransformer.class);

    public static final String[] labels = {"name", "ssid", "am_Perception", "am_Stealth", "am_Aim",	"am_Dodge",
            "am_Strength", "am_Toughness",	"am_Influence",	"am_Self-Control",	"am_Initiative",
            "am_Movement",	"am_choice", "am_Travel Mult",	"shortDescr",	"fullDescr",	"metaPower",
            "subPowers", 	"powerTag",	"assRules",	"prerequisite",	"maxTaken"};


    public static final String sheetName = "PowersList";


    @Override
    public Power transformRow(XSSFRow row) throws TransformerException {
        Power power = new Power();
        int i;
        for (i = 0; i < labels.length; ++i) {
            String label = labels[i];
            XSSFCell cell = row.getCell(i);
            if (cell == null) {
                break;
            }
            try {
                if (label.startsWith("am_")) {
                    String value = cell.toString();
                    if (value != null && !value.isEmpty()) {
                        if (!value.startsWith("*")) {
                            value = String.valueOf(Double.valueOf(cell.toString()).intValue());
                        }
                        String pre = power.getAbilityMods();
                        if (pre == null) {
                            pre = "";
                        }
                        power.setAbilityMods(pre + " " + label.substring(3) + ":" + value);
                    }
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
            catch (Throwable t) {
                throw new TransformerException(createErrorMessage(row, i, label, cell, t), t);
            }
        }
        XSSFRow labelRow = row.getSheet().getRow(getLabelRowIndex());

        //log.info("loading power sets for power "+power.getName());
        boolean found = false;
        while (!found) {
            if (labelRow.getCell(i) == null) {
                break;
            }
            String label = labelRow.getCell(i).toString();
            //log.info("label:"+label);
            if (label.startsWith("set_")) {
                XSSFCell cell = row.getCell(i);
                if (cell != null && !cell.toString().isEmpty()) {
                    try {
                        int value = Double.valueOf(cell.toString()).intValue();
                        power.addToPowerSets(label.substring(4), value);
                    }
                    catch (Throwable t) {
                        throw new TransformerException(createErrorMessage(row,i,label,cell, t), t);
                    }
                }
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
