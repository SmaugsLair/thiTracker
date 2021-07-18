package com.smaugslair.thitracker.ui.powers.transformers;

import com.smaugslair.thitracker.data.powers.Sheetable;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.lang.reflect.InvocationTargetException;

public interface Transformer<K extends Sheetable> {

    public String[] getLabels();
    public int getLabelRowIndex();
    public K transformRow(XSSFRow row) throws InvocationTargetException, IllegalAccessException;

    public String getSheetName();
}
