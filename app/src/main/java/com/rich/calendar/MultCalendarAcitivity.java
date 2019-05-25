package com.rich.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zxn.calendar.CalendarSelectView;
import com.zxn.calendar.DayTimeEntity;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MultCalendarAcitivity extends AppCompatActivity {


    @BindView(R.id.calendar_select)
    CalendarSelectView calendarSelect;
    @BindView(R.id.tv_select_start)
    TextView tvSelectStart;
    @BindView(R.id.tv_select_end)
    TextView tvSelectEnd;
    @BindView(R.id.tv_select_result)
    TextView tvSelectResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_date_mult2);
        ButterKnife.bind(this);

        //Calendar startCalendar = Calendar.getInstance();
//        startCalendar.add(Calendar.YEAR, -1);
        //startCalendar.add(Calendar.MONTH, 0);
        //startCalendar.set(Calendar.DATE, 1);
        //startCalendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天

        Calendar startCalendar = CalendarSelectView.getCalendar(2016, 6, 1);

        Calendar endCalendar = Calendar.getInstance();
//        endCalendar.set(Calendar.DATE, 1);
//        endCalendar.add(Calendar.MONTH, 0);
        //endCalendar.add(Calendar.DATE, -1);


        DayTimeEntity startDayTime
                = new DayTimeEntity(startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                0,
                0,
                0);

        DayTimeEntity endDayTime
                = new DayTimeEntity(endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH),//endCalendar.get(Calendar.DAY_OF_MONTH),
                0,
                0);
        calendarSelect.setCalendarRange(startCalendar, endCalendar, startDayTime, endDayTime);
        calendarSelect.setMultSelectedErrorCallback(new CalendarSelectView.MultSelectedErrorCallback() {
            @Override
            public void onMultSelectedError(int days, int maxDays) {
                Toast.makeText(MultCalendarAcitivity.this, "最多查询跨度" + (maxDays + 1) + "天内的交易", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.tv_select_start, R.id.tv_select_end, R.id.tv_select_result})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_select_start:
                break;
            case R.id.tv_select_end:
                break;
            case R.id.tv_select_result:
                DayTimeEntity startDayTime = calendarSelect.getStartDayTime();
                DayTimeEntity endDayTime = calendarSelect.getEndDayTime();
                tvSelectStart.setText(startDayTime.year + "-" + (startDayTime.month + 1) + "-" + startDayTime.day);
                tvSelectEnd.setText(endDayTime.year + "-" + (endDayTime.month + 1) + "-" + endDayTime.day);
                break;
        }
    }
}
