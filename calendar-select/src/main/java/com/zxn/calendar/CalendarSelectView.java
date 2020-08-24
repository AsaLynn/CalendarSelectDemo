package com.zxn.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxn.calendar.adapter.OuterRecycleAdapter;
import com.zxn.calendar.callback.CalendarSelectUpdateCallback;
import com.zxn.calendar.entity.DayTimeEntity;
import com.zxn.calendar.entity.HeadTimeEntity;
import com.zxn.calendar.utils.Util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.Map;

/**
 * Updated by zxn on 2020/8/24.
 * Created by richzjc on 18/3/13.
 */
public class CalendarSelectView extends LinearLayout {

    public static final int SINGLE = 1;
    public static final int MULT = 2;
    private final int NONE = -1;//不选中
    private final int START = 0;
    private final int TODAY = 1;
    private final int END = 2;
    Calendar startCalendar;
    Calendar endCalendar;
    Calendar startCalendarDate;
    Calendar endCalendarDate;
    DayTimeEntity startDayTime;
    DayTimeEntity endDayTime;
    GridLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private OuterRecycleAdapter outAdapter;
    private int selectType;
    private int locationType;
    private DateSelectListener mDateSelectListener;
    private MultSelectedErrorCallback mMultSelectedErrorCallback;
    private CalendarSelectUpdateCallback mCallback = new CalendarSelectUpdateCallback() {
        @Override
        public void refreshLocate(int position) {
            try {
                if (layoutManager != null) {
                    layoutManager.scrollToPositionWithOffset(position, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMultSelectedError(int days, int maxDays) {
            if (null != mMultSelectedErrorCallback) {
                mMultSelectedErrorCallback.onMultSelectedError(days, maxDays);
            }
        }

        @Override
        public void onSingleDateSelected() {
            if (null != mDateSelectListener) {
                mDateSelectListener.selectSingleDate(startDayTime);
            }
        }
    };
    private Drawable mSelectBgDrawable;
    private Drawable mIntervalSelectBgDrawable;
    private int mIntervalSelectBgColor;
    private int mMaxSelectDays;
    private Calendar mTodaycalendar;
    private int mWeekendColor;

    public CalendarSelectView(Context context) {
        this(context, null);
    }

    public CalendarSelectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCalendar();
        initView();
        initAttrs(attrs);
        initAdapter();
    }

    /**
     * 获取Calendar
     *
     * @param year  年份
     * @param month 月份,比实际月份小1
     * @param date  日期
     * @return 指定年月日的Calendar
     */
    public static Calendar getCalendar(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        return calendar;
    }

    public void setMultSelectedErrorCallback(MultSelectedErrorCallback callback) {
        this.mMultSelectedErrorCallback = callback;
    }

    private void initCalendar() {
        Calendar calendar = Calendar.getInstance();
        startDayTime = new DayTimeEntity(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 0, -1, -1);
        endDayTime = new DayTimeEntity(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 0, -1, -1);

        startCalendarDate = Calendar.getInstance();
        startCalendarDate.add(Calendar.YEAR, -1);
        startCalendarDate.set(Calendar.DATE, 1);
        endCalendarDate = Calendar.getInstance();
        endCalendarDate.set(Calendar.DATE, 1);
        endCalendarDate.add(Calendar.MONTH, 1);
        endCalendarDate.add(Calendar.DATE, -1);
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        initStartEndCalendar();
    }

    private void initStartEndCalendar() {
        startCalendar.setTimeInMillis(startCalendarDate.getTimeInMillis());
        endCalendar.setTimeInMillis(endCalendarDate.getTimeInMillis());

        startCalendarDate.set(Calendar.HOUR_OF_DAY, 0);
        endCalendarDate.set(Calendar.HOUR_OF_DAY, 0);
        startCalendarDate.set(Calendar.MINUTE, 0);
        endCalendarDate.set(Calendar.MINUTE, 0);
        startCalendarDate.set(Calendar.SECOND, 0);
        endCalendarDate.set(Calendar.SECOND, 0);
        startCalendarDate.set(Calendar.MILLISECOND, 0);
        endCalendarDate.set(Calendar.MILLISECOND, 0);
    }

    private void initView() {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.global_view_calendar_select, this, true);
        int color = ContextCompat.getColor(getContext(), R.color.day_mode_background_color);
        setBackgroundColor(color);
        recyclerView = findViewById(R.id.recycleView);

    }

    private void initAdapter() {
        layoutManager = new GridLayoutManager(getContext(),
                7,
                GridLayoutManager.VERTICAL,
                false
        );
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (outAdapter != null && outAdapter.getMap() != null) {
                    Map<Integer, HeadTimeEntity> map = outAdapter.getMap();
                    if (map.containsKey(position))
                        return 7;
                    else
                        return 1;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration());
        outAdapter = new OuterRecycleAdapter(Util.getTotalCount(startCalendar, endCalendar), selectType,
                startCalendarDate, endCalendarDate,
                startDayTime, endDayTime);
        outAdapter.setSelectBgDrawable(mSelectBgDrawable);
        outAdapter.setIntervalSelectBgDrawable(mIntervalSelectBgDrawable);
        outAdapter.setIntervalSelectBgColor(mIntervalSelectBgColor);
        outAdapter.setMaxSelectDays(mMaxSelectDays);
        outAdapter.setWeekendColor(mWeekendColor);

        outAdapter.setUpdateMultCallback(mCallback);
        recyclerView.setAdapter(outAdapter);
        outAdapter.scrollToPosition();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.calendarSelect);
            selectType = array.getInt(R.styleable.calendarSelect_select_type, SINGLE);
            locationType = array.getInt(R.styleable.calendarSelect_locate_position, START);
            updateDayTimeEntity();
            mSelectBgDrawable = array.getDrawable(R.styleable.calendarSelect_select_bg);
            mIntervalSelectBgDrawable = array.getDrawable(R.styleable.calendarSelect_interval_select_bg);
            mIntervalSelectBgColor = array.getColor(R.styleable.calendarSelect_interval_select_color, ContextCompat.getColor(getContext(), R.color.day_mode_backround_1a1482f0));
            mMaxSelectDays = array.getInt(R.styleable.calendarSelect_max_select_days, 0);
            mWeekendColor = array.getColor(R.styleable.calendarSelect_weekend_color, ContextCompat.getColor(getContext(), R.color.day_mode_text_color));
            array.recycle();
        }
    }

