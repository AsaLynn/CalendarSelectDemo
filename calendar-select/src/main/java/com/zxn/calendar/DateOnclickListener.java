package com.zxn.calendar;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by richzjc on 18/3/14.
 */

public class DateOnclickListener implements View.OnClickListener {

    private static final String TAG = "DateOnclickListener";
    private int selectType;
    private DayTimeEntity startDayTime;
    private DayTimeEntity endDayTime;
    private OuterRecycleAdapter outAdapter;
    private DayTimeEntity timeEntity;
    private int mMaxSeparatedDays;

    public DateOnclickListener(int selectType, DayTimeEntity startDayTime, DayTimeEntity endDayTime, OuterRecycleAdapter adapter) {
        this.selectType = selectType;
        this.startDayTime = startDayTime;
        this.endDayTime = endDayTime;
        this.outAdapter = adapter;
        this.mMaxSeparatedDays = adapter.getMaxSelectDays();
    }

    @Override
    public void onClick(View v) {
        if (timeEntity == null)
            throw new IllegalStateException("数据源是不能为空的");
        else {
            if (selectType == CalendarSelectView.SINGLE) {
                responseSingle(timeEntity);
            } else if (selectType == CalendarSelectView.MULT) {
                responseMult(timeEntity);
            }
            if (outAdapter != null)
                outAdapter.notifyDataSetChanged();

            if (outAdapter.multCallback != null)
                outAdapter.multCallback.updateMultView();

        }
    }

