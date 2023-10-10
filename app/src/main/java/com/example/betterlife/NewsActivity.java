package com.example.betterlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class NewsActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar pd;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        pd = findViewById(R.id.progressBar2);
        webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                pd.setVisibility(View.VISIBLE);
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pd.setVisibility(View.GONE);
            }
        });
        //webView.loadUrl("https://www.stanforddaily.com/2021/02/25/stanford-medicine-receives-80-million-donation-for-maternal-fetal-medicine/");
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        } else{
            super.onBackPressed();
        }
    }
}