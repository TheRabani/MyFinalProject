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
    private SelectListener listener;


    CustomAdapterSQLite(Context context, ArrayList<String> book_id, ArrayList<String> book_date, ArrayList<String> book_time, SelectListener listener)
    {
        this.context = context;
        this.book_time = book_time;
        this.book_date = book_date;
        this.book_id = book_id;
        this.listener = listener;
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
        String date = String.valueOf(book_date.get(position));
        String realDate = date.substring(1, date.indexOf('M')) + "/" + date.substring(date.indexOf('M') + 1, date.indexOf('Y')) + "/" + date.substring(date.indexOf('Y') + 1);
        holder.book_date.setText(realDate);
        holder.book_time.setText(String.valueOf(book_time.get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(book_date.get(holder.getAdapterPosition()));
            }
        });
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
