package com.example.myfinalproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.util.NumberUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class admin_fragment_schedule extends Fragment implements SelectListener {

    CalendarView calendar;
    TextView editText;
    DatabaseReference databaseReference;
    String date;
    String simpleDate = "";
    RecyclerView normal_rec;
    private ScheduleAdapter scheduleAdapter;
    private ArrayList<Schedule> scheduleArrayList;
    Schedule temp;
    int count = 0;
    public static ArrayList<String> arrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_schedule, container, false);

        normal_rec = view.findViewById(R.id.normal_rec);
        normal_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        scheduleArrayList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(getContext(), scheduleArrayList, this);
        normal_rec.setAdapter(scheduleAdapter);

        calendar = view.findViewById(R.id.calendar3);
        editText = view.findViewById(R.id.date_view3);
        Button search = view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_phone, null, false);
                builder.setView(dialogView);
                AlertDialog ad4 = builder.create();
                ad4.show();

                Button y = ad4.findViewById(R.id.buttonYes2);
                Button n = ad4.findViewById(R.id.buttonNo2);
                EditText editText = ad4.findViewById(R.id.theNum);
                n.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad4.dismiss();
                    }
                });
                y.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder tempBuilder = new AlertDialog.Builder(getActivity());
                        View tempDialogView = getLayoutInflater().inflate(R.layout.simple_list, null, false);
                        tempBuilder.setView(tempDialogView);
                        AlertDialog tempAd = tempBuilder.create();
                        tempAd.show();

                        RecyclerView recyclerView = tempAd.findViewById(R.id.recyclerView5);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        arrayList.clear();

                        String s = editText.getText().toString();
                        if(s.length() >= 10)
                        {
                            FirebaseDatabase.getInstance().getReference("Calendar").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    boolean b = s.charAt(0) == '+';
                                    int count = 0;
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        count++;
                                        for (DataSnapshot c : child.getChildren()) {
                                            String temp = c.getValue().toString();
                                            if (b) {
                                                if (temp.contains(s) || temp.contains("0" + s.substring(4)))
                                                    arrayList.add("" + child.getKey() + " - " + c.getKey());
                                            } else if (temp.contains(s) || temp.contains("+972" + s.substring(1)))
                                                arrayList.add("" + child.getKey() + " - " + c.getKey());
                                        }
//                                    if(count>=dataSnapshot.getChildrenCount())
//                                        adapter.notifyDataSetChanged();
                                    }
                                    arrayList = sortDateTime(arrayList);
                                    MyAdapter adapter = new MyAdapter(arrayList);
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        else
                            Toast.makeText(getContext(), "wrong number", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });

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
        calendar.setMinDate(System.currentTimeMillis());
        return view;
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


    private boolean isSaturday(int day, int month, int year) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date(year, month, day - 1));
        return calendar2.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }

    @Override
    public void onItemLongClicked(Schedule schedule) {
        temp = schedule;
        AlertDialog.Builder tempBuilder = new AlertDialog.Builder(getActivity());
        View tempDialogView = getLayoutInflater().inflate(R.layout.dialog_info, null, false);
        tempBuilder.setView(tempDialogView);
        AlertDialog tempAd = tempBuilder.create();
        tempAd.setCancelable(true);
        tempAd.show();
        RecyclerView phoneList = tempAd.findViewById(R.id.phoneList);
        phoneList.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<MansInfo> mansInfoArrayList = new ArrayList<>();
        PeopleAdapter peopleAdapter = new PeopleAdapter(getContext(), mansInfoArrayList, this);
        phoneList.setAdapter(peopleAdapter);
        databaseReference.child("" + schedule.getHour()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mansInfoArrayList.clear();
                if (snapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (snapshot.getChildrenCount() == 1 && dataSnapshot.getKey().equals("0"))
                            tempAd.findViewById(R.id.noDataPhone).setVisibility(View.VISIBLE);
                        else
                            mansInfoArrayList.add(new MansInfo(dataSnapshot.getValue().toString(), Integer.parseInt("" + dataSnapshot.getKey())));
                    }
                } else
                    tempAd.findViewById(R.id.noDataPhone).setVisibility(View.VISIBLE);
                peopleAdapter.notifyDataSetChanged();
                TextView textView = tempDialogView.findViewById(R.id.peopleTitle);
                textView.setText("הרשומים לאימון ב" + "\n   " + date + " " + "בשעה" + " " + schedule.getHour());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "error- canceled. try again", Toast.LENGTH_SHORT).show();
            }
        });
        tempAd.show();

        FloatingActionButton add = tempAd.findViewById(R.id.addPerson);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_phone, null, false);
                builder.setView(dialogView);
                AlertDialog ad4 = builder.create();
                ad4.show();
                Button y = ad4.findViewById(R.id.buttonYes2);
                Button n = ad4.findViewById(R.id.buttonNo2);
                EditText p = ad4.findViewById(R.id.theNum);
                y.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (p.getText().length() != 10)
                            shake(dialogView, R.id.theNum);
                        else {
                            databaseReference.child(schedule.getHour()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.getValue() != null) {
                                        if (snapshot.hasChild("0")) {
                                            databaseReference.child(schedule.getHour()).child("1").setValue(p.getText().toString());
                                            databaseReference.child(schedule.getHour()).child("0").removeValue();
                                        } else
                                            databaseReference.child(schedule.getHour()).child("" + (snapshot.getChildrenCount() + 1)).setValue(p.getText().toString());
                                        tempAd.findViewById(R.id.noDataPhone).setVisibility(View.INVISIBLE);
                                        ad4.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "err", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });
                n.setOnClickListener(view1 -> ad4.dismiss());
            }
        });
