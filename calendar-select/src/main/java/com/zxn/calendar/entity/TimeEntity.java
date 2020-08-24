package com.zxn.calendar.entity;

/**
 * TimeEntity,时间实体类
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class TimeEntity {

    /**
     * 自然周的第7天日期
     */
    public int endDay;

    /**
     * 自然周的第7天月份,实际月份要+1
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
     * 自然周的第1天月份,实际月份要+1
     */
    public int startMonth;

    /**
     * 自然周的第1天年份
     */
    public int startYear;

    /**
     * 时间的次序编号(一,二,三,四,五,六)
     */
    public int order;

    public TimeEntity() {
    }

}
