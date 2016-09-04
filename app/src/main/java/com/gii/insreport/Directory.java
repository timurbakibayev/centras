package com.gii.insreport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Timur_hnimdvi on 04-Sep-16.
 */
public class Directory {
    public String name = "";
    public ArrayList<DirectoryItem> items = new ArrayList<>();
    public Map<String,DirectoryItem> itemMap = new HashMap<>();
    public Directory() {

    }
    public ArrayList<DirectoryItem> getItems() {
        return items;
    }

    public void mapToArrayList() {
        items.clear();
        for (Map.Entry<String, DirectoryItem> entry : itemMap.entrySet()) {
            DirectoryItem newDirectoryItem = new DirectoryItem();
            newDirectoryItem = entry.getValue();
            newDirectoryItem.id = entry.getKey();
            items.add(newDirectoryItem);
        }
    }
}
