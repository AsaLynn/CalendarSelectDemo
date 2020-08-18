package com.rich.calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zxn.calendar.weekchoose.DateUtil;

import java.text.ParseException;

/**
 * 周选择示例,WeekSelectActivity
 */
public class WeekSelectDemoActivity extends AppCompatActivity {
    public static String selDate;
    private int nowY;
    private int nowM;
    private int nowD;
    private int startY;
    private int startM;
    private int startD;
    private int endY;
    private int endM;
    private int endD;
    private TextView tvDate;
//    private SelectWeekPopupWindow leftPopupWindow;
//    SelectWeekPopupWindow.OnSelectItemClickListencer onLeftSelectItemClick = new SelectWeekPopupWindow.OnSelectItemClickListencer() {
//        @Override
//        public void onSelectItemClick(View view, int position) {
//            startY = leftPopupWindow.yearData;
//            startM = leftPopupWindow.monthData;
//            startD = leftPopupWindow.dayData;
//            setDateRange();
//        }
//    };

    public static void jumpTo(Context context) {
        Intent intent = new Intent(context, WeekSelectDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        tvDate = (TextView) findViewById(R.id.textView2);
        setDateRange();
        tvDate.setOnClickListener(v -> {
            //showDataView();
            WeekSelectActivity.jumpTo(this);
        });

    }

    public void showDataView() {
        initPopupWindow();
        /*if (startY == 0) {
            leftPopupWindow.show(nowY, nowM, nowD, tvDate, new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    leftPopupWindow.diss();
                }
            });
        } else {
            leftPopupWindow.show(startY, startM, startD, tvDate, new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    leftPopupWindow.diss();
                }
            });
        }*/
    }

    private void initPopupWindow() {
        /*if (leftPopupWindow == null) {
            leftPopupWindow = new SelectWeekPopupWindow(this, onLeftSelectItemClick, nowY, nowM, nowD);
            leftPopupWindow.setZXStyle(false);
        }*/
    }

    private void setDateRange() {
        nowY = DateUtil.getYear();
        nowM = DateUtil.getMonth();
        nowD = DateUtil.getDay();
        int[] is;
        try {
            if (startY == 0) is = DateUtil.getWeekDay(nowY, nowM, nowD);
            else
                is = DateUtil.getWeekDay(startY, startM, startD);
            endY = is[3];
            endM = is[4];
            endD = is[5];
            startY = is[0];
            startM = is[1];
            startD = is[2];

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (startY == endY)
            tvDate.setText(startY + "年" + startM + "月" + startD + "-" + endM + "月" + endD + "日");
        else
            tvDate.setText(startY + "年" + startM + "月" + startD + "-" + endY + "年" + endM + "月" + endD + "日");
        selDate = DateUtil.getYYYY_MM_DD(startY, startM, startD);
    }

}
