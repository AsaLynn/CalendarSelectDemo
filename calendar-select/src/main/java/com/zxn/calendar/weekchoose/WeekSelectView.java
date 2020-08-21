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

    //shiji
    public int dayData;
    public int yearData;
    public int monthData;
    private int year;
    private int month;
    private int days;
    private int syear;
    private int smonth;
    private int sdays;
    private int sdaysLiang;
    private int xyear;
    private int xmonth;
    private int xdays;
    private int xdaysLiang;
    //xianshi
    private int day;
    private int daysLiang;
    private int firstDay;
    private ListView listView;
    private RecyclerView mRecyclerView;
    private List<int[]> list = new ArrayList<>();
    private OnSelectItemClickListencer onSelectItemClickListencer;
    private int selectPosition = -1;
    private TextView title;
    private boolean hasHead;
    private boolean hasFoot;
    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_select_week, null);

                viewHolder.mImageViewBg = (ImageView) view.findViewById(R.id.bg);
                viewHolder.ts[0] = (TextView) view.findViewById(R.id.t1);
                viewHolder.ts[1] = (TextView) view.findViewById(R.id.t2);
                viewHolder.ts[2] = (TextView) view.findViewById(R.id.t3);
                viewHolder.ts[3] = (TextView) view.findViewById(R.id.t4);
                viewHolder.ts[4] = (TextView) view.findViewById(R.id.t5);
                viewHolder.ts[5] = (TextView) view.findViewById(R.id.t6);
                viewHolder.ts[6] = (TextView) view.findViewById(R.id.t7);


                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //selectPosition = i;
                    dayData = list.get(i)[0];
                    monthData = month;
                    yearData = year;
                    if (i == 0 && hasHead) {
                        if (monthData == 1) {
                            monthData = 12;
                            yearData--;

                        } else {
                            monthData--;
                        }

                    }
                    if (i == list.size() - 1 && hasFoot) {
                        if (monthData == 12) {
                            // monthData = 1;
                            // yearData++;
                        } else {
                            // monthData++;
                        }

                    }
                    if (null != onSelectItemClickListencer) {
                        onSelectItemClickListencer.onSelectItemClick(view, i);
                    }
                }
            });

            for (int j = 0; j < 7; j++) {
                viewHolder.ts[j].setAlpha(1f);
                viewHolder.ts[j].setText(list.get(i)[j] + "");

            }
            if (i == 0) {
                for (int j = 0; j < 7; j++) {
                    if (list.get(i)[j] > 10) {
                        hasHead = true;
                        viewHolder.ts[j].setAlpha(0.5f);
                    }

                }
            }
            if (i == list.size() - 1) {
                for (int j = 0; j < 7; j++) {
                    if (list.get(i)[j] < 10) {
                        hasFoot = true;
                        viewHolder.ts[j].setAlpha(0.5f);
                    }

                }
            }

            if (i == selectPosition) {
                viewHolder.mImageViewBg.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mImageViewBg.setVisibility(View.INVISIBLE);
            }
            return view;
        }
    };
    private WeekSelectAdapter mWeekSelectAdapter;
    private List<TimeSectionEntity> mWeekSelectList = new ArrayList<>();

    /**
     * 要展示的指定月的数量.
     */
    private int months = 0;//12


    public WeekSelectView(Context context) {
        this(context, null);
    }

    public WeekSelectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        onInitView();

        /*this.year = DateUtil.getYear();
        this.month = DateUtil.getMonth();
        this.day = DateUtil.getDay();
        this.yearData = year;
        this.monthData = month;
        this.dayData = day;*/

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
            int tYear = calendar.get(Calendar.YEAR);
            int tMonth = calendar.get(Calendar.MONTH) + 1;
            mWeekSelectList.add(new TimeSectionEntity(true, new MonthTimeEntity(tYear, tMonth)));

            //计算该月拥有的星期数.
            getWeekList(tYear,tMonth);

            //init();
        }
        mWeekSelectAdapter.setNewInstance(mWeekSelectList);
    }

    private void getWeekList(int year,int month) {

        int days = DateUtil.getMonthDays(year, month - 1);
        int firstDay = DateUtil.getFirstDayWeek17(year, month - 1);
        daysLiang = days;
        daysLiang--;

        //list.clear();

        int[] is1 = new int[7];
        for (int i = 0; i < (8 - firstDay); i++) {
            is1[firstDay - 1 + i] = days - daysLiang;
            daysLiang--;
            if (day == is1[firstDay - 1 + i]) {
                selectPosition = 0;
            }
        }
        //list.add(is1);
        TimeEntity entity1 = new TimeEntity(year, month);
        entity1.dayArray = is1;
        entity1.order = 1;
        mWeekSelectList.add(new TimeSectionEntity(false, entity1));

        int[] is2 = new int[7];
        for (int i = 0; i < 7; i++) {
            is2[i] = days - daysLiang;
            daysLiang--;
            if (day == is2[i]) {
                selectPosition = 1;
            }
        }
        //list.add(is2);
        TimeEntity entity2 = new TimeEntity(year, month);
        entity2.dayArray = is2;
        mWeekSelectList.add(new TimeSectionEntity(false, entity2));

        int[] is3 = new int[7];
        for (int i = 0; i < 7; i++) {
            is3[i] = days - daysLiang;
            daysLiang--;
            if (day == is3[i]) {
                selectPosition = 2;
            }
        }
        //list.add(is3);
        TimeEntity entity3 = new TimeEntity(year, month);
        entity3.dayArray = is3;
        mWeekSelectList.add(new TimeSectionEntity(false, entity3));

        int[] is4 = new int[7];
        for (int i = 0; i < 7; i++) {
            is4[i] = days - daysLiang;
            daysLiang--;
            if (day == is4[i]) {
                selectPosition = 3;
            }
            if (daysLiang < 0) {
                list.add(is4);
                return;
            }

        }
        //list.add(is4);
        TimeEntity entity4 = new TimeEntity(year, month);
        entity3.dayArray = is4;
        mWeekSelectList.add(new TimeSectionEntity(false, entity4));

        int[] is5 = new int[7];
        for (int i = 0; i < 7; i++) {
            is5[i] = days - daysLiang;
            daysLiang--;
            if (day == is5[i]) {
                selectPosition = 4;
            }
            if (daysLiang < 0) {
                list.add(is5);
                return;
            }
        }

        //list.add(is5);
        TimeEntity entity5 = new TimeEntity(year, month);
        entity3.dayArray = is5;
        mWeekSelectList.add(new TimeSectionEntity(false, entity5));
        if (daysLiang < 0)
            return;

        int[] is6 = new int[7];
        for (int i = 0; i < 7; i++) {
            is6[i] = days - daysLiang;
            daysLiang--;
            if (day == is6[i]) {
                selectPosition = 5;
            }
            if (daysLiang < 0) {
                list.add(is6);
                return;
            }
        }
        //list.add(is6);
        TimeEntity entity6 = new TimeEntity(year, month);
        entity3.dayArray = is6;
        mWeekSelectList.add(new TimeSectionEntity(false, entity6));
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

            }
        });
        mRecyclerView.setAdapter(mWeekSelectAdapter);
        listView = findViewById(R.id.list_view);
        title = findViewById(R.id.titile);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        findViewById(R.id.imgup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = syear;
                month = smonth;
                selectPosition = -1;
                if (year == yearData && month == monthData) {
                    day = dayData;
                } else {
                    day = -1;
                }
                init();
            }
        });
        findViewById(R.id.imgdown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = xyear;
                month = xmonth;
                selectPosition = -1;
                if (year == yearData && month == monthData) {
                    day = dayData;
                } else {
                    day = -1;
                }
                init();
            }
        });
    }

    private void setSX() {
        syear = year;
        xyear = year;
        smonth = month - 1;
        xmonth = month + 1;

        if (smonth == 0) {
            smonth = 12;
            syear--;
        }
        if (xmonth == 13) {
            xmonth = 1;
            xyear++;
        }
        xdays = DateUtil.getMonthDays(xyear, xmonth - 1);
        sdays = DateUtil.getMonthDays(syear, smonth - 1);
    }

    private void getSum() {
        setSX();
        days = DateUtil.getMonthDays(year, month - 1);
        firstDay = DateUtil.getFirstDayWeek17(year, month - 1);
        daysLiang = days;
        daysLiang--;

        list.clear();
        mWeekSelectList.clear();

        int[] is1 = new int[7];
        for (int i = 0; i < (8 - firstDay); i++) {
            is1[firstDay - 1 + i] = days - daysLiang;
            daysLiang--;
            if (day == is1[firstDay - 1 + i]) {
                selectPosition = 0;
            }
        }
        list.add(is1);
        //mWeekSelectList.add(new MySection());
        int[] is2 = new int[7];
        for (int i = 0; i < 7; i++) {
            is2[i] = days - daysLiang;
            daysLiang--;
            if (day == is2[i]) {
                selectPosition = 1;
            }
        }
        list.add(is2);

        int[] is3 = new int[7];
        for (int i = 0; i < 7; i++) {
            is3[i] = days - daysLiang;
            daysLiang--;
            if (day == is3[i]) {
                selectPosition = 2;
            }
        }
        list.add(is3);

        int[] is4 = new int[7];
        for (int i = 0; i < 7; i++) {
            is4[i] = days - daysLiang;
            daysLiang--;
            if (day == is4[i]) {
                selectPosition = 3;
            }
            if (daysLiang < 0) {
                list.add(is4);
                return;
            }

        }
        list.add(is4);


        int[] is5 = new int[7];
        for (int i = 0; i < 7; i++) {
            is5[i] = days - daysLiang;
            daysLiang--;
            if (day == is5[i]) {
                selectPosition = 4;
            }
            if (daysLiang < 0) {
                list.add(is5);
                return;
            }
        }

        list.add(is5);
        if (daysLiang < 0)
            return;

        int[] is6 = new int[7];
        for (int i = 0; i < 7; i++) {
            is6[i] = days - daysLiang;
            daysLiang--;
            if (day == is6[i]) {
                selectPosition = 5;
            }
            if (daysLiang < 0) {
                list.add(is6);
                return;
            }
        }
        list.add(is6);
    }

    private void getSumNextLast() {
        sdaysLiang = 0;
        for (int i = 0; i < 7; i++) {

            if (list.get(0)[i] == 0) {
                sdaysLiang++;
            }

        }
        for (int j = 0; j < sdaysLiang; j++) {

            list.get(0)[j] = sdays - sdaysLiang + 1 + j;

        }
        xdaysLiang = 0;
        for (int i = 0; i < 7; i++) {

            if (list.get(list.size() - 1)[i] == 0) {
                xdaysLiang++;
            }

        }

        int k = xdaysLiang;
        for (int j = 0; j < xdaysLiang; j++) {

            int p = 6 - j;

            list.get(list.size() - 1)[p] = k;
            k--;
        }

    }

    private void init() {
        getSum();
        getSumNextLast();
        hasHead = false;
        hasFoot = false;
        title.setText(month + "月");
    }


    public void show(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayData = day;
        this.yearData = year;
        this.monthData = month;
        init();
        listView.setSelection(selectPosition);
    }

    public interface OnSelectItemClickListencer {
        void onSelectItemClick(View view, int position);
    }

    class ViewHolder {
        ImageView mImageViewBg;
        TextView[] ts = new TextView[7];
    }

}


