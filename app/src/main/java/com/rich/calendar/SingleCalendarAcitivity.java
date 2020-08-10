package com.rich.calendar;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zxn.calendar.CalendarSelectView;
import com.zxn.calendar.ConfirmSelectDateCallback;
import com.zxn.calendar.DayTimeEntity;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleCalendarAcitivity extends AppCompatActivity {


    @BindView(R.id.calendar_select)
    CalendarSelectView calendarSelect;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String flag = getIntent().getStringExtra("flag");
        if (TextUtils.equals(flag, "single")) {
            setContentView(R.layout.select_date_single);
        }
        ButterKnife.bind(this);

//        Calendar startCalendar = Calendar.getInstance();
//        startCalendar.add(Calendar.MONTH, -1);
//        startCalendar.set(Calendar.DATE, 1);
//
//        Calendar endCalendar= Calendar.getInstance();
//        endCalendar.set(Calendar.DATE, 1);
//        endCalendar.add(Calendar.MONTH, 1);
//        endCalendar.add(Calendar.DATE, -1);
//
//
//        DayTimeEntity startDayTime = new DayTimeEntity(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), 0, 0, 0);
//        DayTimeEntity endDayTime = new DayTimeEntity(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), 0, 0, 0);
//        calendarSelect.setCalendarRange(startCalendar,endCalendar,startDayTime,endDayTime);


        //Calendar startCalendar = Calendar.getInstance();
        //从本年上个月日历开始.
        //startCalendar.add(Calendar.MONTH, -1);
        //设置可以选择的起止日期.
        Calendar startCalendar = CalendarSelectView.getCalendar(2019, 3, 29);
        calendarSelect.setCalendarRange(startCalendar);
        calendarSelect.setConfirmCallback(new ConfirmSelectDateCallback() {
            @Override
            public void selectSingleDate(DayTimeEntity timeEntity) {
                Toast.makeText(SingleCalendarAcitivity.this, "timeEntity" + timeEntity.day, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void selectMultDate(DayTimeEntity startTimeEntity, DayTimeEntity endTimeEntity) {

            }
        });

    }
}
