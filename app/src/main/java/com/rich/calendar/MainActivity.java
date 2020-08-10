package com.rich.calendar;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.single)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SingleCalendarAcitivity.class);
                        intent.putExtra("flag", "single");
                        startActivity(intent);
                    }
                });

        findViewById(R.id.mult)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, CalendarSelectAcitivity.class);
                        intent.putExtra("flag", "mult");
                        startActivity(intent);
                    }
                });

        findViewById(R.id.tv_mult_calendar)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MultCalendarAcitivity.class);
                        intent.putExtra("flag", "mult1");
                        startActivity(intent);
                    }
                });
    }
}
