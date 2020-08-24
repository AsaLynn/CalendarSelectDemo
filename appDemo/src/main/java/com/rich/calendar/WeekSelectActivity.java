package com.rich.calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import com.zxn.calendar.TimeSelectView;
import com.zxn.presenter.view.BaseActivity;
import com.zxn.time.SDFPattern;
import com.zxn.time.TimeUtils;
import com.zxn.titleview.TitleView;
import com.zxn.utils.UIUtils;

import butterknife.BindView;

/**
 * Created by zxn on 2020-8-18 11:28:37.
 */
public class WeekSelectActivity extends BaseActivity {

    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.wsv)
    TimeSelectView wsv;
    @BindView(R.id.title_view)
    TitleView titleView;


    private int mParam1;


    public static void jumpTo(Context context, int param1) {
        Intent intent = new Intent(context, WeekSelectActivity.class);
        intent.putExtra(ARG_PARAM1, param1);
        context.startActivity(intent);
    }

    public static void jumpTo(Context context) {
        Intent intent = new Intent(context, WeekSelectActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_week_select;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParam1 = getIntent().getIntExtra(ARG_PARAM1, 1);
        onInitTitle();
        wsv.timeClickListener(timeEntity -> {
            /*String startTime = timeEntity.startYear + "-" + timeEntity.startMonth + "-" + timeEntity.startDay;
            String endTime = timeEntity.endYear + "-" + timeEntity.endMonth + "-" + timeEntity.endDay;*/
            String startTime = TimeUtils.getTimeByCalendar(timeEntity.startYear,timeEntity.startMonth,timeEntity.startDay, SDFPattern.yyyyMMdd_SDF_RR);
            String endTime = TimeUtils.getTimeByCalendar(timeEntity.endYear,timeEntity.endMonth,timeEntity.endDay, SDFPattern.yyyyMMdd_SDF_RR);
            Toast.makeText(mContext, startTime + "至" + endTime, Toast.LENGTH_SHORT).show();
        });
    }

    private void onInitTitle() {
        UIUtils.init(getApplicationContext());
        TextView view = new TextView(mContext);
        view.setText("周");
        view.setTextColor(UIUtils.getColor(R.color.c_ffffff));
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        view.setOnClickListener(v -> {
            TextView rightView = (TextView) v;
            if (wsv.getTimeType() == TimeSelectView.MONTH) {
                rightView.setText("周");
                wsv.setMaxSections(12);
                wsv.setTimeType(TimeSelectView.WEEK);
            } else {
                rightView.setText("月");
                wsv.setMaxSections(3);
                wsv.setTimeType(TimeSelectView.MONTH);
            }
        });
        titleView.addRightView(view);
    }

}
