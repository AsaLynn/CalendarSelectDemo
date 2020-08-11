package com.zxn.calendar;

public interface SelectDateCallback {
    /**
     * 单选日期回调.
     *
     * @param timeEntity
     */
    void selectSingleDate(DayTimeEntity timeEntity);

    void selectMultDate(DayTimeEntity startTimeEntity, DayTimeEntity endTimeEntity);
}
