package com.rich.calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 周选择示例,WeekSelectActivity
 */
@Deprecated
public class WeekSelectDemoActivity extends AppCompatActivity {

    private TextView tvDate;

    public static void jumpTo(Context context) {
        Intent intent = new Intent(context, WeekSelectDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        tvDate = (TextView) findViewById(R.id.textView2);
        tvDate.setOnClickListener(v -> {
            WeekSelectActivity.jumpTo(this);
        });
    }

}
