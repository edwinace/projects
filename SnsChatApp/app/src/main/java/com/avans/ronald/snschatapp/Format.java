package com.avans.ronald.snschatapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Luuk on 16-3-2015.
 */
public class Format
{
    public static Date formatTimeStamp(String timeStamp){
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            date = format.parse(timeStamp);
        }catch (ParseException e){
            System.out.print(e.getMessage());
        }


        return date;
    }

    public static String fancyDate(String timeStamp) {
        Date date = new Date();
        Date formatDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            formatDate = format.parse(timeStamp);
        }catch (ParseException e){
            System.out.print(e.getMessage());
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.get(Calendar.DAY_OF_MONTH);
        int today = cal.get(Calendar.DAY_OF_MONTH);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(formatDate);
        int chatDate = cal.get(Calendar.DAY_OF_MONTH);

        int diff = today - chatDate;
        
        if (diff == 0) {
            return new SimpleDateFormat("HH:mm").format(formatDate);
        }

        if (diff == 1) {
            return "gisteren";
        } else {
            return new SimpleDateFormat("dd-MM-yyyy").format(formatDate);
        }
    }
}
