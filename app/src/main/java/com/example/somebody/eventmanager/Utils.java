package com.example.somebody.eventmanager;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private static final String DATE_PATTERN = "d.MM.yyyy";
    private static final String TIME_PATTERN = "HH:mm";
    private static final String FULL_DATE_PATTERN = TIME_PATTERN + ", " + DATE_PATTERN;

    public static final String CONCAT_DATE_PATTERN = " - ";

    private Utils() {}

    public static String parseLongToFullDate(Long date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FULL_DATE_PATTERN);
        return simpleDateFormat.format(new Date(date));
    }

    public static String parseLongToDate(Long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        return simpleDateFormat.format(new Date(date));
    }

    public static String parseLongToTime(Long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_PATTERN);
        return simpleDateFormat.format(new Date(date));
    }

    public static Long parseFullDateToLong(String date, String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FULL_DATE_PATTERN);
        String full = time + ", " + date;
        Long result = null;

        try {
            result = simpleDateFormat.parse(full).getTime();
        } catch (ParseException e) {
            Log.wtf("Parse: ", e.getLocalizedMessage());
        }

        return result;
    }
}
