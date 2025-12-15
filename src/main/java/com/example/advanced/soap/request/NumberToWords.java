package com.example.advanced.soap.request;

import jakarta.xml.bind.annotation.*;
import java.math.BigInteger;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "NumberToWords", namespace = "http://www.dataaccess.com/webservicesserver/")
public class NumberToWords {

    @XmlElement(name = "ubiNum", namespace = "http://www.dataaccess.com/webservicesserver/")
    private BigInteger ubiNum; // Server này yêu cầu BigInteger (số lớn)

    // Constructor rỗng
    public NumberToWords() {}

    public NumberToWords(BigInteger ubiNum) {
        this.ubiNum = ubiNum;
    }

    public BigInteger getUbiNum() { return ubiNum; }
    public void setUbiNum(BigInteger ubiNum) { this.ubiNum = ubiNum; }
}
