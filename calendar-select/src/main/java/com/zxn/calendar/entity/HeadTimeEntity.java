package com.zxn.calendar.entity;

/**
 * Updated by zxn on 2020/8/20.
 * Created by richzjc on 18/3/13.
 */
public class HeadTimeEntity {
    public int year;
    public int month;

    public HeadTimeEntity(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public HeadTimeEntity(int year) {
        this.year = year;
    }
}
