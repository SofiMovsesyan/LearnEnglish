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

public class TensesRecyclerViewAdapter extends RecyclerView.Adapter<TensesRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<TensesModel> tensesModels;
    private Map<Integer, ValueEventListener> activeListeners;

    public TensesRecyclerViewAdapter(Context context, ArrayList<TensesModel> tensesModels) {
        this.context = context;
        this.tensesModels = tensesModels;
        this.activeListeners = new HashMap<>();
    }

    @NonNull
    @Override
    public TensesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TensesRecyclerViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TensesModel tensesModel = tensesModels.get(position);

        // Set initial data
        holder.tenseName.setText(tensesModel.getTenseName());
        holder.progressBar.setProgress(tensesModel.getProgress());
        holder.progressText.setText(tensesModel.getProgress() + "% completed");

        // Remove any existing listener for this position
        removeListener(position);

        // Set up new Firebase listener for this position
        setupFirebaseListener(position, holder);

        // Set click listeners
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuizActivityTenses.class);
                intent.putExtra("selectedTopic", tensesModels.get(position).getTenseName());
                intent.putExtra("itemPosition", position);
                context.startActivity(intent);
            }
        });

        holder.learnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LearnActivity.class);
                intent.putExtra("selectedTopic", tensesModels.get(position).getTenseName());
                intent.putExtra("itemPosition", position);
                context.startActivity(intent);
            }
        });
    }

    private void setupFirebaseListener(int position, MyViewHolder holder) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("tenses").child(tensesModels.get(position).getTenseName().replace(" ", ""));

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int myProgress = snapshot.getValue(Integer.class);
                    // Update only if this holder is still bound to the correct position
                    if (holder.getAdapterPosition() == position) {
                        tensesModels.get(position).setProgress(myProgress);
                        holder.progressBar.setProgress(myProgress);
                        holder.progressText.setText(myProgress + "% completed");
                    }
                } else {
                    // If no progress exists, set to 0
                    if (holder.getAdapterPosition() == position) {
                        tensesModels.get(position).setProgress(0);
                        holder.progressBar.setProgress(0);
                        holder.progressText.setText("0% completed");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                if (holder.getAdapterPosition() == position) {
                    holder.progressBar.setProgress(0);
                    holder.progressText.setText("0% completed");
                }
            }
        };

        // Store the listener
        activeListeners.put(position, valueEventListener);
        userRef.addValueEventListener(valueEventListener);
    }

    private void removeListener(int position) {
        if (activeListeners.containsKey(position)) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId).child("tenses").child(tensesModels.get(position).getTenseName().replace(" ", ""));

            userRef.removeEventListener(activeListeners.get(position));
            activeListeners.remove(position);
        }
    }

    public void cleanup() {
        for (Map.Entry<Integer, ValueEventListener> entry : activeListeners.entrySet()) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId).child("tenses").child(tensesModels.get(entry.getKey()).getTenseName().replace(" ", ""));
            userRef.removeEventListener(entry.getValue());
        }
        activeListeners.clear();
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
        // Remove listener when view is recycled
        int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            removeListener(position);
        }
    }

    @Override
    public int getItemCount() {
        return tensesModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView tenseName;
        TextView progressText;
        ImageView learnIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tenseName = itemView.findViewById(R.id.itemName);
            learnIcon = itemView.findViewById(R.id.imageView3);
            progressBar = itemView.findViewById(R.id.progressBar3);
            progressText = itemView.findViewById(R.id.progressText);
        }
    }
}