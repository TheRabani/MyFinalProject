package com.example.myfinalproject;

import static com.example.myfinalproject.HomeFragment.book_date;
import static com.example.myfinalproject.HomeFragment.book_id;
import static com.example.myfinalproject.HomeFragment.book_time;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment implements SelectListener {

    CalendarView calendar;
    TextView editText;
    DatabaseReference databaseReference;
    String date;
    String simpleDate = "";
    RecyclerView normal_rec;
    private ScheduleAdapter scheduleAdapter;
    private ArrayList<Schedule> scheduleArrayList;
    public static Context context;
    public static long timeInMilliseconds = 0;

//    CustomAdapterSQLite customAdapterSQLite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        context = getContext();

        view.findViewById(R.id.logout2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "ds", Toast.LENGTH_SHORT).show();
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(getActivity(), Login.class));
            }
        });

        normal_rec = view.findViewById(R.id.normal_rec);
        normal_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        scheduleArrayList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(getContext(), scheduleArrayList, this);
        normal_rec.setAdapter(scheduleAdapter);

        calendar = view.findViewById(R.id.calendar);
        editText = view.findViewById(R.id.date_view);

//        storeDataInArrays();
//        customAdapterSQLite = new CustomAdapterSQLite(getActivity(), book_id, book_date, book_time);
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
//        calendar.setMinDate(System.currentTimeMillis());
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
//                        Schedule schedule = new Schedule(dataSnapshot.getKey(), Integer.parseInt(dataSnapshot.getValue().toString()));
//                        scheduleArrayList.add(schedule);

                        Schedule schedule = new Schedule(dataSnapshot.getKey(), (int) (12 - dataSnapshot.getChildrenCount()));
                        if (dataSnapshot.getChildrenCount() == 1 && dataSnapshot.child("0").getValue() != null)
                            schedule.setPeople(12);
                        scheduleArrayList.add(schedule);
                    }

                } else {
                    if (!isSaturday) {
                        databaseReference.child("10:00").child("0").setValue("null");
                        databaseReference.child("12:00").child("0").setValue("null");
                        databaseReference.child("08:00").child("0").setValue("null");
                        databaseReference.child("14:30").child("0").setValue("null");
                        databaseReference.child("16:30").child("0").setValue("null");
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
    public void onItemLongClicked(Schedule schedule) {
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
                        String s = simpleDate.trim(), s2 = schedule.getHour().trim();

                        String month = s.substring(s.indexOf("M") + 1, s.indexOf("Y"));
                        String day = s.substring(s.indexOf("D") + 1, s.indexOf("M"));
                        String year = s.substring(s.indexOf("Y") + 1);

                        myDB.addBook(s, s2);
                        addTime(s, s2);
//                        storeDataInArrays();
//                        databaseReference.child("" + schedule.getHour()).setValue(schedule.getPeople() - 1);
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        databaseReference.child("" + schedule.getHour()).child("" + (12 - schedule.getPeople() + 1)).setValue("" + currentUser.getPhoneNumber());
                        if (schedule.getPeople() == 12)
                            databaseReference.child("" + schedule.getHour()).child("0").removeValue();
                        ad.dismiss();

                        //all version of android
                        Intent i = new Intent();

                        // mimeType will popup the chooser any  for any implementing application (e.g. the built in calendar or applications such as "Business calendar"
                        i.setType("vnd.android.cursor.item/event");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, Integer.parseInt(""+year)); // Set the year
                        calendar.set(Calendar.MONTH, getMonth(Integer.parseInt(""+month))); // Set the month (Note: January is 0)
                        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(""+day)); // Set the day of the month
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s2.substring(0, 2))); // Set the hour (in 24-hour format)
                        calendar.set(Calendar.MINUTE, Integer.parseInt(s2.substring(3))); // Set the minute

                        // the time the event should start in millis. This example uses now as the start time and ends in 1 hour
                        i.putExtra("beginTime", calendar.getTimeInMillis());
                        i.putExtra("endTime", calendar.getTimeInMillis() + DateUtils.HOUR_IN_MILLIS);
                        String eventTitle = "אימון במטווח קרב";
                        String eventLocation = "תלפיות יד חרוצים 13";
                        i.putExtra("title", eventTitle);
                        i.putExtra("eventLocation", eventLocation);

                        // the action
                        i.setAction(Intent.ACTION_EDIT);

                        startActivity(i);
                    }
                });
                btnNo.setOnClickListener(view -> ad.dismiss());
                ad.show();
            } else
                Toast.makeText(getContext(), "כבר קבעת אימון ליום הזה", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), "אין עוד מקומות פנויים", Toast.LENGTH_SHORT).show();
    }

    private int getMonth(int i) {
        switch (i) {
            case 1:
                return Calendar.JANUARY;
            case 2:
                return Calendar.FEBRUARY;
            case 3:
                return Calendar.MARCH;
            case 4:
                return Calendar.APRIL;
            case 5:
                return Calendar.MAY;
            case 6:
                return Calendar.JUNE;
            case 7:
                return Calendar.JULY;
            case 8:
                return Calendar.AUGUST;
            case 9:
                return Calendar.SEPTEMBER;
            case 10:
                return Calendar.OCTOBER;
            case 11:
                return Calendar.NOVEMBER;
            default:
                return Calendar.DECEMBER;
        }
    }

    @Override
    public void onItemLongClicked(String string, String time) {

    }

    @Override
    public void onItemClicked(Gun gun) {

    }

    @Override
    public void onItemLongClicked(MansInfo mansInfo) {

    }

    @Override
    public void onItemLongClicked(Gun gun) {

    }

    private void addTime(String s, String s2) {
        Toast.makeText(getContext(), "" + book_id.size(), Toast.LENGTH_SHORT).show();
        book_id.add(String.valueOf(book_id.size() + 1));
        book_date.add(s);
        book_time.add(s2);
    }

    public boolean isAvailable() {
        if (book_date.size() == 0)
            return true;
        String st = simpleDate.substring(simpleDate.indexOf("M") + 1, simpleDate.indexOf("Y"));
        String st2 = simpleDate.substring(simpleDate.indexOf("D") + 1, simpleDate.indexOf("M"));
        String st3 = simpleDate.substring(simpleDate.indexOf("Y") + 1);

        for (String s : book_date) {
            String temp = s.substring(s.indexOf("M") + 1, s.indexOf("Y"));
            String temp2 = s.substring(s.indexOf("D") + 1, s.indexOf("M"));
            String temp3 = s.substring(s.indexOf("Y") + 1);

            if (temp.equals(st) && temp2.equals(st2) && temp3.equals(st3))
                return false;
        }
        return true;
    }

    public static void addOne(String date, String hour) {
        DatabaseReference a = FirebaseDatabase.getInstance().getReference("Calendar").child(date);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        a.child(hour).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (snapshot.getChildrenCount() != 1) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.getValue().toString().equals(currentUser.getPhoneNumber()))
                                a.child(hour).child(dataSnapshot.getKey()).removeValue();
                        }
                    } else {
                        a.child(hour).child("0").setValue("null");
                        a.child(hour).child("1").removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context.getApplicationContext(), "error- canceled. try again", Toast.LENGTH_SHORT).show();
            }
        });
//        a.child(hour).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(task.isSuccessful())
//                {
//                    a.child("" + hour).setValue(Integer.parseInt(task.getResult().getValue().toString()) + 1);
//                }
//                else
//                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
//            }
//        });


    }
}