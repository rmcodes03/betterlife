package com.example.betterlife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowNoticeActivity extends AppCompatActivity {

    private TextView notice, writer, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notice);

        notice = findViewById(R.id.notice);
        writer = findViewById(R.id.writer);
        date = findViewById(R.id.date);

        Intent intent = getIntent();
        String writerText = intent.getStringExtra("writer");
        String noticeText = intent.getStringExtra("notice");
        String dateText = intent.getStringExtra("date");

        notice.setText(noticeText);
        writer.setText(writerText);
        date.setText(dateText);
    }
}