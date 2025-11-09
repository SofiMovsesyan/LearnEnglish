package com.example.learn_english;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordsRecyclerViewAdapter extends RecyclerView.Adapter<WordsRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<WordsModel> wordsModels;
    private Map<Integer, ValueEventListener> activeListeners;

    public WordsRecyclerViewAdapter(Context context, ArrayList<WordsModel> wordsModels) {
        this.context = context;
        this.wordsModels = wordsModels;
        this.activeListeners = new HashMap<>();
    }

    @NonNull
    @Override
    public WordsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordsRecyclerViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        WordsModel wordsModel = wordsModels.get(position);

        // Set initial data
        holder.wordName.setText(wordsModel.getWordsName());
        holder.progressBar.setProgress(wordsModel.getProgress());
        holder.progressText.setText(wordsModel.getProgress() + "% անցած է");

        // Remove any existing listener for this position
        removeListener(position);

        // Set up new Firebase listener
        setupFirebaseListener(position, holder);

        // Open quiz
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuizActivityWords.class);
                intent.putExtra("selectedTopic", wordsModels.get(position).getWordsName());
                intent.putExtra("itemPosition", position);
                context.startActivity(intent);
            }
        });

        // Open learning page
        holder.learnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LearnActivity.class);
                intent.putExtra("selectedTopic", wordsModels.get(position).getWordsName());
                intent.putExtra("itemPosition", position);
                context.startActivity(intent);
            }
        });
    }

    private void setupFirebaseListener(int position, MyViewHolder holder) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("words").child(wordsModels.get(position).getWordsName().replace(" ", ""));

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int myProgress = snapshot.getValue(Integer.class);
                    if (holder.getAdapterPosition() == position) {
                        wordsModels.get(position).setProgress(myProgress);
                        holder.progressBar.setProgress(myProgress);
                        holder.progressText.setText(myProgress + "% անցած է");
                    }
                } else {
                    if (holder.getAdapterPosition() == position) {
                        wordsModels.get(position).setProgress(0);
                        holder.progressBar.setProgress(0);
                        holder.progressText.setText("0% անցած է");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (holder.getAdapterPosition() == position) {
                    holder.progressBar.setProgress(0);
                    holder.progressText.setText("0% անցած է");
                }
            }
        };

        activeListeners.put(position, valueEventListener);
        userRef.addValueEventListener(valueEventListener);
    }

    private void removeListener(int position) {
        if (activeListeners.containsKey(position)) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId).child("words").child(wordsModels.get(position).getWordsName().replace(" ", ""));
            userRef.removeEventListener(activeListeners.get(position));
            activeListeners.remove(position);
        }
    }

    public void cleanup() {
        for (Map.Entry<Integer, ValueEventListener> entry : activeListeners.entrySet()) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId).child("words").child(wordsModels.get(entry.getKey()).getWordsName().replace(" ", ""));
            userRef.removeEventListener(entry.getValue());
        }
        activeListeners.clear();
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
        int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            removeListener(position);
        }
    }

    @Override
    public int getItemCount() {
        return wordsModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView wordName;
        TextView progressText;
        ImageView learnIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            wordName = itemView.findViewById(R.id.itemName);
            learnIcon = itemView.findViewById(R.id.imageView3);
            progressBar = itemView.findViewById(R.id.progressBar3);
            progressText = itemView.findViewById(R.id.progressText);
        }
    }
}
