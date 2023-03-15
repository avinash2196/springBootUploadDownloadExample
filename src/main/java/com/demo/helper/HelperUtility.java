package com.demo.helper;

import com.demo.model.DemoModel;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;

@UtilityClass
public class HelperUtility {

    /**
     * It creates the cell based in provided sheet at  given row and columnIndex with provided value and cellStyle
     *
     * @param sheet XSSFSheet sheet created by service
     * @param row current row to be processed
     * @param columnIndex column Index of current column
     * @param valueOfCell value of current column to be set
     * @param style font style to be set
     */

    public static void createCell(XSSFSheet sheet, XSSFRow row, int columnIndex, Object valueOfCell, CellStyle style) {

        sheet.autoSizeColumn(columnIndex);
        XSSFCell cell = row.createCell(columnIndex);
        if (valueOfCell instanceof Integer) {

            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {

            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {

            cell.setCellValue((String) valueOfCell);
        }
        else if (valueOfCell instanceof Double) {

            cell.setCellValue((Double) valueOfCell);
        }else {

            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    /**
     * It creates header for provided sheet and workbook
     *
     * @param workbook workbook object created by service
     * @param sheet sheet in provided worksheet
     * @param headers list of headers
     */
    public static void createHeaders(XSSFWorkbook workbook, XSSFSheet sheet, List<String> headers) {

        XSSFRow row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(18);
        style.setFont(font);
        int columnIndex=0;
        for(String header:headers) {

            createCell(sheet, row, columnIndex++, header, style);
        }
    }

    /**
     * It creates the result value of valid cell as per provided result and message.
     * It appends "|" before value if result is not empty else copies the value directly
     *
     * @param result StringBuffer of existing result
     * @param message value of validation
     */
    public static  void createResult(StringBuffer result, String message){

        if(!result.isEmpty())

            result.append(" | ");

        result.append(message);

    }

    /**
     * It checks if model is valid or not
     * returns "OK" is every validation passed else returns specific values separated by "|"
     * It appends "|" before value if result is not empty else copies the value directly
     *
     * @param demoModel object of demoModel extracted from the row
     */
    public static String ifValid(DemoModel demoModel) {

        StringBuffer result=new StringBuffer();
        if(demoModel==null)

            return "InvalidRow";
        if(demoModel.getIntAttribute()>100 || demoModel.getIntAttribute()<0)

            createResult(result,"Invalid Int Attribute");
        if(StringUtil.isBlank(demoModel.getStringAttribute()))

            createResult(result, "Invalid String Attribute");
        if(demoModel.getDoubleAttribute()>1000|| demoModel.getDoubleAttribute()<0)

            createResult(result, "Invalid Double Attribute");
        if(result.isEmpty())

            result.append("OK");

        return result.toString();
    }
}
