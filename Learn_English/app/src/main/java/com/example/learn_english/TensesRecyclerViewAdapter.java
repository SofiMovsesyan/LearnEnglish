
package com.example.learn_english;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
/*
public class TensesRecyclerViewAdapter extends RecyclerView.Adapter<TensesRecyclerViewAdapter.MyViewHolder> {
    private String selectedTopicName = "";
    Context context;
    ArrayList<TensesModel> tensesModels;
    private int progress = 0;
//    private final RecycleViewInterface recycleViewInterface;

    public TensesRecyclerViewAdapter(Context context, ArrayList<TensesModel> tensesModels, int progress) {
        this.context = context;
        this.tensesModels = tensesModels;
        this.progress = progress;
    }

    @NonNull
    @Override
    public TensesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);
        return new MyViewHolder(view);
    }
    public void updateProgress(int progress) {
        this.progress = progress;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull TensesRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tenseName.setText(tensesModels.get(position).getTenseName());
        holder.progressBar.setProgress(progress);

        holder.ll2.setOnClickListener(new View.OnClickListener() {
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


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LearnActivity.class);
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
        ProgressBar progressBar;
        TextView tenseName;
        View imageView;
        LinearLayout ll2;

        public  MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tenseName = itemView.findViewById(R.id.itemName);
            imageView = itemView.findViewById(R.id.imageView3);
            ll2 = itemView.findViewById(R.id.linearLayout2);
            progressBar = itemView.findViewById(R.id.progressBar3);
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
*/
public class TensesRecyclerViewAdapter extends RecyclerView.Adapter<TensesRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<TensesModel> tensesModels;

    public TensesRecyclerViewAdapter(Context context, ArrayList<TensesModel> tensesModels) {
        this.context = context;
        this.tensesModels = tensesModels;
    }

    @NonNull
    @Override
    public TensesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TensesRecyclerViewAdapter.MyViewHolder holder, int position) {
        TensesModel tensesModel = tensesModels.get(position);
        holder.tenseName.setText(tensesModel.getTenseName());
        holder.progressBar.setProgress(tensesModel.getProgress());

        holder.ll2.setOnClickListener(new View.OnClickListener() {
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


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LearnActivity.class);
                intent.putExtra("selectedTopic",tensesModels.get(position).getTenseName());
                Toast.makeText(context, intent.getStringExtra("selectedTopic"), Toast.LENGTH_SHORT).show();
//                intent.putExtra("title", data.getTitle());
//                intent.putExtra("description", data.getDescription());

                context.startActivity(intent);
            }
        });

        // Add your click listeners here


    }

    @Override
    public int getItemCount() {
        return tensesModels.size();
    }

    public void updateProgress(int itemPosition, int progress) {
        if (itemPosition >= 0 && itemPosition < tensesModels.size()) {
            TensesModel tensesModel = tensesModels.get(itemPosition);
            tensesModel.setProgress(progress);
            notifyItemChanged(itemPosition);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView tenseName;
        View imageView;
        LinearLayout ll2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tenseName = itemView.findViewById(R.id.itemName);
            imageView = itemView.findViewById(R.id.imageView3);
            ll2 = itemView.findViewById(R.id.linearLayout2);
            progressBar = itemView.findViewById(R.id.progressBar3);
        }
    }
}
