package com.example.morningritualtracker;

import java.io.Serializable;
import java.util.HashMap;

public class CompletedDay implements Serializable {
    private static final long serialversionUID = 1L;
    HashMap<String, Boolean> listViewItems;
    String imagePath;

    public CompletedDay(HashMap<String, Boolean> list, String directory){
        listViewItems = list;
        imagePath = directory;
    }

    public String getImagePath(){
        return imagePath;
    }
    public HashMap<String, Boolean> getListViewItems(){
        return listViewItems;
    }

}
