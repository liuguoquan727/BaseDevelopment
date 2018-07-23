package com.banmaz.yongbang.module.home.adapter;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import com.mdroid.lib.recyclerview.BaseRecyclerViewAdapter;
import com.mdroid.lib.recyclerview.BaseViewHolder;
import com.banmaz.yongbang.R;
import com.banmaz.yongbang.bean.localbean.Reason;
import java.util.List;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2018/5/14 20:35.
 */
public class ReasonAdapter extends BaseRecyclerViewAdapter<Reason> {
  public ReasonAdapter(@NonNull List<Reason> data) {
    super(R.layout.module_home_list_item_single_choice, data);
  }

  @Override
  protected void convert(BaseViewHolder holder, Reason item) {
    holder.setText(R.id.tv_reason, item.reasonDesc);
    ImageView icon = holder.getView(R.id.tv_control);
    icon.setSelected(item.isSelected);
  }
}
