# Calendar_Select

日历选择这个库支持单选日期以及选择时间段，可以设置时间段的天数,
并且可以设置第一次进入 是选中第一天，还是当天， 还是最后一天。
废话少说了，直接上效果图

# 效果图

[图片上传失败...(image-3e98db-1558712065753)]

[图片上传失败...(image-68421f-1558712065753)]
#AS下载
```
implementation 'com.zxn.calendar:calendar-select:1.0.4'
```
# 使用

**示例**
- 单选日历:
xml布局:
```
<?xml version="1.0" encoding="utf-8"?>
<com.zxn.calendar.CalendarSelectView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/calendar_select"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:clickable="true"
    app:locate_position="today"
    app:select_type="single"
    app:weekend_color="@color/colorAccent" />
```
代码中初始化:
```
//Calendar startCalendar = Calendar.getInstance();
//从本年上个月日历开始.
//startCalendar.add(Calendar.MONTH, -1);
//设置可以选择的起止日期.
Calendar startCalendar = CalendarSelectView.getCalendar(2019, 3, 29);
calendarSelect.setCalendarRange(startCalendar);
```
- 多选日历:
布局
```
<com.zxn.calendar.CalendarSelectView
    android:id="@+id/calendar_select"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_below="@id/tv_select_result"
    android:clickable="true"
    app:interval_select_color="@color/c_ff9800"
    app:locate_position="today"
    app:max_select_days="7"
    app:select_bg="@drawable/bg_sp_circle_c_fa753a_select"
    app:select_type="mult" />
```    
select_type 属性有如下：
* mult（选择时间段）
* single （单选日期）

locate_position属性如下：
* start (初始选中开始)
* today（初始选中当天，如果当天不在范围之内，则选中最后一天）
* end （初始选中最后一天）

- max_select_days属性:设置时间段的最大天数间隔.
- select_bg属性:设置选中日期的背景
- interval_select_color属性:设置选中日期中间日期的背景颜色
- weekend_color属性:设置周末日期的背景颜色

代码初始化:
```
Calendar startCalendar = CalendarSelectView.getCalendar(2016, 6, 1);

Calendar endCalendar = Calendar.getInstance();

DayTimeEntity startDayTime
                = new DayTimeEntity(startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                0,
                0,
                0);

DayTimeEntity endDayTime
                = new DayTimeEntity(endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH),//endCalendar.get(Calendar.DAY_OF_MONTH),
                0,
                0);
calendarSelect.setCalendarRange(startCalendar, endCalendar, startDayTime, endDayTime);
calendarSelect.setMultSelectedErrorCallback(new CalendarSelectView.MultSelectedErrorCallback() {
    @Override
    public void onMultSelectedError(int days, int maxDays) {
        Toast.makeText(MultCalendarAcitivity.this, "最多查询跨度" + (maxDays + 1) + "天内的交易", Toast.LENGTH_SHORT).show();
    }
});
```
获取选择后的日期结果:
```
DayTimeEntity startDayTime = calendarSelect.getStartDayTime();
DayTimeEntity endDayTime = calendarSelect.getEndDayTime();
tvSelectStart.setText(startDayTime.year + "-" + (startDayTime.month + 1) + "-" + startDayTime.day);
tvSelectEnd.setText(endDayTime.year + "-" + (endDayTime.month + 1) + "-" + endDayTime.day);
```

该控件默认的时间区间是： 
向前一年，向后三个月， 
如果需要更改控件的时间区间，请调用如下方法：
```
/**
 * 更改控件的时间区间
 *
 * @param startCalendar 该控件展示的起始月份
 * @param endCalendar   该控件展示的结束月份
 * @param startDayTime  控制能点击的日期，只有在startDayTime 和 endDayTime之前的日期是可点击的
 * @param endDayTime    控制能点击的日期，只有在startDayTime 和 endDayTime之前的日期是可点击的
 */
public void setCalendarRange(Calendar startCalendar, Calendar endCalendar, DayTimeEntity startDayTime, DayTimeEntity endDayTime) {
    if (startCalendar == null || endCalendar == null)
        throw new IllegalStateException("传入的日历是不能为空的");
    else if (endCalendar.getTimeInMillis() < startCalendar.getTimeInMillis())
        throw new IllegalStateException("结束日期不能早于开始日期");
    else {
        this.startCalendarDate.setTimeInMillis(startCalendar.getTimeInMillis());
        this.endCalendarDate.setTimeInMillis(endCalendar.getTimeInMillis());
        initStartEndCalendar();
        setStartEndTime(startDayTime, endDayTime);
        if (outAdapter != null)
            outAdapter.setData(Util.getTotalCount(startCalendar, endCalendar));
    }
} 
```