package com.example.demo.Services.UploadImage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties("file")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "D:/demo/uploads";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
