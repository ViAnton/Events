package com.example.somebody.eventmanager.activities;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.somebody.eventmanager.EventLoader;
import com.example.somebody.eventmanager.R;
import com.example.somebody.eventmanager.adapters.EventAdapter;
import com.example.somebody.eventmanager.adapters.NoEventAdapter;
import com.example.somebody.eventmanager.entitiy.Event;

import java.util.List;

public class EventManagerActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 999;
    private static final int LOADER_ID = 666;

    private RecyclerView mEventList;
    private RecyclerView.Adapter mEventAdapter;

    private View mLoadStub;
    private ProgressBar mProgressBar;

    private View mErrorPermissionStub;
    private Button mPermissionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);

        mEventList = (RecyclerView) findViewById(R.id.event_list_view);

        mLoadStub = findViewById(R.id.load_layout_stub);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_stub);

        mErrorPermissionStub = findViewById(R.id.error_permission_stub);
        mPermissionButton = (Button) findViewById(R.id.permission_button);
        mPermissionButton.setOnClickListener(new PermissionButtonClick());
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkCalendarPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_event:
                Intent intent = new Intent(this, EventEditorActivity.class);
                intent.putExtra(EventEditorActivity.INCOMING_OPERATION,
                        EventEditorActivity.CREATE_OPERATION);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (isGranted()) {
                mErrorPermissionStub.setVisibility(View.GONE);
                startLoad();
            } else
                mErrorPermissionStub.setVisibility(View.VISIBLE);
        }
    }

    private void checkCalendarPermission() {
        if (isGranted())
            startLoad();
        else
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                    PERMISSION_REQUEST_CODE);
    }

    private boolean isGranted() {
        int readPerm = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR);

        int writePerm = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR);

        return readPerm == PackageManager.PERMISSION_GRANTED &&
                writePerm == PackageManager.PERMISSION_GRANTED;
    }

    private void startLoad() {
        mLoadStub.setVisibility(View.VISIBLE);
        mProgressBar.setEnabled(true);

        getSupportLoaderManager().initLoader(LOADER_ID, null, new EventLoaderCallbacks());
    }

    private class PermissionButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            checkCalendarPermission();
        }
    }

    private class EventLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Event>> {

        @Override
        public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
            return new EventLoader(getApplicationContext());
        }

        @Override
        public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
            if (data.isEmpty())
                mEventAdapter = new NoEventAdapter();
            else
                mEventAdapter = new EventAdapter(data);

            mEventList.setLayoutManager(new LinearLayoutManager(EventManagerActivity.this));
            mEventList.setAdapter(mEventAdapter);

            mLoadStub.setVisibility(View.GONE);
            mProgressBar.setEnabled(false);
        }

        @Override
        public void onLoaderReset(Loader<List<Event>> loader) {

        }
    }
}