package com.rich.calendar;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;

import com.zxn.calendar.CalendarSelectView;
import com.zxn.calendar.entity.DayTimeEntity;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarSelectAcitivity extends AppCompatActivity {


    @BindView(R.id.calendar_select)
    CalendarSelectView calendarSelect;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String flag = getIntent().getStringExtra("flag");
        if (TextUtils.equals(flag, "single")) {
            setContentView(R.layout.select_date_single);
        } else if (TextUtils.equals(flag, "mult")) {
            setContentView(R.layout.select_date_mult);
        } else {
            setContentView(R.layout.select_date_mult2);
        }
        ButterKnife.bind(this);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.YEAR, -1);
        startCalendar.add(Calendar.MONTH, -1);
        //startCalendar.set(Calendar.DATE, 1);

        Calendar endCalendar= Calendar.getInstance();
//        endCalendar.set(Calendar.DATE, 1);
        //endCalendar.add(Calendar.MONTH, 1);
        //endCalendar.add(Calendar.DATE, -1);


        DayTimeEntity startDayTime = new DayTimeEntity(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), 0, 0, 0);
        DayTimeEntity endDayTime
                = new DayTimeEntity(endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH),
                0,
                0);
        calendarSelect.setCalendarRange(startCalendar,endCalendar,startDayTime,endDayTime);


//        Calendar startCalendar = Calendar.getInstance();
//        startCalendar.add(Calendar.MONTH, -1);
//        startCalendar.set(Calendar.DATE, 1);
//        calendarSelect.setCalendarRange(startCalendar);

        //SelectMultAcitivity

    }
}
