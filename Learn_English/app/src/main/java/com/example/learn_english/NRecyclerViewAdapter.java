package com.example.learn_english;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NRecyclerViewAdapter extends RecyclerView.Adapter<NRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<TensesModel> tensesModels;

    public NRecyclerViewAdapter(Context context, ArrayList<TensesModel> tensesModels) {
        this.context = context;
        this.tensesModels = tensesModels;
    }

    @NonNull
    @Override
    public NRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tenses_recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tenseName.setText(tensesModels.get(position).getTenseName());
    }

    @Override
    public int getItemCount() {
        return tensesModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tenseName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tenseName = itemView.findViewById(R.id.tenseName);
        }

    }
}
