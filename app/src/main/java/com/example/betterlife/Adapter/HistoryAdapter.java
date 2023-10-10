package com.example.betterlife.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.betterlife.Model.History;
import com.example.betterlife.Model.User;
import com.example.betterlife.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<History> historyList;
    String dateText;

    public HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.medicineName.setText("Medicine: "+history.getMedicineName());
        holder.quantity.setText("Quantity: "+history.getQuantity());
        String time = history.getDate();
        dateText = time.substring(4,10)+","+time.substring(29);
        holder.date.setText(dateText);
        String userId = history.getDonor();
        FirebaseDatabase.getInstance().getReference("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String username = user.getName();
                if(username.equals("")){
                    holder.donorName.setText("Anonymous");
                }else{
                    holder.donorName.setText(username);
                }
                String profileImageUri = user.getProfileImageUrl();
                if(profileImageUri.equals("")){
                    Glide.with(context).load(R.drawable.user).into(holder.profileImage);
                }else{
                    Glide.with(context).load(profileImageUri).into(holder.profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView donorName, medicineName, quantity, date;
        private CircleImageView profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            donorName = itemView.findViewById(R.id.donorName);
            medicineName = itemView.findViewById(R.id.medicineName);
            quantity = itemView.findViewById(R.id.quantity);
            date = itemView.findViewById(R.id.dateTextView);
            profileImage = itemView.findViewById(R.id.profileImage1);
        }
    }
}
