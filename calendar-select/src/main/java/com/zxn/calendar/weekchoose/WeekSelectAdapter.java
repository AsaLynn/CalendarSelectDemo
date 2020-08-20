package com.zxn.calendar.weekchoose;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zxn.calendar.MonthTimeEntity;
import com.zxn.calendar.R;

import org.jetbrains.annotations.NotNull;

/**
 * 参考:https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * Updated by zxn on 2020/8/18.
 */
public class WeekSelectAdapter extends BaseSectionQuickAdapter<TimeSectionEntity, BaseViewHolder> {
//    /**
//     * Same as QuickAdapter#QuickAdapter(Context,int) but with
//     * some initialization data.
//     *
//     * @param data A new list is created out of this one to avoid mutable list
//     */
//    public WeekSelectAdapter(List<MySection> data) {
//        super(R.layout.def_section_head);
//        //super(R.layout.def_section_head, data);
//        setNormalLayout(R.layout.item_section_content);
//    }

    public WeekSelectAdapter() {
        super(R.layout.def_section_head);
        setNormalLayout(R.layout.item_section_content);
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder helper, @NotNull TimeSectionEntity item) {
        if (item.getObject() instanceof MonthTimeEntity) {
            MonthTimeEntity entity = (MonthTimeEntity) item.getObject();
            String value = entity.year + "年" + entity.month + "月";
            helper.setText(R.id.tv_header, value);
        }
        /*if (item.isHeader()) {

        }*/
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull TimeSectionEntity item) {
        TimeEntity timeEntity = (TimeEntity) item.getObject();
        //helper.setText(R.id.tv, timeEntity.getName());
    }
}
