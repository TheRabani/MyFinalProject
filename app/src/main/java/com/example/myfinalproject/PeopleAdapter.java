package com.example.myfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleHolder> {

    private Context context;
    private ArrayList<MansInfo> info;
    private SelectListener listener;

    public PeopleAdapter(Context context, ArrayList<MansInfo> info, SelectListener listener) {
        this.context = context;
        this.info = info;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PeopleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.phone_numbers_layout, parent,false);
        return new PeopleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleHolder holder, int position) {
        MansInfo mansInfo = info.get(position);
        holder.SetDetails(mansInfo);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemLongClicked(mansInfo);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    class PeopleHolder extends RecyclerView.ViewHolder{

        private TextView phone, num;

        public PeopleHolder(@NonNull View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.phone);
            num = itemView.findViewById(R.id.num);
        }

        void SetDetails(MansInfo mansInfo)
        {
            phone.setText(""+mansInfo.getPhone());
            num.setText(""+mansInfo.getNum());
        }
    }
}
