package com.example.advanced.controller;

import com.example.advanced.dto.ChartData;
import com.example.advanced.entity.mysql.AppUser;
import com.example.advanced.entity.postgres.LoginHistory;
import com.example.advanced.repository.mysql.UserRepository;
import com.example.advanced.repository.postgres.LoginHistoryRepository;
import com.example.advanced.service.DashboardService;
import com.example.advanced.service.LogExcelExporter;
import com.example.advanced.service.UserExcelExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;
    
    @Autowired 
    private DashboardService dashboardService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) return "redirect:/";
        
        String email = principal.getAttribute("email");
        AppUser user = userRepository.findByEmail(email).orElseThrow();

        // 2. Kiểm tra quyền Admin
        if (!"ROLE_ADMIN".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền Admin!");
        }

        // 3. Gửi dữ liệu sang HTML
        model.addAttribute("userName", user.getName());
        model.addAttribute("revenue", "1.000.000.000 USD"); // Giả lập dữ liệu doanh thu
        
        // Trả về file HTML tên là "admin-dashboard.html"
        return "admin/dashboard"; 
    }
    
    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response, @AuthenticationPrincipal OAuth2User principal) throws IOException {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập!");
        }

        String email = principal.getAttribute("email");
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User không tồn tại"));

        if (!"ROLE_ADMIN".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xuất báo cáo!");
        }
        
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<AppUser> listUsers = userRepository.findAll();
        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);
        excelExporter.export(response);
    }
    
    @GetMapping("/logs/export/excel")
    public void exportLogsToExcel(HttpServletResponse response, @AuthenticationPrincipal OAuth2User principal) throws IOException {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập!");
        }

        String email = principal.getAttribute("email");
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User không tồn tại"));

        if (!"ROLE_ADMIN".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xuất báo cáo!");
        }
        
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=login_history_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<LoginHistory> listLogs = loginHistoryRepository.findAll();

        LogExcelExporter excelExporter = new LogExcelExporter(listLogs);
        excelExporter.export(response);
    }
    
    @GetMapping("/api/chart/login-stats")
    @ResponseBody
    public List<ChartData> getLoginStats() {
        return dashboardService.getLoginStatistics();
    }
}