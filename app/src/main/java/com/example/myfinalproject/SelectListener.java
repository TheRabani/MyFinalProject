package com.example.myfinalproject;

public interface SelectListener {
    void onItemClicked(Schedule schedule);
    void onItemClicked(String string, String time);
    void onItemClicked(Gun gun);
    void onItemLongClicked(Gun gun);
}
