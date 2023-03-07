package com.example.myfinalproject;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment implements SelectListener {

    CalendarView calendar;
    TextView editText;
    DatabaseReference databaseReference;
    String date;
    String simpleDate = "";
    RecyclerView normal_rec;
    private ScheduleAdapter scheduleAdapter;
    private ArrayList<Schedule> scheduleArrayList;

    MyDatabaseHelper myDB;
    ArrayList<String> book_id, book_date, book_time;
    CustomAdapterSQLite customAdapterSQLite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        normal_rec = view.findViewById(R.id.normal_rec);
        normal_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        scheduleArrayList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(getContext(), scheduleArrayList, this);
        normal_rec.setAdapter(scheduleAdapter);

        calendar = (CalendarView) view.findViewById(R.id.calendar);
        editText = (TextView) view.findViewById(R.id.date_view);

        storeDataInArrays();
        customAdapterSQLite = new CustomAdapterSQLite(getActivity(), book_id, book_date, book_time);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                date = dayOfMonth + "-" + (month + 1) + "-" + year;
                databaseReference = FirebaseDatabase.getInstance().getReference("Calendar").child(date);
                editText.setText("אימונים בתאריך: " + date);
                simpleDate = "D" + dayOfMonth + "M" + (month + 1) + "Y" + year;
                calendarClicked(isSaturday(dayOfMonth, month, year));
            }
        });
        calendar.setMinDate(System.currentTimeMillis());
        return view;
    }

    private boolean isSaturday(int day, int month, int year) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date(year, month, day - 1));
        return calendar2.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }

    private void calendarClicked(boolean isSaturday) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scheduleArrayList.clear();
                if (snapshot.getValue() != null) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Schedule schedule = new Schedule(dataSnapshot.getKey(), Integer.parseInt(dataSnapshot.getValue().toString()));
                        scheduleArrayList.add(schedule);

                    }

                } else {
                    if (!isSaturday) {
                        databaseReference.child("10:00").setValue(12);
                        databaseReference.child("12:00").setValue(12);
                        databaseReference.child("08:00").setValue(12);
                        databaseReference.child("14:30").setValue(12);
                        databaseReference.child("16:30").setValue(12);
                    } else
                        Toast.makeText(getContext(), "המטווח אינו פעיל בשבת", Toast.LENGTH_SHORT).show();
                }
                scheduleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "error- canceled. try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClicked(Schedule schedule) {
        if (schedule.getPeople() != 0) {
            if (isAvailable()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_save_spot, null, false);
                builder.setView(dialogView);
                AlertDialog ad = builder.create();
                ad.setCancelable(false);
                Button btnYes = dialogView.findViewById(R.id.buttonYes);
                Button btnNo = dialogView.findViewById(R.id.buttonNo);
                TextView textView = dialogView.findViewById(R.id.textView);
                textView.setText("להירשם לאימון בתאריך" + "\n" + date + " " + "בשעה" + " " + schedule.getHour() + "?");

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyDatabaseHelper myDB = new MyDatabaseHelper(getActivity());
                        myDB.addBook(simpleDate.trim(), schedule.getHour().trim());
                        storeDataInArrays();
                        databaseReference.child("" + schedule.getHour()).setValue(schedule.getPeople() - 1);
                        ad.dismiss();
                    }
                });
                btnNo.setOnClickListener(view -> ad.dismiss());
                ad.show();
            } else
                Toast.makeText(getContext(), "כבר קבעת אימון לחודש הזה", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), "אין עוד מקומות פנויים", Toast.LENGTH_SHORT).show();
    }

    void storeDataInArrays() {
        myDB = new MyDatabaseHelper(getContext());
        book_time = new ArrayList<>();
        book_id = new ArrayList<>();
        book_date = new ArrayList<>();
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0)
            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        else
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_date.add(cursor.getString(1));
                book_time.add(cursor.getString(2));
            }
    }

    public boolean isAvailable() {
        if (book_date == null || book_date.size() == 0)
            return true;
        String st = simpleDate.substring(simpleDate.indexOf("M") + 1, simpleDate.indexOf("Y"));
        for (String s : book_date) {
            String temp = s.substring(s.indexOf("M") + 1, s.indexOf("Y"));
            if (temp.equals(st))
                return false;
        }
        return true;
    }
}