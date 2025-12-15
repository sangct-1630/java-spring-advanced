package com.example.advanced.controller;

import com.example.advanced.service.SoapClient;
import com.example.advanced.soap.response.AddResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/integration")
public class IntegrationController {

    @Autowired
    private SoapClient soapClient;
    
    @GetMapping("/soap/convert")
    public String convertNumber(@RequestParam int number) {
        try {
            
            String words = soapClient.getNumberToWords(number);
            return String.format("Kết quả từ SOAP Legacy: %d -> '%s'", number, words);
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi gọi SOAP: " + e.getMessage();
        }

    }
}
