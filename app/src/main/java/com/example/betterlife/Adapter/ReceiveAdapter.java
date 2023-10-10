package com.example.betterlife.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.betterlife.Model.Receive;
import com.example.betterlife.Model.User;
import com.example.betterlife.R;
import com.example.betterlife.RequestActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReceiveAdapter extends RecyclerView.Adapter<ReceiveAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Receive> receiveList;
    private List<Receive> backup;
    private String username, profileImageUri;

    public ReceiveAdapter(Context context, List<Receive> receiveList) {
        this.context = context;
        this.receiveList = receiveList;
        backup = new ArrayList<>(receiveList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.receive_item, parent, false);
        return new ReceiveAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Receive receive = receiveList.get(position);
        holder.medicineName.setText("Medicine: "+receive.getMedicineName());
        holder.quantity.setText("Quantity: "+receive.getQuantity());
        String userId = receive.getDonor();
        String historyId = receive.getHistoryId();
        FirebaseDatabase.getInstance().getReference("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username = user.getName();
                if(username.equals("")){
                    holder.donorName.setText("Anonymous");
                }else{
                    holder.donorName.setText(username);
                }
                profileImageUri = user.getProfileImageUrl();
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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RequestActivity.class);
                intent.putExtra("id", receive.getId());
                intent.putExtra("donorId", userId);
                intent.putExtra("historyId", historyId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return receiveList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Receive> filteredData = new ArrayList<>();

            if (constraint.toString().isEmpty() || constraint == null) {
                filteredData.addAll(backup);
            }
            else {
                for (Receive obj: backup) {
                    if(obj.getMedicineName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredData.add(obj);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredData;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            receiveList.clear();
            receiveList.addAll((ArrayList<Receive>)results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView donorName, medicineName, quantity;
        private CircleImageView profileImage;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            donorName = itemView.findViewById(R.id.receiveNameTextView);
            medicineName = itemView.findViewById(R.id.receiveMedicineTextView);
            quantity = itemView.findViewById(R.id.quantityTextView);
            profileImage = itemView.findViewById(R.id.receiveimageView);
            cardView = itemView.findViewById(R.id.receiveCardView);
        }
    }
}
