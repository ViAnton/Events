package com.example.somebody.eventmanager.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.somebody.eventmanager.R;
import com.example.somebody.eventmanager.entitiy.Event;
import com.example.somebody.eventmanager.utils.DateUtils;
import com.example.somebody.eventmanager.utils.EventContentUtils;

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
        mFromDate.setOnClickListener(new DatePickerButtonListener());
        mFromTime = (TextView) findViewById(R.id.from_time);
        mFromTime.setOnClickListener(new TimePickerButtonListener());

        mToDate = (TextView) findViewById(R.id.to_date);
        mToDate.setOnClickListener(new DatePickerButtonListener());
        mToTime = (TextView) findViewById(R.id.to_time);
        mToTime.setOnClickListener(new TimePickerButtonListener());

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
                EventContentUtils.delete(getApplicationContext(), mCurrentEvent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updatePrepare() {
        Long eventID = getIntent().getExtras().getLong(INCOMING_EVENT_ID);
        mCurrentEvent = EventContentUtils.getEventById(getApplicationContext(), eventID);

        mEventTitle.setText(mCurrentEvent.getTitle());
        mEventDescription.setText(mCurrentEvent.getDescription());

        mFromDate.setText(DateUtils.parseDateToString(mCurrentEvent.getDateStart()));
        mFromTime.setText(DateUtils.parseTimeToString(mCurrentEvent.getDateStart()));

        mToDate.setText(DateUtils.parseDateToString(mCurrentEvent.getDateEnd()));
        mToTime.setText(DateUtils.parseTimeToString(mCurrentEvent.getDateEnd()));

        mEditButton.setText(getString(R.string.update_edit_button));
    }

    private void createPrepare() {
        Calendar calendar = Calendar.getInstance();

        mFromDate.setText(DateUtils.parseDateToString(calendar.getTimeInMillis()));
        mFromTime.setText(DateUtils.parseTimeToString(calendar.getTimeInMillis()));
        mToDate.setText(DateUtils.parseDateToString(calendar.getTimeInMillis()));
        mToTime.setText(DateUtils.parseTimeToString(calendar.getTimeInMillis()));

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

            mCurrentEvent.setDateStart(DateUtils.parseFromFullDate(
                    mFromDate.getText().toString(),
                    mFromTime.getText().toString()
            ));
            mCurrentEvent.setDateEnd(DateUtils.parseFromFullDate(
                    mToDate.getText().toString(),
                    mToTime.getText().toString()
            ));
            mCurrentEvent.setDescription(mEventDescription.getText().toString());

            EventContentUtils.update(getApplicationContext(), mCurrentEvent);
            finish();
        }

        private void createEvent() {
            Event event = new Event();

            event.setTitle(mEventTitle.getText().toString());
            event.setCalendarId(Event.DEFAULT_CALENDAR_ID);
            event.setTimeZone(Event.DEFAULT_TIMEZONE);
            event.setDateStart(DateUtils.parseFromFullDate(
                    mFromDate.getText().toString(),
                    mFromTime.getText().toString()
            ));
            event.setDateEnd(DateUtils.parseFromFullDate(
                    mToDate.getText().toString(),
                    mToTime.getText().toString()
            ));
            event.setDescription(mEventDescription.getText().toString());

            EventContentUtils.create(getApplicationContext(), event);
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

    private class DatePickerButtonListener implements View.OnClickListener,
            DatePickerDialog.OnDateSetListener {

        private DatePickerDialog mDatePickerDialog;
        private TextView mDateView;

        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();

            mDateView = (TextView) v;
            calendar.setTimeInMillis(DateUtils.parseStringToDate(mDateView.getText().toString()));

            mDatePickerDialog = new DatePickerDialog(mDateView.getContext(), this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            mDatePickerDialog.show();
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();

            calendar.set(year, month, dayOfMonth);

            mDateView.setText(DateUtils.parseDateToString(calendar.getTimeInMillis()));
        }
    }

    private class TimePickerButtonListener implements View.OnClickListener,
            TimePickerDialog.OnTimeSetListener {

        private TimePickerDialog mTimePickerDialog;
        private TextView mTimeView;

        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();

            mTimeView = (TextView) v;
            calendar.setTimeInMillis(DateUtils.parseStringToTime(mTimeView.getText().toString()));

            mTimePickerDialog = new TimePickerDialog(mTimeView.getContext(), this,
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    true);

            mTimePickerDialog.show();
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();

            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            mTimeView.setText(DateUtils.parseTimeToString(calendar.getTimeInMillis()));
        }
    }
}
