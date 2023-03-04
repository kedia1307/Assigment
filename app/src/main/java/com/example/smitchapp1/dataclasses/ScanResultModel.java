package com.example.smitchapp1.dataclasses;

public class ScanResultModel {
    private String serviceName;

    public ScanResultModel(String serviceName, String serviceType, String ipAddress, int port) {
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }


    private String serviceType;
    private String ipAddress;
    private int port;


}
