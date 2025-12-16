package com.example.advanced.soap.response;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AddResponse", namespace = "http://tempuri.org/")
public class AddResponse {

    @XmlElement(name = "AddResult", namespace = "http://tempuri.org/")
    private int addResult;

    public int getAddResult() { return addResult; }
    public void setAddResult(int addResult) { this.addResult = addResult; }
}
