package com.smaugslair.thitracker.ui.powers.transformers;

import com.smaugslair.thitracker.data.powers.PowerSet;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PowerSetTransformer extends Transformer<PowerSet>{

    private final Logger log = LoggerFactory.getLogger(PowerSetTransformer.class);

    public static final String[] labels = {"name", "ssid", "updated", "amPerception", "amStealth",
            "amAim", "amDodge", "amStrength", "amToughness", "amInfluence",	"amSelfControl",
            "amInitiative", "amMovement", "amTravelMult", "amChoice", "openText", "abilityText",
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
                if("amChoice".equals(label)) {
                    String value = cell.toString();
                    if (value != null && !value.isEmpty()) {
                        powerSet.setAmChoice(Double.valueOf(cell.toString()).intValue());
                    }
                }
                else if (label.startsWith("am")) {
                    String value = cell.toString();
                    if (value != null && !value.isEmpty()) {
                        if (value.startsWith("*")) {
                            value = value.substring(1);
                        }
                        int number = Double.valueOf(value).intValue();
                        //log.info("row:"+row.getRowNum()+" label:"+label+" number:"+number);
                        BeanUtils.setProperty(powerSet, label, number);
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
        return powerSet;
    }

    @Override
    public String getSheetName() {
        return "PowerSetInformation";
    }
}
