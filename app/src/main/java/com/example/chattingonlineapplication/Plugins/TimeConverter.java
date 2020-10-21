package com.example.chattingonlineapplication.Plugins;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConverter {
    private final static String GENERAL = "yyyy-MM-dd";
    private final static String TIME = "yyyy-MM-dd HH:mm:ss z";
    private final static String MINUTES = "HH:mm";
    private static TimeConverter instance;
    private SimpleDateFormat dateFormat;

    public static TimeConverter getInstance() {
        if(instance == null) instance = new TimeConverter();
        return instance;
    }

    public String convertToMinutes(Date date) {
        dateFormat = new SimpleDateFormat(MINUTES); // the format of your date
        return dateFormat.format(date);
    }

    public String convertToGeneral(Date date){
        dateFormat = new SimpleDateFormat(GENERAL); // the format of your date
        return dateFormat.format(date);
    }
}
