package com.example.betterlife.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.betterlife.AddCommentActivity;
import com.example.betterlife.Model.Post;
import com.example.betterlife.Model.User;
import com.example.betterlife.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> mPosts;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;

    //FirebaseUser firebaseUser;

    public PostAdapter(Context context, List<Post> mPosts) {
        this.context = context;
        this.mPosts = mPosts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        database = FirebaseDatabase.getInstance();
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        String caption = post.getCaption().trim();
        String image = post.getImage();
        String time = post.getTime();
        String date = time.substring(4,10)+","+time.substring(29);

        holder.caption.setText(caption);
        Glide.with(context).load(image).into(holder.postImage);
        holder.dateText.setText(date);

        String userId = post.getUser();
        database.getReference("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String username = user.getName();
                if(username.equals("")){
                    holder.username.setText("Anonymous");
                }else{
                    holder.username.setText(username);
                }
                String url = user.getProfileImageUrl();
                if(url.equals("")) {
                    Glide.with(context).load(R.drawable.user).into(holder.profileImage);
                }
                else {
                    Glide.with(context).load(url).into(holder.profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        isLiked(post.getPostId(), holder.like);
        noOfLikes(post.getPostId(), holder.noOfLikes);
        noOfComments(post.getPostId(), holder.noOfComments);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like.getTag().equals("Like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId()).child(firebaseUser.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddCommentActivity.class);
                intent.putExtra("postId",post.getPostId());
                intent.putExtra("user",post.getUser());
                context.startActivity(intent);
            }
        });
    }

    private void isLiked(String postId, ImageView imageView){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.liked);
                    imageView.setTag("Liked");
                }
                else{
                    imageView.setImageResource(R.drawable.like);
                    imageView.setTag("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void noOfLikes(String postId, TextView text){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() == 1 || snapshot.getChildrenCount() == 0){
                    text.setText(snapshot.getChildrenCount() + " like");
                }
                else{
                    text.setText(snapshot.getChildrenCount() + " likes");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void noOfComments(String postId, TextView text){
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() == 1 || snapshot.getChildrenCount() == 0){
                    text.setText(snapshot.getChildrenCount() + " comment");
                }
                else{
                    text.setText(snapshot.getChildrenCount() + " comments");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

         private ImageView profileImage;
         private ImageView postImage;
         private ImageView like;
         private ImageView comment;
         private SocialTextView caption;
         private TextView username;
         private TextView noOfLikes;
         private TextView noOfComments;
         private TextView dateText;

         public ViewHolder(@NonNull View itemView) {
             super(itemView);

             profileImage = itemView.findViewById(R.id.postProfileImageView);
             postImage = itemView.findViewById(R.id.postsImageView);
             like = itemView.findViewById(R.id.likeImageView);
             comment = itemView.findViewById(R.id.commentImageView);
             caption = itemView.findViewById(R.id.captionTextView);
             username = itemView.findViewById(R.id.usernameTextView);
             noOfLikes = itemView.findViewById(R.id.noOfLikesTextView);
             noOfComments = itemView.findViewById(R.id.noOfCommentsTextView);
             dateText = itemView.findViewById(R.id.dateTextView);

         }
    }
}