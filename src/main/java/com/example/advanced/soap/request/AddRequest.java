package com.example.advanced.soap.request;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Add", namespace = "http://tempuri.org/")
public class AddRequest {

    @XmlElement(name = "intA", namespace = "http://tempuri.org/")
    private int intA;

    @XmlElement(name = "intB", namespace = "http://tempuri.org/")
    private int intB;

    public int getIntA() { return intA; }
    public void setIntA(int intA) { this.intA = intA; }

    public int getIntB() { return intB; }
    public void setIntB(int intB) { this.intB = intB; }
}
