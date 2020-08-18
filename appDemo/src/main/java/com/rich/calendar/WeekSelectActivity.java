package com.rich.calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zxn.presenter.view.BaseActivity;

/**
 * Created by zxn on 2020-8-18 11:28:37.
 */
public class WeekSelectActivity extends BaseActivity {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;


    public static void jumpTo(Context context, String param1) {
        Intent intent = new Intent(context, WeekSelectActivity.class);
        intent.putExtra(ARG_PARAM1, param1);
        context.startActivity(intent);
    }

    public static void jumpTo(Context context) {
        Intent intent = new Intent(context, WeekSelectActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_week_select;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParam1 = getIntent().getStringExtra(ARG_PARAM1);
    }

}
