package com.lab.multiplexer.NewsForMe.Activity.Cell;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.lab.multiplexer.NewsForMe.Activity.Model.Ad;
import com.lab.multiplexer.NewsForMe.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdCell extends SimpleCell<Ad, NewsAdCell.ViewHolder> {

  public NewsAdCell(Ad ad) {
    super(ad);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.cell_news_ad;
  }

  @NonNull
  @Override
  protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
    return new ViewHolder(cellView);
  }

  @Override
  protected void onBindViewHolder(ViewHolder holder, int position, Context context, Object payload) {
    holder.textView.setText(getItem().getTitle());
    holder.itemView.setBackgroundColor(getItem().getColor());
  }

  @Override
  protected void onUnbindViewHolder(ViewHolder holder) {
  }

  @Override
  protected long getItemId() {
    return getItem().getId();
  }

  static class ViewHolder extends SimpleViewHolder {
    @BindView(R.id.textView)
    TextView textView;

    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

}
