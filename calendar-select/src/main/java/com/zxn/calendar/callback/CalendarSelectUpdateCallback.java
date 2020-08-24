package com.zxn.calendar.callback;

/**
 * calendar-select
 * Updated by zxn on 2020/8/24.
 * Created by richzjc on 18/3/15.
 */
public interface CalendarSelectUpdateCallback {

    void refreshLocate(int position);

    /**
     * 勾选多天时候的错误回调.
     *
     * @param days    开始日期结束日期相差的天数.
     * @param maxDays 11
     */
    void onMultSelectedError(int days, int maxDays);

    /**
     * 单选时间选择回调
     */
    void onSingleDateSelected();
}
