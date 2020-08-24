package com.zxn.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.zxn.calendar.entity.HeadTimeEntity;
import com.zxn.calendar.entity.TimeEntity;
import com.zxn.calendar.entity.TimeSectionEntity;
import com.zxn.calendar.adapter.TimeSectionAdapter;
import com.zxn.time.TimeUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 参考了https://github.com/AndroidMsky/WeekChooseDailog
 * Updated by zxn on 2020/8/18.
 */
public class TimeSelectView extends LinearLayout {

    public static final int WEEK = 1;
    public static final int MONTH = 2;
    private RecyclerView mRecyclerView;
    private TimeSectionAdapter mTimeSectionAdapter;
    private List<TimeSectionEntity> mTimeSectionList = new ArrayList<>();

    /**
     * 要展示的指定分组的数量.
     */
    private int maxSections = 12;

    private TimeClickListener mTimeClickListener;
    private int timeType = WEEK;

    public TimeSelectView(Context context) {
        this(context, null);
    }

    public TimeSelectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        onInitView();

        onInitAttrs(attrs);

        onInitTimeSectionList();

    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(@TimeType int timeType) {
        this.timeType = timeType;
        onInitTimeSectionList();
    }

    public void setMaxSections(int maxSections) {
        this.maxSections = maxSections;
    }

    public void timeClickListener(TimeClickListener listener) {
        this.mTimeClickListener = listener;
    }

    private void onInitAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TimeSelectView);
            maxSections = array.getInt(R.styleable.TimeSelectView_max_sections, 12);
            timeType = array.getInt(R.styleable.TimeSelectView_time_type, WEEK);
            array.recycle();
        }
    }


    private void onInitTimeSectionList() {
        mTimeSectionList.clear();

        for (int i = maxSections; i >= 0; i--) {
            Calendar calendar = Calendar.getInstance();
            if (timeType == MONTH) {
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - i);
                calendar.set(Calendar.MONTH, 0);
                //calendar.set(Calendar.DATE, 1);
                int tYear = calendar.get(Calendar.YEAR);
                //添加年份信息
                mTimeSectionList.add(new TimeSectionEntity(true, new HeadTimeEntity(tYear)));
                //一年12个月,添加该年中所有的月份
                for (int j = 1; j <= 12; j++) {
                    TimeEntity entity = new TimeEntity();
                    entity.order = j;

                    //将日期置于该月的第一天
                    calendar.set(Calendar.DATE, 1);
                    //每月开始日期,
                    entity.startYear = calendar.get(Calendar.YEAR);
                    entity.startMonth = calendar.get(Calendar.MONTH);
                    entity.startDay = calendar.get(Calendar.DATE);
                    //每月结束日期
                    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                    entity.endYear = calendar.get(Calendar.YEAR);
                    entity.endMonth = calendar.get(Calendar.MONTH);
                    entity.endDay = calendar.get(Calendar.DATE);
                    mTimeSectionList.add(new TimeSectionEntity(entity));

                    //指定下一个月份
                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
                }

            } else {
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - i);
                calendar.set(Calendar.DATE, 1);
                int tYear = calendar.get(Calendar.YEAR);
                int tMonth = calendar.get(Calendar.MONTH);

                //添加月份信息
                mTimeSectionList.add(new TimeSectionEntity(true, new HeadTimeEntity(tYear, tMonth)));

                //计算该月拥有的星期数.
                //获取该月共计天数
                int days = TimeUtils.getMonthDays(tYear, tMonth);
                //获取该月份第一天处于周几(周一到周日其中的一天)
                int firstDay = TimeUtils.getFirstDayWeek17(tYear, tMonth);
                //若该月份第一天处于周一则属于该月份第一周,否则属于上个月的最后一周,
                //计算处于上个月的最后一周中的天数.
                int inLastWeekDays = (8 - firstDay) % 7;
                //将日历置于本月自然周第一天.
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + inLastWeekDays);
                //该月份中参与整周计算的天数.(参与自然周计算的指定月份的天数)
                int inMonthWeekDays = days - ((8 - firstDay) % 7);
                //计算该月份共有几周
                int weekCount = inMonthWeekDays <= 7 ? 1 : inMonthWeekDays / 7 + 1;
                for (int j = 1; j <= weekCount; j++) {
                    TimeEntity entity = new TimeEntity();
                    entity.order = j;
                    //每周开始日期,
                    entity.startYear = calendar.get(Calendar.YEAR);
                    entity.startMonth = calendar.get(Calendar.MONTH);
                    entity.startDay = calendar.get(Calendar.DATE);
                    //每周结束日期
                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 6);
                    entity.endYear = calendar.get(Calendar.YEAR);
                    entity.endMonth = calendar.get(Calendar.MONTH);
                    entity.endDay = calendar.get(Calendar.DATE);
                    mTimeSectionList.add(new TimeSectionEntity(false, entity));
                    //将日期置于下周的第一天.
                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                }
            }
        }

        mTimeSectionAdapter.setTimeType(timeType);
        mTimeSectionAdapter.setNewInstance(mTimeSectionList);
        mRecyclerView.scrollToPosition(mTimeSectionAdapter.getItemCount() - 1);
    }

    private void onInitView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_select_week, this);
        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecyclerView.setHasFixedSize(true);
        mTimeSectionAdapter = new TimeSectionAdapter();
        mTimeSectionAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                TimeSectionEntity timeSectionEntity = (TimeSectionEntity) adapter.getData().get(position);
                if (!timeSectionEntity.isHeader() && timeSectionEntity.getObject() instanceof TimeEntity) {
                    TimeEntity o = (TimeEntity) timeSectionEntity.getObject();
                    boolean isPast = TimeUtils.isPast(o.startYear, o.startMonth, o.startDay);
                    if (isPast && null != mTimeClickListener) {
                        mTimeClickListener.timeClick(o);
                    }
                }
            }
        });
        mRecyclerView.setAdapter(mTimeSectionAdapter);
    }

    public interface TimeClickListener {
        void timeClick(TimeEntity weekTimeEntity);
    }

    @IntDef({WEEK, MONTH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TimeType {
    }

}


