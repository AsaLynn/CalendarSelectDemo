package com.zxn.calendar.weekchoose;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.zxn.calendar.MonthTimeEntity;
import com.zxn.calendar.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 参考了https://github.com/AndroidMsky/WeekChooseDailog
 * Updated by zxn on 2020/8/18.
 */
public class WeekSelectView extends LinearLayout {

    private RecyclerView mRecyclerView;
    private WeekSelectAdapter mWeekSelectAdapter;
    private List<TimeSectionEntity> mWeekSelectList = new ArrayList<>();

    /**
     * 要展示的指定月的数量.
     */
    private int months = 12;//12


    public WeekSelectView(Context context) {
        this(context, null);
    }

    public WeekSelectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 参与自然周计算的指定月份的天数
     */
    private int inMonthWeekDays;

    public WeekSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        onInitView();

        onInitWeekSectionList();

    }

    /**
     * 周日历仅保留最近一年(12月)的周数据.
     */
    private void onInitWeekSectionList() {
        mWeekSelectList.clear();
        for (int i = months; i >= 0; i--) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - i);
            calendar.set(Calendar.DATE, 1);
            int tYear = calendar.get(Calendar.YEAR);
            int tMonth = calendar.get(Calendar.MONTH);

            //添加月份信息
            mWeekSelectList.add(new TimeSectionEntity(true, new MonthTimeEntity(tYear, tMonth)));

            //计算该月拥有的星期数.
            //获取该月共计天数
            int days = DateUtil.getMonthDays(tYear, tMonth);
            //获取该月份第一天处于周几(周一到周日其中的一天)
            int firstDay = DateUtil.getFirstDayWeek17(tYear, tMonth);
            //若该月份第一天处于周一则属于该月份第一周,否则属于上个月的最后一周,
            //计算处于上个月的最后一周中的天数.
            int inLastWeekDays = (8 - firstDay) % 7;
            //将日历置于本月自然周第一天.
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + inLastWeekDays);
            //该月份中参与整周计算的天数.
            inMonthWeekDays = days - ((8 - firstDay) % 7);
            //计算该月份共有几周
            int weekCount = inMonthWeekDays <= 7 ? 1 : inMonthWeekDays / 7 + 1;
            for (int j = 1; j <= weekCount; j++) {
                TimeEntity entity = new TimeEntity();
                entity.weekOrder = j;
                //每周开始日期,
                entity.startYear = calendar.get(Calendar.YEAR);
                entity.startMonth = calendar.get(Calendar.MONTH);
                entity.startDay = calendar.get(Calendar.DATE);
                //每周结束日期
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 6);
                entity.endYear = calendar.get(Calendar.YEAR);
                entity.endMonth = calendar.get(Calendar.MONTH);
                entity.endDay = calendar.get(Calendar.DATE);
                mWeekSelectList.add(new TimeSectionEntity(false, entity));
                //将日期置于下周的第一天.
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            }
        }
        mWeekSelectAdapter.setNewInstance(mWeekSelectList);
        mRecyclerView.scrollToPosition(mWeekSelectAdapter.getItemCount() - 1);
    }

    private void onInitView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_select_week, this);
        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecyclerView.setHasFixedSize(true);
        mWeekSelectAdapter = new WeekSelectAdapter();
        mWeekSelectAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                // TODO: 2020/8/24 :增加周选择事件
            }
        });
        mRecyclerView.setAdapter(mWeekSelectAdapter);
    }

    public interface OnItemSelectListencer {
        void onSelectItemClick(View view, int position);
    }

}


