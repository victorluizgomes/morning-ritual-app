package com.example.morningritualtracker;

import java.io.Serializable;
import java.util.HashMap;

public class CompletedDay implements Serializable {
    private static final long serialversionUID = 1L;
    HashMap<String, Boolean> listViewItems;
    String imagePath;

    /*
    This will have the list of items done for the task and the image with it.
     */
    public CompletedDay(HashMap<String, Boolean> list, String directory){
        listViewItems = list;
        imagePath = directory;
    }
    /*
    return the image
     */
    public String getImagePath(){
        return imagePath;
    }
    /*
    return all the lists of tasks whether completed or not.
     */
    public HashMap<String, Boolean> getListViewItems(){
        return listViewItems;
    }

}
