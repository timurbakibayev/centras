package com.gii.insreport;

/**
 * Created by Timur_hnimdvi on 16-Aug-16.
 */
public class DirectoryItem {
    public String id = "";
    public String name = "";
    public String status = "";

    public DirectoryItem() {

    }

    public DirectoryItem(String id, String name) {
        this.id = id;
        this.name = name;
        this.status = "Y";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
