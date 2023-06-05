package com.example.learn_english;

import android.content.Context;
import android.content.Intent;
import android.service.autofill.UserData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PrepositionsRecyclerViewAdapter extends RecyclerView.Adapter<PrepositionsRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<PrepositionsModel> prepositionsModels;

    public PrepositionsRecyclerViewAdapter(Context context, ArrayList<PrepositionsModel> prepositionsModels) {
        this.context = context;
        this.prepositionsModels = prepositionsModels;
    }

    @NonNull
    @Override
    public PrepositionsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrepositionsRecyclerViewAdapter.MyViewHolder holder, int position) {
        PrepositionsModel prepositionsModel = prepositionsModels.get(position);
        holder.prepositionName.setText(prepositionsModel.getPrepostionName());
        getUserData(position, holder);

        //        int progress = getUserProgressForItem(position);
//
//        // Update the progress bar for the item
//        holder.progressBar.setProgress(progress);
        holder.ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserData(position, holder);
                Intent intent = new Intent(context, QuizActivityPrepositions.class);
                intent.putExtra("selectedTopic", prepositionsModels.get(position).getPrepostionName());
                context.startActivity(intent);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserData(position, holder);
                Intent intent = new Intent(context, LearnActivity.class);
                intent.putExtra("selectedTopic", prepositionsModels.get(position).getPrepostionName());
                context.startActivity(intent);
            }
        });
    }



    private UserData getUserData(int position, MyViewHolder holder) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("prepositions").child(prepositionsModels.get(position).getPrepostionName().replace(" ", ""));

        final ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int myProgress = snapshot.getValue(Integer.class);
                    updateProgressBar(position, myProgress, holder);

                    // Update the RecyclerView item after getting the data
                    notifyItemChanged(position);
                } else {
                    // Handle the case where the presentSimpleProgress value doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error case
            }
        };

        // Add a ValueEventListener to listen for changes in the data
        userRef.addValueEventListener(valueEventListener);
        return null;
    }

    private void updateProgressBar(int position, int progress, MyViewHolder holder) {
        prepositionsModels.get(position).setProgress(progress);
        holder.progressBar.setProgress(progress);
    }

    @Override
    public int getItemCount() {
        return prepositionsModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView prepositionName;
        View imageView;
        LinearLayout ll2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            prepositionName = itemView.findViewById(R.id.itemName);
            imageView = itemView.findViewById(R.id.imageView3);
            ll2 = itemView.findViewById(R.id.linearLayout2);
            progressBar = itemView.findViewById(R.id.progressBar3);
        }
    }
}