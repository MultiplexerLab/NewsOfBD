package com.lab.multiplexer.NewsForMe.Activity.Cell;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.jaychang.srv.Updatable;
import com.lab.multiplexer.NewsForMe.Activity.Model.News;
import com.lab.multiplexer.NewsForMe.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveCricketCell extends SimpleCell<News, LiveCricketCell.ViewHolder>
        implements Updatable<News> {

    private static final String KEY_TITLE = "KEY_TITLE";
    private boolean showHandle;

    public LiveCricketCell(News item) {
        super(item);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.cell_news;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
        return new ViewHolder(cellView);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, final int position, final Context context, Object payload) {
        if (payload != null) {
            // payload from updateCell()
            if (payload instanceof News) {
                holder.textView.setText(((News) payload).getTitle());
            }

            // payloads from updateCells()
            if (payload instanceof ArrayList) {
                List<News> payloads = ((ArrayList<News>) payload);
                holder.textView.setText(payloads.get(position).getTitle());
            }
            // payload from addOrUpdate()
            if (payload instanceof Bundle) {
                Bundle bundle = ((Bundle) payload);
                for (String key : bundle.keySet()) {
                    if (KEY_TITLE.equals(key)) {
                        holder.textView.setText(bundle.getString(key));
                    }
                }
            }
            return;
        }

        CardView.LayoutParams layoutParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(getDpValue(context, 8), getDpValue(context, 2), getDpValue(context, 8), 0);
        holder.main_card.setLayoutParams(layoutParams);
    /*    Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                String.format(Locale.US, "font/%s", "sutonny.ttf"));
        holder.textView.setTypeface(typeface);*/
        holder.textView.setText(getItem().getTitle());
        holder.source.setVisibility(View.GONE);
        holder.time.setVisibility(View.GONE);
        holder.dragHandle.setVisibility(View.GONE);
    }


    public int getDpValue(Context mContext, int yourdpmeasure) {
        Resources r = mContext.getResources();
        final float scale = mContext.getResources().getDisplayMetrics().density;
        // convert the DP into pixel
        int pixel = (int) (yourdpmeasure * scale + 0.5f);

        return pixel;
    }

    // Optional
    @Override
    protected void onUnbindViewHolder(ViewHolder holder) {
        // do your cleaning jobs here when the item view is recycled.
    }

    @Override
    protected long getItemId() {
        return getItem().getId();
    }

    public void setShowHandle(boolean showHandle) {
        this.showHandle = showHandle;
    }

    /**
     * If the titles of books are same, no need to update the cell, onBindViewHolder() will not be called.
     */
    @Override
    public boolean areContentsTheSame(News newItem) {
        return getItem().getTitle().equals(newItem.getTitle());
    }

    /**
     * If getItem() is the same as newItem (i.e. their return value of getItemId() are the same)
     * and areContentsTheSame()  return false, then the cell need to be updated,
     * onBindViewHolder() will be called with this payload object.
     */
    @Override
    public Object getChangePayload(News newItem) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, newItem.getTitle());
        return bundle;
    }

    public static class ViewHolder extends SimpleViewHolder {
        @BindView(R.id.textView)
        TextView textView;
        @BindView(R.id.description)
        TextView source;
        @BindView(R.id.dragHandle)
        ImageView dragHandle;
        @BindView(R.id.main_card)
        CardView main_card;
        @BindView(R.id.time)
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
