package com.example.somebody.eventmanager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.AsyncTaskLoader;

import com.example.somebody.eventmanager.entitiy.Event;
import com.example.somebody.eventmanager.utils.EventContentUtils;

import java.util.ArrayList;
import java.util.List;

public class EventLoader extends AsyncTaskLoader<List<Event>> {

    private final EventContentObserver mEventContentObserver;

    public EventLoader(Context context) {
        super(context);
        mEventContentObserver = new EventContentObserver();
        context.getContentResolver().registerContentObserver(
                CalendarContract.Events.CONTENT_URI,
                false, mEventContentObserver
        );
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Event> loadInBackground() {
        List<Event> events = new ArrayList<>();

        Cursor cursor = getEventsCursor();

        if (cursor != null) {
            EventContentUtils.fillList(cursor, events);
            cursor.close();
        }

        return events;
    }

    private Cursor getEventsCursor() {
        ContentResolver resolver = getContext().getContentResolver();
        Cursor cursor = null;

        // лишняя проверка
        int readPerm = ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CALENDAR);

        int writePerm = ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_CALENDAR);

        if (readPerm == PackageManager.PERMISSION_GRANTED &&
                writePerm == PackageManager.PERMISSION_GRANTED)

            cursor = resolver.query(CalendarContract.Events.CONTENT_URI, null,
                    null, null, null);

        return cursor;
    }


    private class EventContentObserver extends ContentObserver {
        public EventContentObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            onContentChanged();
        }
    }
}
