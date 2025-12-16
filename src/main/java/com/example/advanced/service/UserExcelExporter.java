package com.example.advanced.service;

import com.example.advanced.entity.mysql.AppUser;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

// Class này không cần @Service vì ta sẽ new trực tiếp mỗi khi cần export
public class UserExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<AppUser> listUsers;

    // Constructor nhận danh sách user cần xuất
    public UserExcelExporter(List<AppUser> listUsers) {
        this.listUsers = listUsers;
        workbook = new XSSFWorkbook(); // Tạo một file Excel ảo
    }
    
    // Hàm phụ: Tạo dòng tiêu đề (Header)
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users"); // Tạo Sheet tên là Users

        Row row = sheet.createRow(0); // Dòng đầu tiên (index 0)

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "User ID", style);
        createCell(row, 1, "E-mail", style);
        createCell(row, 2, "Full Name", style);
        createCell(row, 3, "Role", style);
        createCell(row, 4, "Provider", style);
    }

    // Hàm phụ: Tạo ô dữ liệu (Cell)
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount); // Tự động co giãn chiều rộng cột
        Cell cell = row.createCell(columnCount);
        
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    // Hàm phụ: Viết dữ liệu từ List vào các dòng
    private void writeDataLines() {
        int rowCount = 1; // Bắt đầu từ dòng thứ 2 (dòng 1 là header rồi)

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (AppUser user : listUsers) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, user.getId(), style);
            createCell(row, columnCount++, user.getEmail(), style);
            createCell(row, columnCount++, user.getName(), style);
            createCell(row, columnCount++, user.getRole(), style);
            createCell(row, columnCount++, user.getProvider(), style);
        }
    }

    // HÀM CHÍNH: Gọi các hàm trên và xuất ra luồng dữ liệu (Stream)
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream); // Ghi workbook vào đường ống response
        workbook.close(); // Đóng file để giải phóng RAM
        outputStream.close();
    }
}
