package com.example.somebody.eventmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.somebody.eventmanager.EventManager;
import com.example.somebody.eventmanager.R;
import com.example.somebody.eventmanager.Utils;
import com.example.somebody.eventmanager.entitiy.Event;

import java.util.Calendar;

public class EventEditorActivity extends AppCompatActivity {


    public static final String INCOMING_EVENT_ID = "incoming_event_id";
    public static final String INCOMING_OPERATION = "incoming_operation";

    public static final int CREATE_OPERATION = 1;
    public static final int UPDATE_OPERATION = 3;

    private Event mCurrentEvent;
    private int mCurrentOperation;

    private EditText mEventTitle;
    private EditText mEventDescription;
    private TextView mFromDate;
    private TextView mFromTime;
    private TextView mToDate;
    private TextView mToTime;
    private Button mEditButton;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_editor_activity);
        mCurrentOperation = getIntent().getExtras().getInt(INCOMING_OPERATION);

        mEventTitle = (EditText) findViewById(R.id.title_edit_text);
        mEventDescription = (EditText) findViewById(R.id.description_edit_text);

        mFromDate = (TextView) findViewById(R.id.from_date);
        mFromTime = (TextView) findViewById(R.id.from_time);
        mToDate = (TextView) findViewById(R.id.to_date);
        mToTime = (TextView) findViewById(R.id.to_time);

        mEditButton = (Button) findViewById(R.id.edit_button);
        mEditButton.setOnClickListener(new EditButtonListener());
        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new CancelButtonListener());

        if (mCurrentOperation == CREATE_OPERATION)
            createPrepare();
        else
            updatePrepare();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mCurrentOperation == UPDATE_OPERATION)
            getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_event:
                EventManager.delete(getApplicationContext(), mCurrentEvent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updatePrepare() {
        Long eventID = getIntent().getExtras().getLong(INCOMING_EVENT_ID);
        mCurrentEvent = EventManager.getEventById(getApplicationContext(), eventID);

        mEventTitle.setText(mCurrentEvent.getTitle());
        mEventDescription.setText(mCurrentEvent.getDescription());

        mFromDate.setText(Utils.parseLongToDate(mCurrentEvent.getDateStart()));
        mFromTime.setText(Utils.parseLongToTime(mCurrentEvent.getDateStart()));

        mToDate.setText(Utils.parseLongToDate(mCurrentEvent.getDateEnd()));
        mToTime.setText(Utils.parseLongToTime(mCurrentEvent.getDateEnd()));

        mEditButton.setText(getString(R.string.update_edit_button));
    }

    private void createPrepare() {
        Calendar calendar = Calendar.getInstance();

        mFromDate.setText(Utils.parseLongToDate(calendar.getTimeInMillis()));
        mFromTime.setText(Utils.parseLongToTime(calendar.getTimeInMillis()));
        mToDate.setText(Utils.parseLongToDate(calendar.getTimeInMillis()));
        mToTime.setText(Utils.parseLongToTime(calendar.getTimeInMillis()));

        mEditButton.setText(getString(R.string.save_edit_button));
    }

    private class CancelButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    private class EditButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mCurrentOperation == UPDATE_OPERATION) {
                if (checkInputValues()) {
                    updateEvent();
                } else
                    displayErrorMessage();

            } else {
                if (checkInputValues()) {
                    createEvent();
                } else
                    displayErrorMessage();
            }
        }

        private void updateEvent() {
            mCurrentEvent.setTitle(mEventTitle.getText().toString());

            mCurrentEvent.setDateStart(Utils.parseFullDateToLong(
                    mFromDate.getText().toString(),
                    mFromTime.getText().toString()
            ));
            mCurrentEvent.setDateEnd(Utils.parseFullDateToLong(
                    mToDate.getText().toString(),
                    mToTime.getText().toString()
            ));
            mCurrentEvent.setDescription(mEventDescription.getText().toString());

            EventManager.update(getApplicationContext(), mCurrentEvent);
            finish();
        }

        private void createEvent() {
            Event event = new Event();

            event.setTitle(mEventTitle.getText().toString());
            event.setCalendarId(Event.DEFAULT_CALENDAR_ID);
            event.setTimeZone(Event.DEFAULT_TIMEZONE);
            event.setDateStart(Utils.parseFullDateToLong(
                    mFromDate.getText().toString(),
                    mFromTime.getText().toString()
            ));
            event.setDateEnd(Utils.parseFullDateToLong(
                    mToDate.getText().toString(),
                    mToTime.getText().toString()
            ));
            event.setDescription(mEventDescription.getText().toString());

            EventManager.create(getApplicationContext(), event);
            finish();
        }

        private boolean checkInputValues() {
            return !mEventTitle.getText().toString().isEmpty() ||
                    !mEventDescription.getText().toString().isEmpty();
        }

        private void displayErrorMessage() {
            Toast.makeText(EventEditorActivity.this, "Одно из полей должно быть заполнено",
                    Toast.LENGTH_LONG).show();
        }
    }
}