//
//        btnYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String s = simpleDate.trim(), s2 = schedule.getHour().trim();
//                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                databaseReference.child("" + schedule.getHour()).child("" + (12 - schedule.getPeople() + 1)).setValue("" + currentUser.getPhoneNumber());
//                if (schedule.getPeople() == 12)
//                    databaseReference.child("" + schedule.getHour()).child("0").removeValue();
//                ad.dismiss();
//            }
//        });
//        btnNo.setOnClickListener(view -> ad.dismiss());
//        ad.show();

//        Toast.makeText(getContext(), "אין עוד מקומות פנויים", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClicked(String string, String time) {

    }

    @Override
    public void onItemClicked(Gun gun) {

    }

    @Override
    public void onItemLongClicked(MansInfo mansInfo) {
        Toast.makeText(getContext(), "fde", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("רוצה למחוק את" + " " + mansInfo.getPhone() + " " + "מהאימון" + "?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int j = mansInfo.getNum();
                DatabaseReference data = databaseReference.child(temp.getHour());
                data.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int place = 0;
                        count = (int) (snapshot.getChildrenCount());
                        if (count == 1) {
                            data.child("0").setValue("null");
                            data.child("" + j).removeValue();
                        } else {
                            data.child("" + j).removeValue();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (Integer.parseInt(dataSnapshot.getKey()) > j) {
                                    place = Integer.parseInt(dataSnapshot.getKey());
                                    data.child("" + (place - 1)).setValue(dataSnapshot.getValue());
                                }
                            }
                            data.child("" + place).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                databaseReference.child(temp.getHour()).child("" + mansInfo.getNum()).removeValue();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onItemLongClicked(Gun gun) {

    }

    private void shake(View dialogView, int id) {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(0)
                .playOn(dialogView.findViewById(id));
        Toast.makeText(getContext(), "מספר טלפון שגוי", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<String> sortDateTime(ArrayList<String> dateTimeList) {
        // Create a date-time format pattern
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy - HH:mm");

        // Define a custom comparator to sort the list
        Comparator<String> dateTimeComparator = new Comparator<String>() {
            @Override
            public int compare(String dateTime1, String dateTime2) {
                try {
                    // Parse the date-time strings to Date objects
                    Date date1 = dateFormat.parse(dateTime1);
                    Date date2 = dateFormat.parse(dateTime2);

                    // Compare the parsed dates
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Return 0 if there's an error parsing the dates
                return 0;
            }
        };

        // Sort the list using the custom comparator
        Collections.sort(dateTimeList, dateTimeComparator);

        // Return the sorted list
        return dateTimeList;
    }

}