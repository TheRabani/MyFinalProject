package com.example.myfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {

    private Context context;
    private ArrayList<Schedule> schedules;
    private SelectListener listener;

    public ScheduleAdapter(Context context, ArrayList<Schedule> schedules, SelectListener listener) {
        this.context = context;
        this.schedules = schedules;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.schedule_layout_item, parent,false);
        return new ScheduleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        holder.SetDetails(schedule);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(schedules.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    class ScheduleHolder extends RecyclerView.ViewHolder{

        private TextView hour, people;


        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.hour);
            people = itemView.findViewById(R.id.people);

        }

        void SetDetails(Schedule schedule)
        {
            hour.setText(""+schedule.getHour());
            people.setText(""+schedule.getPeople());
        }
    }

}
