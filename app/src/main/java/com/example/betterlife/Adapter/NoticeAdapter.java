package com.example.betterlife.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterlife.Model.Notice;
import com.example.betterlife.Model.User;
import com.example.betterlife.R;
import com.example.betterlife.ShowNoticeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private Context context;
    private List<Notice> noticeList;
    private FirebaseUser firebaseUser;
    private String username, noticeText, dateText;

    public NoticeAdapter(Context context, List<Notice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_item, parent, false);
        return new NoticeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Intent intent = new Intent(context, ShowNoticeActivity.class);;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Notice notice = noticeList.get(position);
        noticeText = notice.getNotice();
        holder.noticeTextView.setText(noticeText);
        String time = notice.getDate();
        dateText = time.substring(4,10)+","+time.substring(29);
        holder.dateTextView.setText(dateText);
        String userId = notice.getWriter();
        FirebaseDatabase.getInstance().getReference("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username = user.getName();
                if(username.equals("")){
                    holder.writerTextView.setText("Anonymous");
                    intent.putExtra("writer", username);
                }else{
                    holder.writerTextView.setText(username);
                    intent.putExtra("writer", username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("notice",notice.getNotice());
                intent.putExtra("date", notice.getDate());
                context.startActivity(intent);
            }
        });

        holder.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, notice.getNotice() + " -(By: " + username + " Date: " + notice.getDate() + ")");
                context.startActivity(Intent.createChooser(intent, "Share via"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private TextView noticeTextView, writerTextView, dateTextView;
        private ImageView shareIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.noticeCardView);
            noticeTextView = itemView.findViewById(R.id.noticeTextView);
            writerTextView = itemView.findViewById(R.id.writerTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            shareIcon = itemView.findViewById(R.id.shareImageView);
        }
    }
}
