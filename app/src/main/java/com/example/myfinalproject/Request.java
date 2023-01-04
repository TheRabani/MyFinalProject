package com.example.myfinalproject;

public class Request {
    private String id;
    private int count;

    public Request(String id) {
        this.id = id;
        this.count = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
