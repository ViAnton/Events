package com.example.somebody.eventmanager.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.somebody.eventmanager.R;
import com.example.somebody.eventmanager.activities.EventEditorActivity;
import com.example.somebody.eventmanager.entitiy.Event;
import com.example.somebody.eventmanager.utils.DateUtils;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventItemViewHolder> {
    private final List<Event> mEvents;

    public EventAdapter(List<Event> events) {
        mEvents = events;
    }

    @Override
    public EventItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.event_item_list, parent, false);

        return new EventItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventItemViewHolder holder, int position) {
        Event currentEvent = mEvents.get(position);
        Long dtStart = currentEvent.getDateStart();
        Long dtEnd = currentEvent.getDateEnd();

        holder.mEventTitle.setText(currentEvent.getTitle());
        holder.mEventTitle.setTag(currentEvent.getId());

        holder.mEventDt.setText(DateUtils.parseToFullDate(dtStart)
                .concat(DateUtils.CONCAT_DATE_PATTERN)
                .concat(DateUtils.parseToFullDate(dtEnd)));
        holder.mEventDescription.setText(currentEvent.getDescription());
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    class EventItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView mEventTitle;
        TextView mEventDt;
        TextView mEventDescription;

        public EventItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mEventTitle = (TextView) itemView.findViewById(R.id.event_title);
            mEventDt = (TextView) itemView.findViewById(R.id.event_DT);
            mEventDescription = (TextView) itemView.findViewById(R.id.event_description);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), EventEditorActivity.class);
            intent.putExtra(EventEditorActivity.INCOMING_EVENT_ID,
                    (Long) mEventTitle.getTag());
            intent.putExtra(EventEditorActivity.INCOMING_OPERATION,
                    EventEditorActivity.UPDATE_OPERATION);

            v.getContext().startActivity(intent);
        }
    }
}