package com.example.advanced.service;

import com.example.advanced.entity.postgres.LoginHistory;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class LogExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<LoginHistory> listLogs;

    public LogExcelExporter(List<LoginHistory> listLogs) {
        this.listLogs = listLogs;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Login History");
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "ID", style);
        createCell(row, 1, "Email User", style);
        createCell(row, 2, "Thời gian Login", style);
        createCell(row, 3, "Trạng thái", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (LoginHistory log : listLogs) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            // Map đúng các trường của LoginHistory
            createCell(row, columnCount++, log.getId(), style);
            createCell(row, columnCount++, log.getEmail(), style);
            createCell(row, columnCount++, log.getLoginTime(), style); 
            createCell(row, columnCount++, log.getStatus(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
