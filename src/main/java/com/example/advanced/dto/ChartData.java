package com.example.advanced.dto;

public class ChartData {
    private String label;
    private long value;
    public ChartData(String label, long value) {
        this.label = label;
        this.value = value;
    }

    // Getters & Setters
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public long getValue() { return value; }
    public void setValue(long value) { this.value = value; }
}
