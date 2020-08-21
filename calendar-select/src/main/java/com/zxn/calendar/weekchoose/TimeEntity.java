package com.zxn.calendar.weekchoose;

import android.text.TextUtils;

/**
 * TimeEntity,
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class TimeEntity {
    /**
     * 当前时间的年份
     */
    public int year;
    /**
     * 当前时间的月份
     */
    public int month;
    /**
     * 自然周的第1天日期
     */
    public String sundayDate;
    /**
     * 自然周的第7天日期
     */
    public String saturdayDate;

    public int[] dayArray;


    /**
     * 周的次序编号
     */
    public int order;

    /**
     *
     * @param year 年份
     * @param month 月份
     */
    public TimeEntity(int year, int month) {
        this.year = year;
        this.month = month;
    }

    /**
     * 本周起止日期mm.dd-mm.dd
     *
     * @return 本周起止日期
     */
    public String getStartStopDate() {
        String startTime = TextUtils.isEmpty(sundayDate)
                ? ""
                : sundayDate;
        String endTime = TextUtils.isEmpty(saturdayDate)
                ? ""
                : saturdayDate;
        return startTime + "-" + endTime;
    }
}
