package com.zxn.calendar.weekchoose;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zxn.calendar.MonthTimeEntity;
import com.zxn.calendar.R;
import com.zxn.time.SDFPattern;
import com.zxn.time.TimeUtils;

import org.jetbrains.annotations.NotNull;

/**
 * 参考:https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * Updated by zxn on 2020/8/18.
 */
public class WeekSelectAdapter extends BaseSectionQuickAdapter<TimeSectionEntity, BaseViewHolder> {

    public WeekSelectAdapter() {
        super(R.layout.def_section_head);
        setNormalLayout(R.layout.item_section_content);
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder helper, @NotNull TimeSectionEntity item) {
        if (item.getObject() instanceof MonthTimeEntity) {
            MonthTimeEntity entity = (MonthTimeEntity) item.getObject();
            String time = TimeUtils.getTimeByCalendar(entity.year, entity.month, 1, SDFPattern.yyyyM_SDF_YM);
            helper.setText(R.id.tv_header, time);
            /*String value = entity.year + "年" + entity.month + "月";
            helper.setText(R.id.tv_header, value);*/
        }
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull TimeSectionEntity item) {
        TimeEntity timeEntity = (TimeEntity) item.getObject();
        helper.setText(R.id.tv, getWeekOrderText(timeEntity.weekOrder));
        String startTime
                = TimeUtils.getTimeByCalendar(timeEntity.startYear, timeEntity.startMonth, timeEntity.startDay, SDFPattern.Md_SDF_P);
        String endTime
                = TimeUtils.getTimeByCalendar(timeEntity.endYear, timeEntity.endMonth, timeEntity.endDay, SDFPattern.Md_SDF_P);
        helper.setText(R.id.tv_day_start_stop, startTime + "-" + endTime);
        boolean isPast = TimeUtils.isPast(timeEntity.startYear, timeEntity.startMonth, timeEntity.startDay);
        helper.getView(R.id.tv).setAlpha(isPast ? 1f : 0.2f);
        helper.getView(R.id.tv_day_start_stop).setAlpha(isPast ? 1f : 0.2f);
        /*StringBuilder sbArray = new StringBuilder();
        for (int i = 0; i < timeEntity.dayArray.length; i++) {
            sbArray.append(timeEntity.dayArray[i]).append(",");
        }
        helper.setText(R.id.tv_day_start_stop, sbArray.toString());*/
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
