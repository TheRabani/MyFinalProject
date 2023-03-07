package com.example.myfinalproject;

import androidx.annotation.NonNull;

public class Schedule {
    private String hour;
    private int people;

    public Schedule(String hour, int people) {
        this.hour = hour;
        this.people = people;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    @NonNull
    public String toString()
    {
        return ""+this.hour+" - "+this.people;
    }

}
