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

public class WordsRecyclerViewAdapter extends RecyclerView.Adapter<WordsRecyclerViewAdapter.MyViewHolder> {
    private String selectedTopicName = "";
    Context context;
    ArrayList<WordsModel> wordsModels;
//    private final RecycleViewInterface recycleViewInterface;


    public WordsRecyclerViewAdapter(Context context, ArrayList<WordsModel> wordsModels) {
        this.context = context;
        this.wordsModels = wordsModels;
    }

    @NonNull
    @Override
    public WordsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordsRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.wordName.setText(wordsModels.get(position).getWordsName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuizActivityWords.class);
                intent.putExtra("selectedTopic",wordsModels.get(position).getWordsName());
                Toast.makeText(context, intent.getStringExtra("selectedTopic"), Toast.LENGTH_SHORT).show();
//                intent.putExtra("title", data.getTitle());
//                intent.putExtra("description", data.getDescription());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordsModels.size();
    }

//    @Override
//    public void onItemClick(int position) {
//
//
//    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView wordName;

        public  MyViewHolder(@NonNull View itemView) {
            super(itemView);
            wordName = itemView.findViewById(R.id.itemName);


        }

    }
}