    private void updateDayTimeEntity() {
        if (locationType == TODAY) {//TODAY
            mTodaycalendar = Calendar.getInstance();
            mTodaycalendar.set(Calendar.HOUR_OF_DAY, 0);
            mTodaycalendar.set(Calendar.MINUTE, 0);
            mTodaycalendar.set(Calendar.SECOND, 0);
            mTodaycalendar.set(Calendar.MILLISECOND, 0);
            if (mTodaycalendar.getTimeInMillis() <= endCalendarDate.getTimeInMillis()) {
                endDayTime.year = mTodaycalendar.get(Calendar.YEAR);
                endDayTime.month = mTodaycalendar.get(Calendar.MONTH);
                endDayTime.day = mTodaycalendar.get(Calendar.DAY_OF_MONTH);
            } else {
                endDayTime.year = endCalendarDate.get(Calendar.YEAR);
                endDayTime.month = endCalendarDate.get(Calendar.MONTH);
                endDayTime.day = endCalendarDate.get(Calendar.DAY_OF_MONTH);
            }
        } else if (locationType == END) {
            endDayTime.year = endCalendarDate.get(Calendar.YEAR);
            endDayTime.month = endCalendarDate.get(Calendar.MONTH);
            endDayTime.day = endCalendarDate.get(Calendar.DAY_OF_MONTH);
        } else {
            endDayTime.year = startCalendarDate.get(Calendar.YEAR);
            endDayTime.month = startCalendarDate.get(Calendar.MONTH);
            endDayTime.day = startCalendarDate.get(Calendar.DAY_OF_MONTH);
        }

        if (selectType == SINGLE) {
            startDayTime.year = endDayTime.year;
            startDayTime.month = endDayTime.month;
            startDayTime.day = endDayTime.day;
        }
    }

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

    /**
     * 更改控件的时间区间
     *
     * @param startCalendar 该控件展示的起始月份
     */
    public void setCalendarRange(Calendar startCalendar) {
        if (startCalendar == null)
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

    public void setStartEndTime(DayTimeEntity startDayTime, DayTimeEntity endDayTime) {
        if (startDayTime == null || endDayTime == null) {
            updateDayTimeEntity();
        } else {
            if (startDayTime != null) {
                this.startDayTime.day = startDayTime.day;
                this.startDayTime.year = startDayTime.year;
                this.startDayTime.monthPosition = -1;
                this.startDayTime.month = startDayTime.month;
                this.startDayTime.listPosition = -1;
            }

            if (endDayTime != null) {
                this.endDayTime.day = endDayTime.day;
                this.endDayTime.year = endDayTime.year;
                this.endDayTime.monthPosition = -1;
                this.endDayTime.month = endDayTime.month;
                this.endDayTime.listPosition = -1;
            }
        }

        if (outAdapter != null) {
            outAdapter.notifyDataSetChanged();
            outAdapter.scrollToLocation();
        }
    }

    public void selectDateCallback(DateSelectListener listener) {
        this.mDateSelectListener = listener;
    }

    /**
     * 获取当前年份和月份,日期.
     *
     * @return 年份和月份, 日期.
     */
    public DayTimeEntity getTodyDayTime() {
        return new DayTimeEntity(mTodaycalendar.get(Calendar.YEAR), mTodaycalendar.get(Calendar.MONTH), mTodaycalendar.get(Calendar.DAY_OF_MONTH), 0, 0);
    }

    public DayTimeEntity getStartDayTime() {
        return startDayTime;
    }

    public DayTimeEntity getEndDayTime() {
        return endDayTime;
    }

    public void setSelectType(@SelectType int selectType) {
        this.selectType = selectType;
        outAdapter.setSelectType(selectType);
    }

    public interface MultSelectedErrorCallback {

        /**
         * 选择天数间隔错误的回调.
         *
         * @param days    开始时间间隔和结束时间的实际天数间隔.
         * @param maxDays 开始时间间隔和结束时间的最大天数间隔.
         */
        void onMultSelectedError(int days, int maxDays);
    }

    public interface DateSelectListener {
        /**
         * 单选日期回调.
         *
         * @param timeEntity
         */
        void selectSingleDate(DayTimeEntity timeEntity);

        //void selectMultDate(DayTimeEntity startTimeEntity, DayTimeEntity endTimeEntity);
    }

    @IntDef({SINGLE, MULT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SelectType {
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        public SpaceItemDecoration() {

        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = 10;
            int position = parent.getChildLayoutPosition(view);
            if (outAdapter != null && outAdapter.getMap() != null) {
                Map<Integer, HeadTimeEntity> map = outAdapter.getMap();
                if (map.containsKey(position))
                    outRect.top = 20;
                else
                    outRect.top = 0;
            } else {
                outRect.top = 0;
            }
        }
    }
}
