package com.example.myfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterSQLite extends RecyclerView.Adapter<CustomAdapterSQLite.MyViewHolder> {

    private Context context;
    private ArrayList<String> book_id, book_date, book_time;


    CustomAdapterSQLite(Context context, ArrayList<String> book_id, ArrayList<String> book_date, ArrayList<String> book_time)
    {
        this.context = context;
        this.book_time = book_time;
        this.book_date = book_date;
        this.book_id = book_id;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.book_id.setText(String.valueOf(book_id.get(position)));
        holder.book_date.setText(String.valueOf(book_date.get(position)));
        holder.book_time.setText(String.valueOf(book_time.get(position)));
    }

    @Override
    public int getItemCount() {
        return book_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView book_id, book_date, book_time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id = itemView.findViewById(R.id.book_id);
            book_date = itemView.findViewById(R.id.book_date);
            book_time = itemView.findViewById(R.id.book_time_txt);
        }
    }
}
