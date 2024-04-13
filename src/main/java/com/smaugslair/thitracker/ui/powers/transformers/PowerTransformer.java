package com.smaugslair.thitracker.ui.powers.transformers;

import com.smaugslair.thitracker.data.powers.Power;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PowerTransformer extends Transformer<Power>{

    private static final Logger log = LoggerFactory.getLogger(PowerTransformer.class);

    public static final String[] labels = {"name", "ssid", "amPerception", "amStealth", "amAim",
            "amDodge", "amStrength", "amToughness",	"amInfluence",	"amSelfControl", "amInitiative",
            "amMovement", "amChoice", "amTravelMult", "shortDescr",	"fullDescr", "metaPower",
            "subPowers", "powerTag", "assRules", "prerequisite", "maxTaken"};


    public static final String sheetName = "PowersList";

    private final DataFormatter dataFormatter = new DataFormatter();


    @Override
    public Power transformRow(XSSFRow row) throws TransformerException {
        Power power = new Power();
        int i;
        boolean track = "Read Animal Memory".equals(row.getCell(0).toString());
        for (i = 0; i < labels.length; ++i) {
            String label = labels[i];
            XSSFCell cell = row.getCell(i);
            try {
                if (cell == null) {
                    throw new Exception(power.getName() + " has a Null cell! Editing the contents of this cell in Sheets should resolve this issue.");
                }
                if ("amChoice".equals(label)) {
                    String value = cell.toString();
                    if (value != null && !value.isEmpty()) {
                        power.setAmChoice(Double.valueOf(cell.toString()).intValue());
                    }
                }
                if (label.startsWith("am")) {
                    String value = cell.toString();
                    if (value != null && !value.isEmpty()) {
                        if (value.startsWith("*")) {
                            value = value.substring(1);
                        }
                        int number = Double.valueOf(value).intValue();
                        BeanUtils.setProperty(power, label, number);
                    }
                }
                else if (label.equals("metaPower")) {
                    power.setMetaPower(cell.toString().equals("1.0"));
                }
                else if (label.equals("maxTaken")) {
                    if (cell.toString().isEmpty()) {
                        throw new Exception(power.getName() + " has an empty maxTaken value");
                    }
                    //power.setMaxTaken(dataFormatter.formatCellValue(cell));
                    power.setMaxTaken(Double.valueOf(cell.toString()).intValue());
                }
                else {
                    BeanUtils.setProperty(power, label, dataFormatter.formatCellValue(cell));
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
