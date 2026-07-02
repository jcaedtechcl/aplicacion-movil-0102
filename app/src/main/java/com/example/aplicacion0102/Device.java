package com.example.aplicacion0102;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class Device {
    private String id;
    private String name;
    
    @SerializedName("data")
    private Map<String, Object> deviceData;

    public Device(String name, Map<String, Object> deviceData) {
        this.name = name;
        this.deviceData = deviceData;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Map<String, Object> getDeviceData() { return deviceData; }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Data: " + deviceData;
    }
}