package com.zxn.calendar.weekchoose;

import android.text.TextUtils;

/**
 * TimeEntity,周时间实体类
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class TimeEntity {

    /**
     * 自然周的第7天日期
     */
    public int endDay;

    /**
     * 自然周的第7天月份
     */
    public int endMonth;

    /**
     * 自然周的第7天年份
     */
    public int endYear;

    /**
     * 自然周的第1天日期
     */
    public int startDay;

    /**
     * 自然周的第1天月份
     */
    public int startMonth;

    /**
     * 自然周的第1天年份
     */
    public int startYear;

    /**
     * 当前时间的年份
     */
    public int year;
    /**
     * 当前时间的月份
     */
    public int month;
    /**
     * 自然周的第1天日期,周一作为第1天
     */
    public String startDate;
    /**
     * 自然周的第7天日期,周日作为第7天
     */
    public String endDate;

    public int[] dayArray;


    /**
     * 周的次序编号(一,二,三,四,五,六)
     */
    public int weekOrder;

    public TimeEntity() {
    }

    /**
     * @param year  年份
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
        String startTime = TextUtils.isEmpty(startDate)
                ? ""
                : startDate;
        String endTime = TextUtils.isEmpty(endDate)
                ? ""
                : endDate;
        return startTime + "-" + endTime;
    }
}
