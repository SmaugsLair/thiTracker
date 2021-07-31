package com.smaugslair.thitracker.ui.powers.transformers;

import com.smaugslair.thitracker.data.powers.Sheetable;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

public abstract class Transformer<K extends Sheetable> {

    public abstract String[] getLabels();
    public abstract int getLabelRowIndex();
    public abstract K transformRow(XSSFRow row) throws TransformerException;

    public abstract String getSheetName();

    protected String  createErrorMessage(XSSFRow row, int columnIndex, String columnLabel,
                                         XSSFCell cell, Throwable t) {

        StringBuilder sb = new StringBuilder();
        sb.append("Problem in sheet:").append(row.getSheet().getSheetName())
                .append(", columnLabel:").append(columnLabel)
                .append(", at row:").append(row.getRowNum()+1).append(", ");
        if (cell == null) {
            sb.append("null cell at column: "+columnIndex+1);
        }
        else {
            sb.append("column:").append(cell.getColumnIndex()+1);
            sb.append(", cell value:").append(cell.toString());

        }
        sb.append(" --- errorMessage:").append(t.getMessage());
        return sb.toString();
    }
}
