package com.example.betterlife.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterlife.Model.Articles;
import com.example.betterlife.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    Context context;
    List<Articles>  articles;
    CardView cardView;

    public NewsAdapter(Context context, List<Articles> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        Articles a = articles.get(position);
        holder.headLine.setText(a.getTitle());
        holder.publisher.setText(a.getSource().getName());
        holder.date.setText((a.getPublishedAt()).substring(0,10));

        String url = a.getUrl();
        String imageUrl = a.getUrlToImage();
        try {
            Picasso.get().load(imageUrl).into(holder.newsImage);
        }
        catch (Exception e){

        }
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("url",url);
                context.startActivity(intent);
                */
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(context, Uri.parse(url));
            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView headLine, publisher, date;
        ImageView newsImage;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            headLine = itemView.findViewById(R.id.headLineTextView);
            publisher = itemView.findViewById(R.id.publisherTextView);
            date = itemView.findViewById(R.id.dateTextView);
            cardView = itemView.findViewById(R.id.newsCardView);
            newsImage = itemView.findViewById(R.id.newsImageView);
            linearLayout = itemView.findViewById(R.id.newsLinearlayout);
        }
    }
}
