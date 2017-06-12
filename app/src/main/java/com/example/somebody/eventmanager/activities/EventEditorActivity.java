package com.example.somebody.eventmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.somebody.eventmanager.EventManager;
import com.example.somebody.eventmanager.R;
import com.example.somebody.eventmanager.Utils;
import com.example.somebody.eventmanager.entitiy.Event;

import java.util.Calendar;
import java.util.TimeZone;

public class EventEditorActivity extends AppCompatActivity {


    public static final String INCOMING_EVENT = "incoming_event";
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
        setContentView(R.layout.activity_event_editor);

        mCurrentEvent = (Event) getIntent().getExtras().getSerializable(INCOMING_EVENT);
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


    private void updatePrepare() {
        mEventTitle.setText(mCurrentEvent.getTitle());
        mEventDescription.setText(mCurrentEvent.getDescription());
        mFromTime.setText(Utils.parseLongToFullDate(mCurrentEvent.getDateStart()));
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
                finish();
            } else {
                Event event = new Event();

                event.setTitle(mEventTitle.getText().toString());
                event.setCalendarId(1L);
                event.setTimeZone(TimeZone.getDefault().getID());
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
        }
    }
}
