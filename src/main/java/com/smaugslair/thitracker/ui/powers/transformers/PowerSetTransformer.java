package com.smaugslair.thitracker.ui.powers.transformers;

import com.smaugslair.thitracker.data.powers.PowerSet;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PowerSetTransformer extends Transformer<PowerSet>{

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

    public PowerSet transformRow(XSSFRow row) throws TransformerException {
        PowerSet powerSet = new PowerSet();
        int column;
        for (column = 0; column < labels.length; ++column) {
            String label = labels[column];
            XSSFCell cell = row.getCell(column);
            if (column == 0) {
                if (cell.toString().isEmpty() || cell.toString().startsWith("More")) {
                    break;
                }
            }
            try {
                if (label.startsWith("ps_")) {
                    String value = cell.toString();
                    if (value != null && !value.isEmpty()) {
                        if (!value.startsWith("*")) {
                            value = String.valueOf(Double.valueOf(cell.toString()).intValue());
                        }
                        String pre = powerSet.getAbilityMods();
                        if (pre == null) {
                            pre = "";
                        }
                        powerSet.setAbilityMods(pre + " " + label.substring(3) + ":" + value);
                    }
                }
                else if (label.equals("updated")) {
                    powerSet.setUpdated(cell.getDateCellValue());
                }
                else {
                    BeanUtils.setProperty(powerSet, label, cell.toString());
                }
            }
            catch (Throwable t) {
                throw new TransformerException(createErrorMessage(row, column, label, cell, t), t);
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
