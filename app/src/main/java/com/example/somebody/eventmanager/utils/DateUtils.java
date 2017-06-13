package com.example.somebody.eventmanager.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final String DATE_PATTERN = "d MMM yyyy";
    private static final String TIME_PATTERN = "HH:mm";
    private static final String FULL_DATE_PATTERN = TIME_PATTERN + ", " + DATE_PATTERN;

    public static final String CONCAT_DATE_PATTERN = " - ";

    private DateUtils() {
    }

    public static String parseToString(String pattern, long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date(date));
    }

    public static long parseToDate(String pattern, String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        long result = 0L;

        try {
            result = simpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            Log.wtf("Parse: ", e.getLocalizedMessage());
        }

        return result;
    }

    public static String parseToFullDate(long date) {
        return parseToString(FULL_DATE_PATTERN, date);
    }

    public static String parseDateToString(long date) {
        return parseToString(DATE_PATTERN, date);
    }

    public static String parseTimeToString(long date) {
        return parseToString(TIME_PATTERN, date);
    }

    public static long parseStringToDate(String date) {
        return parseToDate(DATE_PATTERN, date);
    }

    public static long parseStringToTime(String date) {
        return parseToDate(TIME_PATTERN, date);
    }

    public static long parseFromFullDate(String date, String time) {
        String full = time + ", " + date;
        return parseToDate(FULL_DATE_PATTERN, full);
    }
}
