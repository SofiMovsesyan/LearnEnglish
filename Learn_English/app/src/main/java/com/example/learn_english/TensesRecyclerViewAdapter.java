
package com.example.learn_english;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
/*
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
//        holder.progressBar.setProgress(tensesModel.getProgress());


        holder.ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = getUserProgressForItem(position);

                // Update the progress bar for the clicked item
                holder.progressBar.setProgress(progress);
                // Update the progress bar for the clicked item
                updateProgressBar(position, progress);
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
                int progress = getUserProgressForItem(position);

                // Update the progress bar for the clicked item
                holder.progressBar.setProgress(progress);
                // Update the progress bar for the clicked item
                updateProgressBar(position, progress);
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
    public void updateProgressBar(int position, int progress) {
        // Update the progress value in the TensesModel at the specified position
        tensesModels.get(position).setProgress(progress);
        // Notify the adapter that the data has changed for the specified position
        notifyItemChanged(position);
    }
    private int getUserProgressForItem(int position) {
        TensesModel tensesModel = tensesModels.get(position);

        // Assuming you have a database or data source where you can retrieve the progress value for the specific item
        // Retrieve the progress value for the item based on its ID or any other identifier
        String itemId = tensesModel.getItemId(); // Replace with the appropriate method to retrieve the item ID
        int progress = 0; // Default progress value
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserData userData = getUserData(userId);
        // Retrieve the progress value from the database or data source based on the item ID
        // Implement your logic here to fetch the progress value for the item

        return progress;
    }
    private UserData getUserData(String userId) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference userRef = usersRef.child(userId).child("tenses").child("presentSimpleProgress");

        // Read the presentSimpleProgress value from the database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int presentSimpleProgress = snapshot.getValue(Integer.class);
                    Toast.makeText(context, "" + presentSimpleProgress, Toast.LENGTH_SHORT).show();
                    // Call a method or update UI with the retrieved presentSimpleProgress value
                    // For example: updateProgressBar(presentSimpleProgress);
                } else {
                    // Handle the case where the presentSimpleProgress value doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error case
            }
        });

        // Return 0 for now (the actual presentSimpleProgress value will be retrieved asynchronously)
        return null;
    }

    @Override
    public int getItemCount() {
        return tensesModels.size();
    }

//    public void updateProgress(int itemPosition, int progress) {
//        if (itemPosition >= 0 && itemPosition < tensesModels.size()) {
//            TensesModel tensesModel = tensesModels.get(itemPosition);
//            tensesModel.setProgress(progress);
//            notifyItemChanged(itemPosition);
//        }
//    }

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
//        int progress = getUserProgressForItem(position);
//
//        // Update the progress bar for the item
//        holder.progressBar.setProgress(progress);
        holder.ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserData(position, holder);
                Intent intent = new Intent(context, QuizActivityTenses.class);
                intent.putExtra("selectedTopic", tensesModels.get(position).getTenseName());
                context.startActivity(intent);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserData(position, holder);
                Intent intent = new Intent(context, LearnActivity.class);
                intent.putExtra("selectedTopic", tensesModels.get(position).getTenseName());
                context.startActivity(intent);
            }
        });
    }

//    private int getUserProgressForItem(int position) {
//        TensesModel tensesModel = tensesModels.get(position);
//
//        // Assuming you have a database or data source where you can retrieve the progress value for the specific item
//        // Retrieve the progress value for the item based on its ID or any other identifier
//        String itemId = tensesModel.getItemId(); // Replace with the appropriate method to retrieve the item ID
//        int progress = 0; // Default progress value
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        UserData userData = getUserData(position, userId);
//        // Retrieve the progress value from the database or data source based on the item ID
//        // Implement your logic here to fetch the progress value for the item
//
//        return progress;
//    }




    private UserData getUserData(int position, MyViewHolder holder) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("tenses").child("presentSimpleProgress");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int presentSimpleProgress = snapshot.getValue(Integer.class);
                    updateProgressBar(position, presentSimpleProgress, holder);
                } else {
                    // Handle the case where the presentSimpleProgress value doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error case
            }
        });
        return null;
    }

    private void updateProgressBar(int position, int progress, MyViewHolder holder) {
        tensesModels.get(position).setProgress(progress);
        holder.progressBar.setProgress(progress);
    }

    @Override
    public int getItemCount() {
        return tensesModels.size();
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
