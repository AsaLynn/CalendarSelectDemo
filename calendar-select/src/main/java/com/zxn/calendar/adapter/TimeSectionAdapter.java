package com.zxn.calendar.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zxn.calendar.TimeSelectView;
import com.zxn.calendar.entity.HeadTimeEntity;
import com.zxn.calendar.R;
import com.zxn.calendar.entity.TimeEntity;
import com.zxn.calendar.entity.TimeSectionEntity;
import com.zxn.time.SDFPattern;
import com.zxn.time.TimeUtils;

import org.jetbrains.annotations.NotNull;

/**
 * TimeSectionAdapter
 * 参考:https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * Updated by zxn on 2020/8/18.
 */
public class TimeSectionAdapter extends BaseSectionQuickAdapter<TimeSectionEntity, BaseViewHolder> {

    private int timeType;

    public TimeSectionAdapter() {
        super(R.layout.item_section_head);
        setNormalLayout(R.layout.item_section_content);
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder helper, @NotNull TimeSectionEntity item) {
        if (item.getObject() instanceof HeadTimeEntity) {
            HeadTimeEntity entity = (HeadTimeEntity) item.getObject();
            if (timeType == TimeSelectView.MONTH) {
                String time = TimeUtils.getTimeByCalendar(entity.year, 0, 1, SDFPattern.yyyy_SDF_Y);
                helper.setText(R.id.tv_header, time);
            } else {
                String time = TimeUtils.getTimeByCalendar(entity.year, entity.month, 1, SDFPattern.yyyyM_SDF_YM);
                helper.setText(R.id.tv_header, time);
            }
        }
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull TimeSectionEntity item) {
        TimeEntity timeEntity = (TimeEntity) item.getObject();
        if (timeType == TimeSelectView.MONTH) {
            String month
                    = TimeUtils.getTimeByCalendar(timeEntity.startYear, timeEntity.startMonth, timeEntity.startDay, SDFPattern.M_SDF_M);
            helper.setText(R.id.tv, month);
            boolean isPast = TimeUtils.isPast(timeEntity.startYear, timeEntity.startMonth, timeEntity.startDay);
            helper.getView(R.id.tv).setAlpha(isPast ? 1f : 0.2f);
            helper.setVisible(R.id.tv_day_start_stop, false);
        } else {
            helper.setText(R.id.tv, getWeekOrderText(timeEntity.order));
            String startTime
                    = TimeUtils.getTimeByCalendar(timeEntity.startYear, timeEntity.startMonth, timeEntity.startDay, SDFPattern.Md_SDF_P);
            String endTime
                    = TimeUtils.getTimeByCalendar(timeEntity.endYear, timeEntity.endMonth, timeEntity.endDay, SDFPattern.Md_SDF_P);
            helper.setText(R.id.tv_day_start_stop, startTime + "-" + endTime);
            boolean isPast = TimeUtils.isPast(timeEntity.startYear, timeEntity.startMonth, timeEntity.startDay);
            helper.getView(R.id.tv).setAlpha(isPast ? 1f : 0.2f);
            helper.getView(R.id.tv_day_start_stop).setAlpha(isPast ? 1f : 0.2f);
            helper.setVisible(R.id.tv_day_start_stop, true);
        }
    }

    private String getWeekOrderText(int weekOrder) {
        String result = "";
        if (weekOrder == 1) {
            result = "第一周";
        } else if (weekOrder == 2) {
            result = "第二周";
        } else if (weekOrder == 3) {
            result = "第三周";
        } else if (weekOrder == 4) {
            result = "第四周";
        } else if (weekOrder == 5) {
            result = "第五周";
        } else if (weekOrder == 6) {
            result = "第六周";
        } else if (weekOrder == 7) {
            result = "第七周";
        }
        return result;
    }
}
