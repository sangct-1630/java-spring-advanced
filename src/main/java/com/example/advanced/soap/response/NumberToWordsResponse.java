package com.example.advanced.soap.response;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "NumberToWordsResponse", namespace = "http://www.dataaccess.com/webservicesserver/")
public class NumberToWordsResponse {

    @XmlElement(name = "NumberToWordsResult", namespace = "http://www.dataaccess.com/webservicesserver/")
    private String result;

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}
