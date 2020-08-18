package com.zxn.calendar.weekchoose;

import com.chad.library.adapter.base.entity.JSectionEntity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * <p>
 * java请自定义类，继承于JSectionEntity抽象类。封装一遍自己的数据类
 * <p>
 * kotlin，数据类请直接实现SectionEntity接口即可，无需封装。
 */
public class TimeSectionEntity extends JSectionEntity {
    private boolean isMonth;
    private Object object;

    public TimeSectionEntity(boolean isMonth, Object object) {
        this.isMonth = isMonth;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public boolean isHeader() {
        return isMonth;
    }

}
