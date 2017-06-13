package com.example.somebody.eventmanager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;

import com.example.somebody.eventmanager.entitiy.Event;

import java.util.List;

public class EventManager {

    public static void fillList(Cursor source, List<Event> target) {
        if (source.moveToFirst())
            while (!source.isAfterLast()) {
                target.add(createEventFromCursor(source));
                source.moveToNext();
            }
    }

    public static Event getEventById(Context context, long id) {
        ContentResolver resolver = context.getContentResolver();
        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);

        Cursor cursor = resolver.query(updateUri, null, null, null, null);
        return createEventFromCursor(cursor);
    }

    private static Event createEventFromCursor(Cursor cursor) {
        Event event = new Event();
        event.setId(getLong(cursor, CalendarContract.Events._ID));
        event.setTitle(getString(cursor, CalendarContract.Events.TITLE));
        event.setCalendarId(getLong(cursor, CalendarContract.Events.CALENDAR_ID));
        event.setDateStart(getLong(cursor, CalendarContract.Events.DTSTART));
        event.setDateEnd(getLong(cursor, CalendarContract.Events.DTEND));
        event.setDescription(getString(cursor, CalendarContract.Events.DESCRIPTION));

        return event;
    }

    public static boolean update(Context context, Event event) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.TITLE, event.getTitle());
        values.put(CalendarContract.Events.DTSTART, event.getDateStart());
        values.put(CalendarContract.Events.DTEND, event.getDateEnd());
        values.put(CalendarContract.Events.DESCRIPTION, event.getDescription());

        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, event.getId());
        int rows = resolver.update(updateUri, values, null, null);
        return rows != 0;
    }

    public static boolean create(Context context, Event event) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.TITLE, event.getTitle());
        values.put(CalendarContract.Events.DTSTART, event.getDateStart());
        values.put(CalendarContract.Events.CALENDAR_ID, event.getCalendarId());
        values.put(CalendarContract.Events.EVENT_TIMEZONE, event.getTimeZone());
        values.put(CalendarContract.Events.DTEND, event.getDateEnd());
        values.put(CalendarContract.Events.DESCRIPTION, event.getDescription());

        boolean isSuccessful = false;

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED)
            isSuccessful = resolver.insert(CalendarContract.Events.CONTENT_URI, values) != null;

        return isSuccessful;
    }

    public static boolean delete(Context context, Event event) {
        ContentResolver resolver = context.getContentResolver();

        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,
                event.getId());
        int rows = resolver.delete(updateUri, null, null);
        return rows != 0;
    }

    private static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    private static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
}