    private void responseMult(DayTimeEntity timeEntity) {
        Calendar timeEntityCalendar = Calendar.getInstance();
        Calendar tempCalendar = Calendar.getInstance();
        timeEntityCalendar.set(Calendar.HOUR_OF_DAY, 0);
        tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
        timeEntityCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        timeEntityCalendar.set(Calendar.SECOND, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        timeEntityCalendar.set(Calendar.MILLISECOND, 0);
        tempCalendar.set(Calendar.MILLISECOND, 0);
        timeEntityCalendar.set(Calendar.YEAR, timeEntity.year);
        timeEntityCalendar.set(Calendar.MONTH, timeEntity.month);
        timeEntityCalendar.set(Calendar.DATE, timeEntity.day);
        if (endDayTime.day != 0 && startDayTime.day == 0) {
            responseEndNotZero(timeEntity, timeEntityCalendar, tempCalendar);//首次点击已经有一个选中后的点击.
        } else if (startDayTime.day == 0 && endDayTime.day == 0) {
            responseBothZero(timeEntity);   //
        } else if (startDayTime.day != 0 && endDayTime.day == 0) {
            responseStartNotZero(timeEntity, timeEntityCalendar, tempCalendar);//已经有一个选中后的点击.
        } else if (startDayTime.day != 0 && endDayTime.day != 0) {      //两个都选中后的点击.
            responseBothNotZero(timeEntity);
        }
    }

    private void responseBothNotZero(DayTimeEntity timeEntity) {
        startDayTime.year = timeEntity.year;
        startDayTime.month = timeEntity.month;
        startDayTime.day = timeEntity.day;
        startDayTime.monthPosition = timeEntity.monthPosition;
        startDayTime.listPosition = timeEntity.listPosition;
        endDayTime.day = 0;
        endDayTime.listPosition = -1;
    }

    private void responseStartNotZero(DayTimeEntity timeEntity, Calendar timeEntityCalendar, Calendar tempCalendar) {
        tempCalendar.set(Calendar.YEAR, startDayTime.year);
        tempCalendar.set(Calendar.MONTH, startDayTime.month);
        tempCalendar.set(Calendar.DATE, startDayTime.day);
        if (timeEntityCalendar.getTimeInMillis() > tempCalendar.getTimeInMillis()) {
            //选中的第二个日期大于了第一个选中的日期了-->

            //比较两个时间相差的天数.
            int separatedDays = Util.getSeparatedDays(tempCalendar.getTimeInMillis(), timeEntityCalendar.getTimeInMillis());
            Log.i(TAG, "responseStartNotZero:mMaxSeparatedDays---> " + mMaxSeparatedDays);
            Log.i(TAG, "responseStartNotZero:separatedDays---> " + separatedDays);
            if (mMaxSeparatedDays != 0 && separatedDays > mMaxSeparatedDays) {
                //选择的两个时间间隔大于指定最大间隔天数后,置零.
                outAdapter.multCallback.onMultSelectedError(separatedDays, mMaxSeparatedDays);
                return;
            }
            endDayTime.year = timeEntity.year;
            endDayTime.month = timeEntity.month;
            endDayTime.monthPosition = timeEntity.monthPosition;
            endDayTime.day = timeEntity.day;
            endDayTime.listPosition = timeEntity.listPosition;
        } else if (timeEntityCalendar.getTimeInMillis() == tempCalendar.getTimeInMillis()) {
            endDayTime.day = timeEntity.day;
            endDayTime.listPosition = timeEntity.listPosition;
            endDayTime.monthPosition = timeEntity.monthPosition;
            endDayTime.year = timeEntity.year;
            endDayTime.month = timeEntity.month;
            startDayTime.year = timeEntity.year;
            startDayTime.month = timeEntity.month;
            startDayTime.monthPosition = timeEntity.monthPosition;
            startDayTime.day = timeEntity.day;
            startDayTime.listPosition = timeEntity.listPosition;
        } else {
            startDayTime.year = timeEntity.year;
            startDayTime.month = timeEntity.month;
            startDayTime.day = timeEntity.day;
            startDayTime.monthPosition = timeEntity.monthPosition;
            startDayTime.listPosition = timeEntity.listPosition;
            endDayTime.day = 0;
            endDayTime.monthPosition = -1;
            endDayTime.listPosition = -1;
        }
    }

    private void responseBothZero(DayTimeEntity timeEntity) {
        startDayTime.year = timeEntity.year;
        startDayTime.month = timeEntity.month;
        startDayTime.day = timeEntity.day;
        startDayTime.monthPosition = timeEntity.monthPosition;
        startDayTime.listPosition = timeEntity.listPosition;
        endDayTime.day = 0;
        endDayTime.monthPosition = -1;
        endDayTime.listPosition = -1;
    }

    private void responseEndNotZero(DayTimeEntity timeEntity, Calendar timeEntityCalendar, Calendar tempCalendar) {
        tempCalendar.set(Calendar.YEAR, endDayTime.year);
        tempCalendar.set(Calendar.MONTH, endDayTime.month);
        tempCalendar.set(Calendar.DATE, endDayTime.day);
        if (timeEntityCalendar.getTimeInMillis() > tempCalendar.getTimeInMillis()) {
            startDayTime.year = timeEntity.year;
            startDayTime.month = timeEntity.month;
            startDayTime.monthPosition = timeEntity.monthPosition;
            startDayTime.day = timeEntity.day;
            startDayTime.listPosition = timeEntity.listPosition;
            endDayTime.monthPosition = -1;
            endDayTime.day = 0;
            endDayTime.listPosition = -1;
        } else if (timeEntityCalendar.getTimeInMillis() == tempCalendar.getTimeInMillis()) {
            startDayTime.day = timeEntity.day;
            startDayTime.month = timeEntity.month;
            startDayTime.year = timeEntity.year;
            startDayTime.monthPosition = timeEntity.monthPosition;
            startDayTime.listPosition = timeEntity.listPosition;
            endDayTime.year = timeEntity.year;
            endDayTime.month = timeEntity.month;
            endDayTime.monthPosition = timeEntity.monthPosition;
            endDayTime.day = timeEntity.day;
            endDayTime.listPosition = timeEntity.listPosition;
        } else {
            //比较两个时间相差的天数.
            int separatedDays = Util.getSeparatedDays(tempCalendar.getTimeInMillis(), timeEntityCalendar.getTimeInMillis());
            Log.i(TAG, "responseEndNotZero:mMaxSeparatedDays---> " + mMaxSeparatedDays);
            Log.i(TAG, "responseEndNotZero:separatedDays---> " + separatedDays);
            if (mMaxSeparatedDays != 0 && separatedDays > mMaxSeparatedDays) {
                //选择的两个时间间隔大于指定最大间隔天数后,置零.
                outAdapter.multCallback.onMultSelectedError(separatedDays, mMaxSeparatedDays);
                return;
            }
            startDayTime.year = timeEntity.year;
            startDayTime.month = timeEntity.month;
            startDayTime.monthPosition = timeEntity.monthPosition;
            startDayTime.day = timeEntity.day;
            startDayTime.listPosition = timeEntity.listPosition;
        }
    }

    private void responseSingle(DayTimeEntity timeEntity) {
        startDayTime.year = timeEntity.year;
        endDayTime.year = timeEntity.year;
        startDayTime.month = timeEntity.month;
        endDayTime.month = timeEntity.month;
        startDayTime.day = timeEntity.day;
        startDayTime.monthPosition = timeEntity.monthPosition;
        endDayTime.monthPosition = timeEntity.monthPosition;
        endDayTime.day = timeEntity.day;
        startDayTime.listPosition = timeEntity.listPosition;
        endDayTime.listPosition = timeEntity.listPosition;
    }

    public void setEntity(DayTimeEntity dayTimeEntity) {
        this.timeEntity = dayTimeEntity;
    }
}
