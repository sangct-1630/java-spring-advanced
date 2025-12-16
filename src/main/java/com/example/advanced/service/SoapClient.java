package com.example.advanced.service;

import com.example.advanced.soap.request.NumberToWords;
import com.example.advanced.soap.response.NumberToWordsResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.math.BigInteger;

public class SoapClient extends WebServiceGatewaySupport {

    public String getNumberToWords(int number) {
        // 1. Tạo request
        NumberToWords request = new NumberToWords(BigInteger.valueOf(number));

        System.out.println("📤 SOAP Request: Đang gửi số " + number + " đi...");

        // 2. Gửi request đến URL mới
        // URL: https://www.dataaccess.com/webservicesserver/NumberConversion.wso
        // Action: "" (Server này không yêu cầu Action cụ thể, để rỗng cũng được)
        try {
            NumberToWordsResponse response = (NumberToWordsResponse) getWebServiceTemplate()
                    .marshalSendAndReceive(
                            "https://www.dataaccess.com/webservicesserver/NumberConversion.wso",
                            request,
                            new SoapActionCallback("") // Để trống hoặc điền namespace đều được
                    );
            return response.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi gọi SOAP: " + e.getMessage();
        }
    }
}
