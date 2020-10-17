package com.example.chattingonlineapplication.Plugins;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConverter {
    private final static String TIME = "yyyy-MM-dd HH:mm:ss z";
    private final static String MINUTES = "HH:mm";
    private static TimeConverter instance;

    public static TimeConverter getInstance() {
        if(instance == null) instance = new TimeConverter();
        return instance;
    }

    public String convertToMinutes(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(MINUTES); // the format of your date
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }
}
