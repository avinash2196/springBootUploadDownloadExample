package com.demo.service;

import com.demo.model.DemoModel;
import com.demo.model.OutputModel;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.demo.helper.HelperUtility.*;

@Service
public class DemoService {

    /**
     * It processes the uploaded file and return the list of demoModel objects.
     *
     * @param files multipart file uploaded by user
     * @Return List<DemoModel>  List of DemoModel objects
     */
    @SneakyThrows
    public List<DemoModel> uploadFile(MultipartFile files) {

        List<DemoModel> demoModelList = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int index = 1; index < worksheet.getPhysicalNumberOfRows(); index++) {

            XSSFRow row = worksheet.getRow(index);
            demoModelList.add(DemoModel.builder().intAttribute((int) row.getCell(0).getNumericCellValue()).stringAttribute(row.getCell(1).getStringCellValue()).doubleAttribute(row.getCell(2).getNumericCellValue()).build());
        }

        workbook.close();

        return demoModelList;

    }

    /**
     * It processes the uploaded file and return the OutputModel.
     *
     * @param files multipart file uploaded by user
     * @Return OutputModel object
     */
    public OutputModel uploadFileV1(MultipartFile files) {
        List<DemoModel> modelList = uploadFile(files);

        return createFile(modelList);
    }

    /**
     * It processes the list of demoModel object and generates the output model based on if row is valid or not
     *
     * @param modelList multipart file uploaded by user
     * @Return OutputModel object
     */
    @SneakyThrows
    private OutputModel createFile(List<DemoModel> modelList) {

        boolean isValid = true;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Output");
        List<String> headers = Arrays.asList("IntAttribute", "StringAttribute", "DoubleAttribute", "BooleanAttribute");
        createHeaders(workbook, sheet, headers);
        int rowCount = 1;
        for (DemoModel demoModel : modelList) {

            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeight(16);
            style.setFont(font);
            XSSFRow row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(sheet, row, columnCount++, demoModel.getIntAttribute(), style);
            createCell(sheet, row, columnCount++, demoModel.getStringAttribute(), style);
            createCell(sheet, row, columnCount++, demoModel.getDoubleAttribute(), style);
            String validCheck = ifValid(demoModel);
            if (!validCheck.equalsIgnoreCase("OK")) isValid = false;

            createCell(sheet, row, columnCount, validCheck, style);

        }
        workbook.write(out);
        workbook.close();

        return OutputModel.builder().isValid(isValid).byteArrayInputStream(new ByteArrayInputStream(out.toByteArray())).build();
    }


}

