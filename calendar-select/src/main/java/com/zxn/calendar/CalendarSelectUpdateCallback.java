package com.zxn.calendar;

/**
 * calendar-select
 * Created by richzjc on 18/3/15.
 */

public interface CalendarSelectUpdateCallback {

    @Deprecated
    void updateMultView();

    void refreshLocate(int position);

    /**
     * 勾选多天时候的错误回调.
     * @param days 开始日期结束日期相差的天数.
     * @param maxDays 11
     */
    void onMultSelectedError(int days, int maxDays);
}
