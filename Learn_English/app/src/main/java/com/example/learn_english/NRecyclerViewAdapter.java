package com.example.learn_english;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NRecyclerViewAdapter extends RecyclerView.Adapter<NRecyclerViewAdapter.MyViewHolder> {
    private String selectedTopicName = "";
    Context context;
    ArrayList<TensesModel> tensesModels;
//    private final RecycleViewInterface recycleViewInterface;

    public NRecyclerViewAdapter(Context context, ArrayList<TensesModel> tensesModels) {
        this.context = context;
        this.tensesModels = tensesModels;
//        this.recycleViewInterface = recycleViewInterface;
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuizActivityTenses.class);
                intent.putExtra("selectedTopic",tensesModels.get(position).getTenseName());
                Toast.makeText(context, intent.getStringExtra("selectedTopic"), Toast.LENGTH_SHORT).show();
//                intent.putExtra("title", data.getTitle());
//                intent.putExtra("description", data.getDescription());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tensesModels.size();
    }

//    @Override
//    public void onItemClick(int position) {
//
//
//    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tenseName;

        public  MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tenseName = itemView.findViewById(R.id.tenseName);

//            itemView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    // get position
////                    int pos = getAdapterPosition();
////
////                    // check if item still exists
////                    if(pos != RecyclerView.NO_POSITION){
////                        TensesModel clickedDataItem = tensesModels.get(pos);
////                        recycleViewInterface = clickedDataItem.tenseName;
//////                        Toast.makeText(v.getContext(), "You clicked " + yourClikedItem, Toast.LENGTH_SHORT).show();
////                    }
//
//                    if (recycleViewInterface != null) {
//                        int pos =getAdapterPosition();
//
//                        if(pos != RecyclerView.NO_POSITION){
//                            recycleViewInterface.onItemClick(pos);
//                    }
//                    }
//                }
//            });
        }

    }
}
